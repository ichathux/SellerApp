package com.example.demo.service.impl;

import com.example.demo.config.UserAuthProvider;
import com.example.demo.dto.BulkInputDto;
import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.ListingService;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ListingServiceImpl implements ListingService {

    private final ListingFileUploadRepository listingFileUploadRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final UserAuthProvider userAuthProvider;
    private final HashMap<String, BulkInputDto> listHashMap;
    private final ApplicationParamService applicationParamService;

    public ResponseEntity<String> uploadBulkListing(MultipartFile file) {
        log.info("handling request parts " + file.getOriginalFilename());
        String savingPath = applicationParamService.getUploadBulkOrdersSavingPath();
        String folderName = Instant.now().toString();
        try {
            File f = new ClassPathResource("").getFile();
            final Path path = Paths.get(savingPath + File.separator + folderName);

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.copy(file.getInputStream() , filePath , StandardCopyOption.REPLACE_EXISTING);
            addFileToDataBase(folderName , file.getOriginalFilename() , savingPath);

            return new ResponseEntity<>("done" , HttpStatus.OK);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addFileToDataBase(String folderName , String filename , String location) {
        try {
            log.info("adding file " + filename + " to database");
            ListingFileUpload listingFileUpload = new ListingFileUpload();
            listingFileUpload.setFileName(filename);
            listingFileUpload.setLocation(location + File.separator + folderName);
            listingFileUpload.setDateTime(Instant.now());
            listingFileUpload.setStatus(Status.PENDING);
            listingFileUpload.setUsername(userAuthProvider.getCurrentUserUsername());
            listingFileUploadRepository.save(listingFileUpload);
        } catch (Exception e) {
            throw new SpringException("Error occurred while adding file to database");
        }
    }

    public ResponseEntity<Page<ListingFileUpload>> getUploadedFiles(int page ,
                                                                    int size) {
        Pageable pageRequest = PageRequest
                .of(page , size);

        Optional<Page<ListingFileUpload>> pageResult = listingFileUploadRepository
                .findAllByUsernameOrderByIdDesc(
                        userAuthProvider.getCurrentUserUsername() ,
                        pageRequest);

        return pageResult
                .map(listingFileUploads -> new ResponseEntity<>(listingFileUploads , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.MINUTES)
    private void readData() {
        readBulkExcelUploadValues();
        readBulkNormalInputValues();
    }

    private void readBulkExcelUploadValues() {
        Optional<List<ListingFileUpload>> list = listingFileUploadRepository
                .findAllByStatus(
                        Status.PENDING);

        log.info("total file in pending status " + list.get().size());
        if (list.isPresent()) {
            for (ListingFileUpload file : list.get()) {
                readFile(file);
            }
        }
    }

    private void readBulkNormalInputValues() {
        log.info("reading item list from hashmap");
        var list2 = listHashMap.entrySet()
                .stream()
                .filter(s -> s.getValue().getStatus().equals(Status.PENDING))
                .collect(Collectors.toList());

        List<Orders> orders = new ArrayList<>();
        for (var item : list2) {
            BulkInputDto bulkInputDto = item.getValue();
            orders = makeOrderObject(bulkInputDto , orders);
            bulkInputDto.setStatus(Status.COMPLETE);
            listHashMap.replace(item.getKey() , bulkInputDto);
        }
        orderRepository.saveAll(orders);
    }

    private List<Orders> makeOrderObject(BulkInputDto bulkInputDto ,
                                         List<Orders> orders) {
        for (Orders order : bulkInputDto.getOrders()) {
            order = checkCustomer(order);
            orders.add(order);
        }
        return orders;
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
                    order.setSellerUsername(file.getUsername());
                    order.setStatus(Status.ORDER_PLACED);
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

    public ResponseEntity<Page<Orders>> getListedOrders(int page ,
                                                        int size) {
        Pageable pageRequest = PageRequest.of(page , size);

        Optional<Page<Orders>> orders = orderRepository
                .findAllBySellerUsernameOrderByIdDesc(userAuthProvider.getCurrentUserUsername() , pageRequest);

        if (orders.isEmpty()) {
            return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);
        }
        Page<Orders> pageResult = orders.get();
        return new ResponseEntity<>(pageResult , HttpStatus.OK);
    }


    public ResponseEntity<String> addSingleOrder(Orders orders) {

        try {
            orders = checkCustomer(orders);
            orders.setCreatedAt(Instant.now());
            orders.setUpdatedAt(Instant.now());
            orderRepository.save(orders);
            return new ResponseEntity<>("done" , HttpStatus.OK);
        } catch (Exception e) {
            e.getStackTrace();
            return new ResponseEntity<>(e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



    private Orders checkCustomer(Orders orders) {
        Optional<Customer> customer = customerRepository.findByContactNo(orders.getCustomer().getContactNo());
        if (customer.isPresent()) {
            orders.setCustomer(customer.get());
        } else {
            orders.setCustomer(customerRepository.save(orders.getCustomer()));
            ;
        }
        return orders;
    }

    public ResponseEntity<String> generateBulkUploadList(String token , List<Orders> list) {
        String username = userAuthProvider.getCurrentUserUsername();
        BulkInputDto bulkInputDto = new BulkInputDto(Instant.now() , list , Status.PENDING , username);
        listHashMap.put(username , bulkInputDto);
        return new ResponseEntity<>("done" , HttpStatus.OK);
    }

    public ResponseEntity<List<BulkInputDto>> getBulkInputList() {
        String username = userAuthProvider.getCurrentUserUsername();
        System.out.println(listHashMap);
        ArrayList<BulkInputDto> list = new ArrayList<>();
        if (!listHashMap.isEmpty()) {
            if (listHashMap.containsKey(username)) {
                for (String un : listHashMap.keySet()) {
                    if (un.trim().equals(username.trim())) {
                        list.add(listHashMap.get(un));
                    }
                }
                return new ResponseEntity<>(list , HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(null , HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<Page<Orders>> getOrdersByDate(Instant startDate ,
                                                        Instant endDate ,
                                                        int page ,
                                                        int size) {
        Pageable pageRequest = PageRequest
                .of(page , size);
        String username = userAuthProvider
                .getCurrentUserUsername();
        Optional<Page<Orders>> orders = orderRepository
                .findBySellerUsernameAndCreatedAtBetweenOrderByIdDesc(username , startDate , endDate , pageRequest);
        return orders
                .map(ordersPage -> new ResponseEntity<>(ordersPage , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }

    public ResponseEntity<Page<Orders>> getOrdersById(Long id ,
                                                      int page ,
                                                      int size) {

        String username = userAuthProvider.getCurrentUserUsername();

        Pageable pageRequest = PageRequest.of(page , size);
        Optional<Page<Orders>> orders = orderRepository
                .findByIdAndSellerUsernameOrderByIdDesc(id , username , pageRequest);
        return orders
                .map(ordersPage -> new ResponseEntity<>(ordersPage , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }

    public ResponseEntity<Page<Orders>> getOrdersByContactNumber(String contactNo ,
                                                                 int page ,
                                                                 int size) {

        String username = userAuthProvider.getCurrentUserUsername();
        Pageable pageRequest = PageRequest.of(page , size);
        Customer customer = customerRepository
                .findByContactNo(Long.valueOf(contactNo))
                .orElseGet(Customer::new);
        Optional<Page<Orders>> orders = orderRepository
                .findAllByCustomerAndSellerUsernameOrderByIdDesc(customer , username , pageRequest);
        return orders
                .map(ordersPage -> new ResponseEntity<>(ordersPage , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null , HttpStatus.NO_CONTENT));
    }


}
