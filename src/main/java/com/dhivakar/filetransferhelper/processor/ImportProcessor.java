package com.dhivakar.filetransferhelper.processor;

import com.dhivakar.filetransferhelper.exception.ValidationException;
import com.dhivakar.filetransferhelper.model.ImportDetail;
import com.dhivakar.filetransferhelper.model.ProcessingDetail;
import com.dhivakar.filetransferhelper.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
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
    String sourceFolderPath;
    @Value("${import.target.name}")
    String targetMainFolder;


    @Override
    public String process(ImportDetail item) throws IOException, ValidationException {

        String[] source = sourceFolderPath.split(",");

        ProcessingDetail finalDetail = new ProcessingDetail("All Folders", 0);
        log.info("Last Imported Date is {}", item.getDate());

        for (String source_path : source) {

            String sourceFolder = StringUtils.substringAfterLast(source_path, "/");
            File folder = new File(source_path);

            String targetFolder = targetMainFolder + sourceFolder + File.separator;


            File[] listOfFiles = folder.listFiles(File::isFile);

            ProcessingDetail processingDetail;

            if (listOfFiles != null && listOfFiles.length > 0) {
                Files.createDirectories(Paths.get(targetFolder));
                processingDetail = startProcessingFiles(item, sourceFolder, targetFolder, listOfFiles);
                processingDetail.validateTotalCount();

                updateFinalDetailRecord(finalDetail, processingDetail);

            } else {
                log.info("No Data Available in Folder to Import");
                processingDetail = new ProcessingDetail(sourceFolder, 0);
                log.debug(processingDetail.toString());

            }
        }

        log.info(finalDetail.toStringWithTimeStamp());
        return finalDetail.toStringWithTimeStamp();
    }

    private void updateFinalDetailRecord(ProcessingDetail finalDetail, ProcessingDetail processingDetail) {
        finalDetail.setTotalFiles(processingDetail.getTotalFiles());
        finalDetail.setNoOfFilesAlreadyExist(processingDetail.getNoOfFilesAlreadyExist());
        finalDetail.setNoOfFilesSkipped(processingDetail.getNoOfFilesSkipped());
        finalDetail.setNoOfFilesMoved(processingDetail.getNoOfFilesMoved());
        finalDetail.setNoOfInvalidExtensionFile(processingDetail.getNoOfFilesInvalidExt());
    }

    private ProcessingDetail startProcessingFiles(ImportDetail item, String sourceFolder, String targetFolder, File[] listOfFiles) throws IOException {
        log.info("Started Importing Folder \"{}\" with {} elements", sourceFolder, listOfFiles.length);

        ProcessingDetail processingDetail = new ProcessingDetail(sourceFolder, listOfFiles.length);


        LocalDate lastImportedDate = LocalDate.parse(item.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        // TO include the file which may be created after the imported data on the same
        lastImportedDate = lastImportedDate.minusDays(1);

        for (File file : listOfFiles) {

            if (file.isFile()) {
                importFilesToTarget(targetFolder, processingDetail, lastImportedDate, file);
            }

        }
        log.info(processingDetail.toString());
        return processingDetail;
    }

    private void importFilesToTarget(String targetFolder, ProcessingDetail processingDetail, LocalDate lastImportedDate, File file) throws IOException {

        String fileExt = FilenameUtils.getExtension(file.getName());

        if (FileUtil.validateExtension(fileExt)) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());

            LocalDate localDate = dateTime.toLocalDate();

            if (localDate.isAfter(lastImportedDate)) {

                Path path = Paths.get(targetFolder + file.getName());
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
        } else {
            processingDetail.incrementInvalidExtensionFileCounter();
        }
    }
}
