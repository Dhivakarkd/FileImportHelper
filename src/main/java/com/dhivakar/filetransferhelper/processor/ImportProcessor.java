package com.dhivakar.filetransferhelper.processor;

import com.dhivakar.filetransferhelper.model.ImportDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Slf4j
public class ImportProcessor implements ItemProcessor<ImportDetail,ImportDetail> {


    @Override
    public ImportDetail process(ImportDetail item) throws IOException {
        log.info("Last Imported Date is {}",item.getDate());

        File folder = new File("/");
        File[] listOfFiles = folder.listFiles();

        LocalDate date1 = LocalDate.parse(item.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.println(date1);



        for (File file : listOfFiles) {


            if (file.isFile()) {

                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());

                LocalDate localDate = dateTime.toLocalDate();

                if(localDate.isAfter(date1)){
                    System.out.println(file.getName());
                }


            }

        }
        log.info("Processsing Input");
        log.info(item.toString());
        LocalDate date = LocalDate.now();
        return new ImportDetail(date.getMonth().name(),date.toString());
    }
}
