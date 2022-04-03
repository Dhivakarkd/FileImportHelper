package com.dhivakar.filetransferhelper.utils;

import com.dhivakar.filetransferhelper.database.model.ImportRecord;
import com.dhivakar.filetransferhelper.model.ProcessingDetail;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ImportRecordDecorator {

    private ImportRecordDecorator() {

        //Nothing to do
    }

    public static ImportRecord convertToImportRecord(@NonNull ProcessingDetail processingDetail, int batchId) {

        return ImportRecord.builder()
                .batchId(batchId)
                .exportDate(LocalDate.now())
                .alreadyExistedFiles(processingDetail.getNoOfFilesAlreadyExist())
                .filesCopied(processingDetail.getNoOfFilesMoved())
                .filesSkipped(processingDetail.getNoOfFilesSkipped())
                .invalidExtensionFiles(processingDetail.getNoOfFilesInvalidExt())
                .totalFilesScanned(processingDetail.getTotalFiles())
                .recordUpdatedTs(LocalDateTime.now())
                .build();


    }
}
