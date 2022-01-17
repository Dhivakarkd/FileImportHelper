package com.dhivakar.filetransferhelper.config;


import com.dhivakar.filetransferhelper.model.ImportDetail;
import com.dhivakar.filetransferhelper.processor.ImportProcessor;
import com.dhivakar.filetransferhelper.listener.JobCompletionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    // end::setup[]

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<ImportDetail> reader() {
        return new FlatFileItemReaderBuilder<ImportDetail>()
                .name("ImportDetailItemReader")
                .resource(new ClassPathResource("last-import-data.csv"))
                .delimited()
                .names(new String[]{"month", "date"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<ImportDetail>() {{
                    setTargetType(ImportDetail.class);
                }})
                .build();
    }

    @Bean
    public ImportProcessor processor() {
        return new ImportProcessor();
    }


    @Bean
    public FlatFileItemWriter<ImportDetail> writer()
    {
        BeanWrapperFieldExtractor<ImportDetail> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"month", "date"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<ImportDetail> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FlatFileItemWriter<ImportDetail> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setName("ImportDetailItemWriter");
        flatFileItemWriter.setResource(new FileSystemResource("test-outputs/ExportDetails.csv"));
        flatFileItemWriter.setLineAggregator(lineAggregator);
        flatFileItemWriter.setAppendAllowed(true);

        return flatFileItemWriter;
    }

    @Bean
    public Job readCSVFilesJob(JobCompletionListener listener) {
        return jobBuilderFactory
                .get("Read Last Imported Date from File")
                .listener(listener)
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<ImportDetail, ImportDetail>chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
