package com.dhivakar.filetransferhelper.database.repo;

import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepo extends JpaRepository<ImportRecord, Integer> {


}
