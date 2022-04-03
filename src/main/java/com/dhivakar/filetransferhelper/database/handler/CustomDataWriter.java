package com.dhivakar.filetransferhelper.database.handler;

import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import com.dhivakar.filetransferhelper.database.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@Configuration
public class CustomDataWriter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcBatchItemWriter<ImportRecord> importDbWriter() {
        JdbcBatchItemWriter<ImportRecord> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO ImportDBDev.IMPORT_DETAIL\n" +
                "(BATCH_ID, EXPORT_DATE, FILES_COPIED, FILES_SKIPPED, ALREADY_EXISTED_FILES, INVALID_FILES, TOTAL_FILES_SCANNED, RECORD_UPDATED_TS)\n" +
                "VALUES (:batchId, :exportDate, :filesCopied, :filesSkipped, :alreadyExistedFiles, :invalidExtensionFiles, :totalFilesScanned, :recordUpdatedTs)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return itemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<ImportRecord> batchDbWriter() {
        JdbcBatchItemWriter<ImportRecord> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("UPDATE BATCH_INFO\n" +
                "SET RECORD_UPDATED_TS = :recordUpdatedTs, BATCH_STATUS = :batchStatus\n" +
                "WHERE BATCH_ID = :batchId;");
        itemWriter.setItemSqlParameterSourceProvider(item -> new MapSqlParameterSource()
                .addValue("recordUpdatedTs", item.getRecordUpdatedTs())
                .addValue("batchId", item.getBatchId())
                .addValue("batchStatus", Status.COMPLETED.name()));
        return itemWriter;
    }

    @Bean
    public JdbcBatchItemWriter<ImportRecord> newBatchIfoWriter() {
        JdbcBatchItemWriter<ImportRecord> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO ImportDBDev.BATCH_INFO\n" +
                "(IMPORT_DATE, RECORD_UPDATED_TS, BATCH_STATUS)\n" +
                "VALUES(:importDate, :recordUpdatedTs, :batchStatus);\n");
        itemWriter.setItemSqlParameterSourceProvider(item -> new MapSqlParameterSource()
                .addValue("recordUpdatedTs", item.getRecordUpdatedTs())
                .addValue("importDate", LocalDate.now().toString())
                .addValue("batchStatus", Status.PENDING.name()));
        return itemWriter;
    }

    @Bean
    public CompositeItemWriter<ImportRecord> compositeItemWriter() {
        CompositeItemWriter<ImportRecord> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(Arrays.asList(importDbWriter(), batchDbWriter(), newBatchIfoWriter()));
        return compositeItemWriter;
    }

}
