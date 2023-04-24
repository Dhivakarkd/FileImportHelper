package com.dhivakar.filehelperapi.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Data
@Slf4j
public class FileContext {
    private String source;
    private String target;
    private int upto;
    private String validExtensions; //comma separated values


    public void printInfo() {
        log.info("Source Dir : {}", source);
        log.info("Target Dir : {}", target);
        log.info("Filter by Month : {}", LocalDate.now().minusMonths(upto));
        log.info("Valid Extensions : {}", validExtensions);
    }
}
