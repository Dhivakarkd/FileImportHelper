package com.dhivakar.filetransferhelper.database;

import com.dhivakar.filetransferhelper.database.model.BatchInfo;
import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import com.dhivakar.filetransferhelper.database.model.Status;
import com.dhivakar.filetransferhelper.database.repo.BatchRepo;
import com.dhivakar.filetransferhelper.database.repo.ImportRepo;
import com.dhivakar.filetransferhelper.listener.JobCompletionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DatabaseBeans {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchRepo batchRepo;

    @Autowired
    private ImportRepo importRepo;

    @Bean
    public RepositoryItemReader<BatchInfo> batchInfoItemReader() {

        RepositoryItemReader<BatchInfo> itemReader = new RepositoryItemReader<>();
        itemReader.setRepository(batchRepo);
        itemReader.setMethodName("findByBatchStatus");

        List<Object> queryMethodArguments = new ArrayList<>();
        queryMethodArguments.add(Status.PENDING.name());

        itemReader.setArguments(queryMethodArguments);

        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("batchId", Sort.Direction.DESC);
        itemReader.setSort(sorts);

        return itemReader;
    }

    @Bean
    public TempProcessor databaseProcessor() {
        return new TempProcessor();
    }

    @Bean
    public RepositoryItemWriter<ImportRecord> databaseWriter() {
        RepositoryItemWriter<ImportRecord> writer = new RepositoryItemWriter<>();
        writer.setRepository(importRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1()
            throws Exception {

        return this.stepBuilderFactory.get("step1").<BatchInfo, ImportRecord>chunk(5).reader(batchInfoItemReader())
                .processor(databaseProcessor()).writer(databaseWriter()).build();
    }

    @Bean
    public Job profileUpdateJob(JobCompletionListener listener, Step step1)
            throws Exception {

        return this.jobBuilderFactory.get("profileUpdateJob").incrementer(new RunIdIncrementer())
                .listener(listener).start(step1).build();
    }
}
