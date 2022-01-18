package com.dhivakar.filetransferhelper.processor;

import com.dhivakar.filetransferhelper.model.ImportDetail;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class LinesWriter implements Tasklet, StepExecutionListener {

    private CSVWriter csvWriter;
    private FileWriter fileWriter;
    private File file;

    @Value("${spring.profiles.active}")
    private String env;


    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Value is "+env);
        if(!StringUtils.equalsIgnoreCase("dev",env)) {
            log.debug("Latest Import Date is Updated");
            writeLine(new ImportDetail(LocalDate.now().getMonth().name(), LocalDate.now().toString()));
        }else{
            log.info("Dev Mode is Active \"Skipping\" Updating Import Details");
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        try {
            initWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        closeWriter();
        log.debug("Line Writer ended.");
        return ExitStatus.COMPLETED;
    }

    private void initWriter() throws IOException {
        if (file == null) {
            file = new FileSystemResource("test-outputs/last-import-data.csv").getFile();

        }


        if (fileWriter == null) {
            new FileWriter(file, false).close();
            fileWriter = new FileWriter(file, true);
        }

        if (csvWriter == null) csvWriter = new CSVWriter(fileWriter);

    }

    public void writeLine(ImportDetail detail) {
        try {
            if (csvWriter == null) initWriter();
            String[] lineStr = new String[2];
            lineStr[0] = detail.getMonth();
            lineStr[1] = detail.getDate();

            csvWriter.writeNext(lineStr);
        } catch (Exception e) {
            log.error("Error while writing line in file");
        }
    }

    public void closeWriter() {
        try {
            csvWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            log.error("Error while closing writer.");
        }
    }
}
