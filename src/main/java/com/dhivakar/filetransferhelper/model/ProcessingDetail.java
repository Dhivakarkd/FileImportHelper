package com.dhivakar.filetransferhelper.model;

import com.dhivakar.filetransferhelper.exception.ValidationException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProcessingDetail {

    private String sourceFolder;
    private int totalFiles;
    private int noOfFilesMoved;
    private int noOfFilesSkipped;
    private int noOfFilesAlreadyExist;
    private int noOfFilesInvalidExt;

    public ProcessingDetail(String sourceFolder, int totalFiles) {
        this.sourceFolder = sourceFolder;
        this.totalFiles = totalFiles;
        this.noOfFilesMoved = 0;
        this.noOfFilesSkipped = 0;
        this.noOfFilesAlreadyExist = 0;
        this.noOfFilesInvalidExt = 0;
    }

    public void incrementMovedFileCounter() {
        this.noOfFilesMoved++;
    }

    public void incrementSkippedFileCounter() {
        this.noOfFilesSkipped++;
    }

    public void incrementAlreadyExistingFileCounter() {
        this.noOfFilesAlreadyExist++;
    }

    public void incrementInvalidExtensionFileCounter() {
        this.noOfFilesInvalidExt++;
    }


    public void validateTotalCount() throws ValidationException {

        int processTotal = noOfFilesMoved + noOfFilesSkipped + noOfFilesAlreadyExist + noOfFilesInvalidExt;

        if (processTotal != totalFiles) {
            throw new ValidationException("Validation Failed with " + processTotal + "Processed Files and " + totalFiles + " Total Files");
        }
    }

    public void setTotalFiles(int totalFiles) {
        this.totalFiles = this.totalFiles + totalFiles;
    }

    public void setNoOfFilesMoved(int noOfFilesMoved) {
        this.noOfFilesMoved = this.noOfFilesMoved + noOfFilesMoved;
    }

    public void setNoOfFilesSkipped(int noOfFilesSkipped) {
        this.noOfFilesSkipped = this.noOfFilesSkipped + noOfFilesSkipped;
    }

    public void setNoOfFilesAlreadyExist(int noOfFilesAlreadyExist) {
        this.noOfFilesAlreadyExist = this.noOfFilesAlreadyExist + noOfFilesAlreadyExist;
    }

    public void setNoOfInvalidExtensionFile(int noOfFilesInvalidExt) {
        this.noOfFilesInvalidExt = this.noOfFilesInvalidExt + noOfFilesInvalidExt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nProcessing Details for Folder : ");
        sb.append(sourceFolder).append("\n");
        sb.append("Number of Moved Files : ").append(noOfFilesMoved).append("\n");
        sb.append("Number of Skipped Files : ").append(noOfFilesSkipped).append("\n");
        sb.append("Number of Already Existing Files : ").append(noOfFilesAlreadyExist).append("\n");
        sb.append("Number of Invalid Extension Files : ").append(noOfFilesInvalidExt).append("\n");
        sb.append("Total Number of Files : ").append(totalFiles).append("\n");

        return sb.toString();
    }

    public String toStringWithTimeStamp() {
        final StringBuilder sb = new StringBuilder("\nProcessing Details for Folder : ");
        sb.append(sourceFolder).append('\n');
        sb.append("Number of Moved Files : ").append(noOfFilesMoved).append("\n");
        sb.append("Number of Skipped Files : ").append(noOfFilesSkipped).append("\n");
        sb.append("Number of Already Existing Files : ").append(noOfFilesAlreadyExist).append("\n");
        sb.append("Number of Invalid Extension Files : ").append(noOfFilesInvalidExt).append("\n");
        sb.append("Total Number of Files : ").append(totalFiles).append("\n");
        sb.append("Processing Completed At : ").append(LocalDateTime.now());

        return sb.toString();
    }


}
