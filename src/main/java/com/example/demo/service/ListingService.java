package com.example.demo.service;

import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.Customer;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.Orders;
import com.example.demo.model.SellerDetails;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ListingFileUploadRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.SellerDetailsRepository;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ListingService {

    private final ListingFileUploadRepository listingFileUploadRepository;
    private final AuthService authService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final SellerDetailsRepository sellerDetailsRepository;

//    public ResponseEntity<String> uploadBulkListing(MultipartFile file){
//        log.info("handling request parts "+file.getOriginalFilename());
//        String savingPath = "etc/seller-app/uploads/bulk-listing/bulk_upload_file";
//        String filename = Instant.now().toString()+".xlsx";
//        try {
//            File f = new ClassPathResource("").getFile();
//            final Path path = Paths.get(savingPath +File.separator+ filename);
//
//            if (!Files.exists(path)) {
//                Files.createDirectories(path);
//            }
//
//            Path filePath = path.resolve(file.getOriginalFilename());
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            addFileToDataBase(file.getOriginalFilename(), savingPath);
//            return new ResponseEntity<>("done",HttpStatus.OK);
//
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    private void addFileToDataBase(String filename, String location){
//        try{
//            ListingFileUpload listingFileUpload = new ListingFileUpload();
//            listingFileUpload.setFileName(filename);
//            listingFileUpload.setLocation(location);
//            listingFileUpload.setDateTime(Instant.now());
//            listingFileUpload.setStatus(Status.PENDING);
//            listingFileUpload.setUser(authService.getCurrentUser());
//            listingFileUploadRepository.save(listingFileUpload);
//        }catch (Exception e){
//            throw new SpringException("Error occurred while adding file to database");
//        }
//    }

//    public ResponseEntity<Page<ListingFileUpload>> getUploadedFiles(int page, int size){
//        Pageable pageRequest = PageRequest.of(page, size);
//
//        Page<ListingFileUpload> pageResult = listingFileUploadRepository.findAllByUser(
//                authService.getCurrentUser(),
//                pageRequest).get();
//        return new ResponseEntity<>(pageResult, HttpStatus.OK);
//    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    private void readData(){

        Optional <List<ListingFileUpload>> list = listingFileUploadRepository
                .findAllByStatus(
                        Status.PENDING);

        log.info("total file in pending status "+list.get().size());
        if (list.isPresent()){
            for (ListingFileUpload file : list.get()){
                readFile(file);
            }
        }
    }

    private void readFile(ListingFileUpload file){
        try {
            file.setStatus(
                    Status.PROCESSING);
            listingFileUploadRepository.save(file);
            Workbook workbook = Workbook.getWorkbook(new File(file.getLocation()+File.separator+file.getFileName()));
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int columns = sheet.getColumns();
            Optional<SellerDetails> sellerDetails = sellerDetailsRepository.findById(Long.valueOf(sheet.getCell(0,1).getContents()));
            if (sellerDetails.isEmpty()){
                new SpringException("Seller not found for provided ID");
            }
            List<Orders> list = new ArrayList<>();

            for(int i = 2; i < rows ; i++){
                Orders order = new Orders();
                Optional<Customer> customer = customerRepository
                        .findByContactNo(sheet.getCell(0,i).getContents());
                if (customer.isPresent()){
                    Customer cus = customer.get();
                    order.setCustomer(cus);
                }else{
                    Customer cus = new Customer();
                    cus.setContactNo(Long.valueOf(sheet.getCell(0,i).getContents()));
                    cus.setEmail(sheet.getCell(1,i).getContents());
                    cus.setName(sheet.getCell(2,i).getContents());
                    cus.setAddress(sheet.getCell(3,i).getContents());
                    cus.setDistrict(sheet.getCell(4,i).getContents());
                    order.setCustomer(customerRepository.save(cus));
                }

                order.setOrderDescription(sheet.getCell(5,i).getContents());
                order.setPrice(Double.valueOf(sheet.getCell(6,i).getContents()));
                order.setDeliveryCharge(Double.valueOf(sheet.getCell(7,i).getContents()));
                order.setSellerDetails(sellerDetails.get());
                list.add(order);
            }
            log.info("readed excel file contents ",list);
//            orderRepository.saveAll(list);

        } catch (IOException | BiffException e) {
            file.setStatus(Status.ERROR);
            listingFileUploadRepository.save(file);
            log.error("reading excel "+e.getMessage());

        }

    }


}
