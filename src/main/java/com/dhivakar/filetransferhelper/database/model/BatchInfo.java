package com.dhivakar.filetransferhelper.database.model;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "BATCH_INFO")
public class BatchInfo {

    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "IMPORT_DATE")
    private LocalDate importDate;
}
