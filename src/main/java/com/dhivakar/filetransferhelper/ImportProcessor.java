package com.dhivakar.filetransferhelper;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;

public class ImportProcessor implements ItemProcessor<ImportDetail,ImportDetail> {


    @Override
    public ImportDetail process(ImportDetail item){
        LocalDate date = LocalDate.now();
        System.out.println("Processsing Input");
        System.out.println(item);
        return new ImportDetail(date.getMonth().name(),date.toString());
    }
}
