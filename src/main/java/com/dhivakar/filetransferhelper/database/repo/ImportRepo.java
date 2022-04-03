package com.dhivakar.filetransferhelper.database.repo;

import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepo extends CrudRepository<ImportRecord, Integer> {


}
