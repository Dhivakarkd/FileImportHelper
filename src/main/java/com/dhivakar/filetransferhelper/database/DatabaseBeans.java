package com.dhivakar.filetransferhelper.database;

import com.dhivakar.filetransferhelper.database.repo.BatchRepo;
import com.dhivakar.filetransferhelper.database.repo.ImportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class DatabaseBeans {

    @Autowired
    @Lazy
    private BatchRepo batchRepo;

    @Autowired
    @Lazy
    private ImportRepo importRepo;
}
