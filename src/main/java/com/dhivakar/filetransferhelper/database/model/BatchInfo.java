package com.dhivakar.filetransferhelper.database.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BATCH_INFO")
public class BatchInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "IMPORT_DATE")
    private LocalDate importDate;
    @Column(name = "RECORD_UPDATED_TS")
    private LocalDateTime recordUpdatedTimeStamp;
    @Column(name = "BATCH_STATUS")
    private String batchStatus;

}
