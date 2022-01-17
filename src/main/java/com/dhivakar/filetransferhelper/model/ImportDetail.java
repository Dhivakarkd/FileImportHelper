package com.dhivakar.filetransferhelper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetail implements Serializable {

    private String month;
    private String date;


}
