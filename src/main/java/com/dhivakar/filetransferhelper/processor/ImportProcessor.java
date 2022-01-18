package com.dhivakar.filetransferhelper.processor;

import com.dhivakar.filetransferhelper.exception.ValidationException;
import com.dhivakar.filetransferhelper.model.ImportDetail;
import com.dhivakar.filetransferhelper.model.ProcessingDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ImportProcessor implements ItemProcessor<ImportDetail, String> {

    @Value("${import.source.name}")
    String source_folder_path;
    @Value("${import.target.name}")
    String target_main_folder;


    @Override
    public String process(ImportDetail item) throws IOException, ValidationException {

        String[] source = source_folder_path.split(",");

        ProcessingDetail finalDetail = new ProcessingDetail("All Folders", 0);
        log.info("Last Imported Date is {}", item.getDate());

        for (String source_path : source) {

            String source_folder = StringUtils.substringAfterLast(source_path, "/");
            File folder = new File(source_path);

            String target_folder = target_main_folder + source_folder + File.separator;


            File[] listOfFiles = folder.listFiles(File::isFile);

            ProcessingDetail processingDetail;

            if (listOfFiles != null && listOfFiles.length > 0) {
                Files.createDirectories(Paths.get(target_folder));
                System.out.println("No of files " + listOfFiles.length);
                processingDetail = startProcessingFiles(item, source_folder, target_folder, listOfFiles);
                processingDetail.validateTotalCount();

                updateFinalDetailRecord(finalDetail, processingDetail);

            } else {
                log.info("No Data Available in Folder to Import");
                processingDetail = new ProcessingDetail(source_folder, 0);
                log.debug(processingDetail.toString());

            }
        }

        log.info(finalDetail.toString());
        return finalDetail.toString();
    }

    private void updateFinalDetailRecord(ProcessingDetail finalDetail, ProcessingDetail processingDetail) {
        finalDetail.setTotalFiles(processingDetail.getTotal_files());
        finalDetail.setNoOfFilesAlreadyExist(processingDetail.getNo_of_files_already_exist());
        finalDetail.setNoOfFilesSkipped(processingDetail.getNo_of_files_skipped());
        finalDetail.setNoOfFilesMoved(processingDetail.getNo_of_files_moved());
    }

    private ProcessingDetail startProcessingFiles(ImportDetail item, String source_folder, String target_folder, File[] listOfFiles) throws IOException {
        log.info("Started Importing Folder \"{}\" with {} elements", source_folder, listOfFiles.length);

        ProcessingDetail processingDetail = new ProcessingDetail(source_folder, listOfFiles.length);


        LocalDate lastImportedDate = LocalDate.parse(item.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);


        for (File file : listOfFiles) {

            if (file.isFile()) {
                importFilesToTarget(target_folder, processingDetail, lastImportedDate, file);
            }

        }
        log.info(processingDetail.toString());
        return processingDetail;
    }

    private void importFilesToTarget(String target_folder, ProcessingDetail processingDetail, LocalDate lastImportedDate, File file) throws IOException {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());

        LocalDate localDate = dateTime.toLocalDate();

        if (localDate.isAfter(lastImportedDate)) {

            Path path = Paths.get(target_folder + file.getName());
            try {
                Files.copy(file.toPath(), path, StandardCopyOption.COPY_ATTRIBUTES);
                processingDetail.incrementMovedFileCounter();
            } catch (FileAlreadyExistsException e) {
                log.error("File : {} Already Exist ", file.getName());
                processingDetail.incrementAlreadyExistingFileCounter();
            }
        } else {
            processingDetail.incrementSkippedFileCounter();
        }
    }
}
