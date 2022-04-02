package com.dhivakar.filetransferhelper.database.repo;

import com.dhivakar.filetransferhelper.database.model.BatchInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepo extends JpaRepository<BatchInfo, Integer> {

    Page<BatchInfo> findByBatchStatus(String batchStatus, Pageable pageable);
}
