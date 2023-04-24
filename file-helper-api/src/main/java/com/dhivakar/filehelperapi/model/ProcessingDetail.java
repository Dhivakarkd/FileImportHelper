package com.dhivakar.filehelperapi.model;

import lombok.Data;

@Data
public class ProcessingDetail {
    private long totalFiles;
    private long invalidExtensionFileCounter;
    private int skippedFileCounter;
    private int movedFileCounter;
    private int alreadyExistingFileCounter;
    private int failedFileCounter;

    public void incrementSkippedFileCounter() {
        this.skippedFileCounter++;
    }

    public void incrementMovedFileCounter() {
        this.movedFileCounter++;
    }

    public void incrementAlreadyExistingFileCounter() {
        this.alreadyExistingFileCounter++;
    }

    public void incrementFailedFileCounter() {
        this.failedFileCounter++;
    }

    public void updateExtensionCounter() {
        this.invalidExtensionFileCounter = totalFiles - (skippedFileCounter + movedFileCounter + alreadyExistingFileCounter + failedFileCounter);
    }
}
