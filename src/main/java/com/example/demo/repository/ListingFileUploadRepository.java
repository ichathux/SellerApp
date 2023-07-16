package com.example.demo.repository;

import com.example.demo.enums.Status;
import com.example.demo.model.ListingFileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListingFileUploadRepository extends PagingAndSortingRepository<ListingFileUpload, Long> {
    Optional<Page<List<ListingFileUpload>>> findAllByStatus(Status status, Pageable pageable);
}
