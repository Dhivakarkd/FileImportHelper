package com.dhivakar.filetransferhelper.database.repo;

import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import org.springframework.data.repository.CrudRepository;

public interface ImportRepo extends CrudRepository<ImportRecord, Integer> {
}
