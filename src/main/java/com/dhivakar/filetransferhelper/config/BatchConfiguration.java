package com.dhivakar.filetransferhelper.config;


import com.dhivakar.filetransferhelper.listener.JobCompletionListener;
import com.dhivakar.filetransferhelper.model.ImportDetail;
import com.dhivakar.filetransferhelper.processor.ImportProcessor;
import com.dhivakar.filetransferhelper.processor.LinesWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<ImportDetail> reader() {
        return new FlatFileItemReaderBuilder<ImportDetail>()
                .name("ImportDetailItemReader")
                .resource(new FileSystemResource("test-outputs/last-import-data.csv"))
                .delimited()
                .names("month", "date")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<ImportDetail>() {{
                    setTargetType(ImportDetail.class);
                }})
                .build();
    }


    @Bean
    public FlatFileItemWriter<String> importDetailsWriter() {
        return new FlatFileItemWriterBuilder<String>()
                .name("importDetailsWriter")
                .resource(new FileSystemResource("test-outputs/ExportDetails.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .append(true)
                .build();
    }

    @Bean
    public ImportProcessor processor() {
        return new ImportProcessor();
    }

    @Bean
    public LinesWriter linesWriter() {
        return new LinesWriter();
    }


    @Bean
    public Job latestFilesImportJob(JobCompletionListener listener) {
        return jobBuilderFactory
                .get("Analyze and Copy Latest Files")
                .listener(listener)
                .incrementer(new RunIdIncrementer())
                .start(readAndCopyFiles())
                .next(updateLatestImportDate())
                .build();
    }

    @Bean
    public Step readAndCopyFiles() {
        return stepBuilderFactory.get("Read and Copy Files").<ImportDetail, String>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(importDetailsWriter())
                .build();
    }

    @Bean
    protected Step updateLatestImportDate() {
        return stepBuilderFactory
                .get("updateLatestImportDate")
                .tasklet(linesWriter())
                .build();
    }


}
