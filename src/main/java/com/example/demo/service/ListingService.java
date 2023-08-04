package com.example.demo.service;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.OrderListDto;
import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private final UserRepository userRepository;

    private final ListingFileUploadRepository listingFileUploadRepository;
    private final AuthService authService;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final SellerDetailsRepository sellerDetailsRepository;
    private final UserAuthProvider userAuthProvider;

    public ResponseEntity<String> uploadBulkListing(MultipartFile file , String token) {
        log.info("handling request parts " + file.getOriginalFilename());
        String savingPath = "/etc/seller-app/uploads/bulk-listing/bulk_upload_file";
        String folderName = Instant.now().toString();
        try {
            File f = new ClassPathResource("").getFile();
            final Path path = Paths.get(savingPath + File.separator + folderName);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream() , filePath , StandardCopyOption.REPLACE_EXISTING);
            User user = userAuthProvider.getCurrentUserByToken(token);
            addFileToDataBase(folderName , file.getOriginalFilename() , savingPath , user);
            return new ResponseEntity<>("done" , HttpStatus.OK);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addFileToDataBase(String folderName , String filename , String location , User user) {
        try {
            ListingFileUpload listingFileUpload = new ListingFileUpload();
            listingFileUpload.setFileName(filename);
            listingFileUpload.setLocation(location + File.separator + folderName);
            listingFileUpload.setDateTime(Instant.now());
            listingFileUpload.setStatus(Status.PENDING);
            listingFileUpload.setUser(user);
//            readFile(listingFileUploadRepository.save(listingFileUpload));
        } catch (Exception e) {
            throw new SpringException("Error occurred while adding file to database");
        }
    }

    public ResponseEntity<Page<ListingFileUpload>> getUploadedFiles(int page , int size , String token) {
        Pageable pageRequest = PageRequest.of(page , size);

        Page<ListingFileUpload> pageResult = listingFileUploadRepository.findAllByUserOrderByIdDesc(
                userAuthProvider.getCurrentUserByToken(token) ,
                pageRequest).get();
        return new ResponseEntity<>(pageResult , HttpStatus.OK);
    }

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

    private void readFile(ListingFileUpload file) {
        log.info("start reading file ");
        try {
            file.setStatus(
                    Status.PROCESSING);
            listingFileUploadRepository.save(file);
            FileInputStream fileInputStream = new FileInputStream(file.getLocation() + File.separator + file.getFileName());
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assuming the data is on the first sheet

            long sellerId = (long) sheet.getRow(0).getCell(1).getNumericCellValue();
            Optional<User> user = userRepository
                    .findById(sellerId);
            Optional<SellerDetails> sellerDetails = sellerDetailsRepository.findByUser(user.get());

            int rowCount = sheet.getLastRowNum();
            int columnCount = sheet.getRow(1).getLastCellNum();
            ArrayList<Orders> list = new ArrayList<>();
            System.out.println("Row - " + rowCount + " Cells " + columnCount);
            for (Row row : sheet) {

                Orders order = new Orders();

                if (row.getRowNum() > 1) {
                    Optional<Customer> customer = customerRepository
                            .findByContactNo(Long.valueOf(getCellValueAsString(row.getCell(0))));
                    if (customer.isPresent()) {
                        Customer cus = customer.get();
                        order.setCustomer(cus);
                    } else {
                        Customer cus = new Customer();
                        cus.setContactNo(Long.valueOf(getCellValueAsString(row.getCell(0))));
                        cus.setEmail(getCellValueAsString(row.getCell(1)));
                        cus.setName(getCellValueAsString(row.getCell(2)));
                        cus.setAddress(getCellValueAsString(row.getCell(3)));
                        cus.setDistrict(getCellValueAsString(row.getCell(4)));
                        order.setCustomer(customerRepository.save(cus));
                    }
                    order.setOrderDescription(getCellValueAsString(row.getCell(5)));
                    order.setPrice(Double.valueOf(getCellValueAsString(row.getCell(6))));
                    order.setDeliveryCharge(Double.valueOf(getCellValueAsString(row.getCell(7))));
                    order.setSellerDetails(sellerDetails.get());
                    order.setStatus(Status.READY_TO_PACK);
                    order.setCreatedAt(Instant.now());
                    order.setUpdatedAt(Instant.now());
                    list.add(order);

                }

            }
            orderRepository.saveAll(list);
            workbook.close();
            fileInputStream.close();
            file.setStatus(Status.READY_TO_DOWNLOAD);
            listingFileUploadRepository.save(file);
        } catch (FileNotFoundException ex) {
            file.setStatus(Status.ERROR);
            listingFileUploadRepository.save(file);
            log.error("reading excel " + ex.getMessage());
        } catch (IOException ex) {
            file.setStatus(Status.ERROR);
            listingFileUploadRepository.save(file);
            log.error("reading excel " + ex.getMessage());
        } catch (Exception e) {
            file.setStatus(Status.ERROR);
            listingFileUploadRepository.save(file);
            log.error("reading excel " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        String cellValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    break;
                case NUMERIC:
                case FORMULA:
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                default:
                    cellValue = "";
            }
        }
        return cellValue;

    }

    public ResponseEntity<Page<Orders>> getListedOrders(int page , int size , String token){
        Pageable pageRequest = PageRequest.of(page , size);

        Optional<SellerDetails> sellerDetails = sellerDetailsRepository.findByUser(userAuthProvider.getCurrentUserByToken(token));
        Optional<Page<Orders>> orders = orderRepository.findAllBySellerDetailsOrderByIdDesc(sellerDetails.get(), pageRequest);

        if (orders.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        Page<Orders> pageResult = orders.get();
        return new ResponseEntity<>(pageResult , HttpStatus.OK);
    }


    public ResponseEntity<String> addSingleOrder(String token , Orders orders) {

        try{
            Optional<Customer> customer = customerRepository.findByContactNo(orders.getCustomer().getContactNo());
            if (customer.isPresent()){
                orders.setCustomer(customer.get());
            }else{
                orders.setCustomer(customerRepository.save(orders.getCustomer()));;
            }
            User user = userAuthProvider.getCurrentUserByToken(token);
            SellerDetails sellerDetails = sellerDetailsRepository.findByUser(user).get();
            orders.setSellerDetails(sellerDetails);
            orders.setCreatedAt(Instant.now());
            orders.setUpdatedAt(Instant.now());
            System.out.println(orders);
            orderRepository.save(orders);
            return new ResponseEntity<>("done",HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.getStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<String> generateBulkUploadExcelFile(String token , List<Orders> list) {

        return null;
    }
}
