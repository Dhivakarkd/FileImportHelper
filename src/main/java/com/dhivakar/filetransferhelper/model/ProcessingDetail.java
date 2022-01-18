package com.dhivakar.filetransferhelper.model;

import com.dhivakar.filetransferhelper.exception.ValidationException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProcessingDetail {

    private String source_folder;
    private int total_files;
    private int no_of_files_moved;
    private int no_of_files_skipped;
    private int no_of_files_already_exist;
    private int no_of_files_invalid_ext;

    public ProcessingDetail(String source_folder,int total_files) {
        this.source_folder = source_folder;
        this.total_files = total_files;
        this.no_of_files_moved = 0;
        this.no_of_files_skipped = 0;
        this.no_of_files_already_exist = 0;
        this.no_of_files_invalid_ext = 0;
    }

    public void incrementMovedFileCounter(){
        this.no_of_files_moved++;
    }

    public void incrementSkippedFileCounter() {
        this.no_of_files_skipped++;
    }

    public void incrementAlreadyExistingFileCounter() {
        this.no_of_files_already_exist++;
    }

    public void incrementInvalidExtensionFileCounter() {
        this.no_of_files_invalid_ext++;
    }


    public void validateTotalCount() throws ValidationException {

        int process_total = no_of_files_moved + no_of_files_skipped + no_of_files_already_exist + no_of_files_invalid_ext;

        if (process_total != total_files) {
            throw new ValidationException("Validation Failed with " + process_total + "Processed Files and " + total_files + " Total Files");
        }
    }

    public void setTotalFiles(int total_files) {
        this.total_files = this.total_files + total_files;
    }

    public void setNoOfFilesMoved(int no_of_files_moved) {
        this.no_of_files_moved = this.no_of_files_moved + no_of_files_moved;
    }

    public void setNoOfFilesSkipped(int no_of_files_skipped) {
        this.no_of_files_skipped = this.no_of_files_skipped + no_of_files_skipped;
    }

    public void setNoOfFilesAlreadyExist(int no_of_files_already_exist) {
        this.no_of_files_already_exist = this.no_of_files_already_exist + no_of_files_already_exist;
    }

    public void setNoOfInvalidExtensionFile(int no_of_files_invalid_ext) {
        this.no_of_files_invalid_ext = this.no_of_files_invalid_ext + no_of_files_invalid_ext;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nProcessing Details for Folder : ");
        sb.append(source_folder).append("\n");
        sb.append("Number of Moved Files : ").append(no_of_files_moved).append("\n");
        sb.append("Number of Skipped Files : ").append(no_of_files_skipped).append("\n");
        sb.append("Number of Already Existing Files : ").append(no_of_files_already_exist).append("\n");
        sb.append("Number of Invalid Extension Files : ").append(no_of_files_invalid_ext).append("\n");
        sb.append("Total Number of Files : ").append(total_files).append("\n");

        return sb.toString();
    }

    public String toStringWithTimeStamp() {
        final StringBuilder sb = new StringBuilder("\nProcessing Details for Folder : ");
        sb.append(source_folder).append('\n');
        sb.append("Number of Moved Files : ").append(no_of_files_moved).append("\n");
        sb.append("Number of Skipped Files : ").append(no_of_files_skipped).append("\n");
        sb.append("Number of Already Existing Files : ").append(no_of_files_already_exist).append("\n");
        sb.append("Number of Invalid Extension Files : ").append(no_of_files_invalid_ext).append("\n");
        sb.append("Total Number of Files : ").append(total_files).append("\n");
        sb.append("Processing Completed At : ").append(LocalDateTime.now());

        return sb.toString();
    }


}
