package com.dhivakar.filetransferhelper.database.model;


import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "IMPORT_DETAIL")
public class ImportRecord {

    @Column(name = "BATCH_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer batchId;
    @Column(name = "EXPORT_DATE")
    @CreatedDate
    private LocalDate exportDate;
    @Column(name = "FILES_COPIED")
    private String filesCopied;
    @Column(name = "FILES_SKIPPED")
    private String filesSkipped;
    @Column(name = "ALREADY_EXISTED_FILES")
    private String alreadyExistedFiles;
    @Column(name = "INVALID_FILES")
    private String invalidExtensionFiles;
    @Column(name = "TOTAL_FILES_SCANNED")
    private String totalFilesScanned;
    @Column(name = "RECORD_UPDATED_TS")
    @CreationTimestamp
    private LocalDateTime recordUpdatedTs;
}
