package com.dhivakar.filetransferhelper.database.repo;

import com.dhivakar.filetransferhelper.database.model.BatchInfo;
import org.springframework.data.repository.CrudRepository;

public interface BatchRepo extends CrudRepository<BatchInfo, Integer> {
}
