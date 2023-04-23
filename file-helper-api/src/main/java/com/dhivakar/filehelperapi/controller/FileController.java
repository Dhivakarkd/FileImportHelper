package com.dhivakar.filehelperapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class FileController {


    @GetMapping(value = "/api")
    public String greeting() {
        return "hello world";
    }
}
