package com.example.demo.service;

import com.example.demo.enums.Status;
import com.example.demo.exception.SpringException;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.repository.ListingFileUploadRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ListingService {

    private final ListingFileUploadRepository listingFileUploadRepository;
    public ResponseEntity<String> uploadBulkListing(MultipartFile file){
        log.info("handling request parts "+file.getOriginalFilename());
        String savingPath = "etc/seller-app/uploads/bulk-listing/bulk_upload_file";
        try {
            File f = new ClassPathResource("").getFile();
            final Path path = Paths.get(savingPath +File.separator+ file.getName());

            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Path filePath = path.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            addFileToDataBase(file.getOriginalFilename(), savingPath);
            return new ResponseEntity<>("done",HttpStatus.OK);

        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addFileToDataBase(String filename, String location){
        try{
            ListingFileUpload listingFileUpload = new ListingFileUpload();
            listingFileUpload.setFileName(filename);
            listingFileUpload.setLocation(location);
            listingFileUpload.setDateTime(Instant.now());
            listingFileUpload.setStatus(Status.PENDING);
            listingFileUploadRepository.save(listingFileUpload);
        }catch (Exception e){
            throw new SpringException("Error occurred while adding file to database");
        }


//        readData();
    }



    public ResponseEntity<Page<ListingFileUpload>> getUploadedFiles(int page, int size){
        Pageable pageRequest = PageRequest.of(page, size);
        Page<ListingFileUpload> pageResult = listingFileUploadRepository.findAll(pageRequest);
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
    @Scheduled(fixedDelay = 5400)
    private void readData(){
        System.out.println("thread running ");
        Pageable firstPageWithTwoElements = PageRequest.of(0, 2);
        Optional <Page<List<ListingFileUpload>>> list = listingFileUploadRepository.findAllByStatus(Status.PENDING, firstPageWithTwoElements);
        if (list.isPresent()){
//            for (ListingFileUpload file : list.get()){
//                readFile(file.getFileName(), file.getLocation());
//            }
        }
    }

    private void readFile(String filename, String location){

    }


}
