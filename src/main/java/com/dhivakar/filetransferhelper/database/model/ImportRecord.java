package com.dhivakar.filetransferhelper.database.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "IMPORT_DETAIL")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportRecord {

    @Column(name = "BATCH_ID")
    @Id
    private Integer batchId;
    @Column(name = "EXPORT_DATE")
    private LocalDate exportDate;
    @Column(name = "FILES_COPIED")
    private Integer filesCopied;
    @Column(name = "FILES_SKIPPED")
    private Integer filesSkipped;
    @Column(name = "ALREADY_EXISTED_FILES")
    private Integer alreadyExistedFiles;
    @Column(name = "INVALID_FILES")
    private Integer invalidExtensionFiles;
    @Column(name = "TOTAL_FILES_SCANNED")
    private Integer totalFilesScanned;
    @Column(name = "RECORD_UPDATED_TS")
    private LocalDateTime recordUpdatedTs;
}
