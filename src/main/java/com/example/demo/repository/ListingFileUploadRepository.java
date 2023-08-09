package com.example.demo.repository;

import com.example.demo.enums.Status;
import com.example.demo.model.ListingFileUpload;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingFileUploadRepository extends PagingAndSortingRepository<ListingFileUpload, Long> {
    Optional<List<ListingFileUpload>> findAllByStatus(Status status);
    Optional<Page<ListingFileUpload>> findAllByUsernameOrderByIdDesc(String username, Pageable pageable);
}
