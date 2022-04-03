package com.dhivakar.filetransferhelper.config;


import com.dhivakar.filetransferhelper.database.handler.CustomDataReader;
import com.dhivakar.filetransferhelper.database.model.BatchInfo;
import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import com.dhivakar.filetransferhelper.listener.JobCompletionListener;
import com.dhivakar.filetransferhelper.listener.NoWorkListener;
import com.dhivakar.filetransferhelper.processor.ImportProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

    private StepBuilderFactory stepBuilderFactory;
    private CustomDataReader customDataReader;
    private CompositeItemWriter<ImportRecord> compositeItemWriter;
    private ImportProcessor filesProcessor;
    @Autowired
    private NoWorkListener noWorkListener;

    @Autowired
    public BatchConfiguration(StepBuilderFactory stepBuilderFactory, CustomDataReader customDataReader, CompositeItemWriter<ImportRecord> compositeItemWriter, ImportProcessor filesProcessor) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.customDataReader = customDataReader;
        this.compositeItemWriter = compositeItemWriter;
        this.filesProcessor = filesProcessor;
    }

    @Bean
    public Step readAndCopyFiles() {

        return this.stepBuilderFactory.get("Read and Copy Files").<BatchInfo, ImportRecord>chunk(5)
                .reader(customDataReader)
                .processor(filesProcessor)
                .writer(compositeItemWriter)
                .listener(noWorkListener)
                .build();
    }

    @Bean
    public Job analyzeAndCopyLatestFilesJob(JobBuilderFactory jobBuilderFactory, JobCompletionListener listener) {

        return jobBuilderFactory.get("Analyze and Copy Latest Files")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(readAndCopyFiles()).on(ExitStatus.FAILED.getExitCode()).fail()
                .from(readAndCopyFiles()).on(ExitStatus.COMPLETED.getExitCode()).end().build()
                .build();
    }

}
