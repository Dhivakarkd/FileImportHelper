package com.dhivakar.filehelperapi.controller;

import com.dhivakar.filehelperapi.model.FileContext;
import com.dhivakar.filehelperapi.model.ProcessingDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

@RestController
@Slf4j
public class FileController {


    @GetMapping(value = "/test")
    public String greeting() {
        return "hello world";
    }

    @PostMapping(value = "/file")
    public ProcessingDetail processImage(@RequestBody FileContext fileContext) throws IOException {
        fileContext.printInfo();
        ProcessingDetail processingDetail = new ProcessingDetail();
        String sourceDir = fileContext.getSource();
        String targetDir = fileContext.getTarget();
        String[] extensions = fileContext.getValidExtensions().split(",");
        LocalDate thresholdDate = LocalDate.now().minusMonths(fileContext.getUpto()); //change this to modify threshold
        HashSet<String> copiedFiles = fileSet(targetDir);
        Path sourcePath = Paths.get(sourceDir);
        processingDetail.setTotalFiles(getTotalFilesCount(sourcePath));
        try (Stream<Path> stream = Files.walk(sourcePath)) {

            stream.filter(Files::isRegularFile)
                    .filter(path -> Arrays.stream(extensions).anyMatch(ext -> path.toString().toLowerCase().endsWith(ext.trim().toLowerCase())))
                    .filter(path -> {
                        String fileName = path.getFileName().toString();
                        if (copiedFiles.contains(fileName)) { //exclude already copied files
                            processingDetail.incrementSkippedFileCounter();
                            return false;
                        }
                        return true;
                    })
                    .filter(path -> {
                        LocalDate fileDate = Instant.ofEpochMilli(path.toFile().lastModified())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                        return fileDate.isAfter(thresholdDate) || fileDate.equals(thresholdDate);
                    })
                    .forEach(path -> {
                        try {
                            Files.copy(path, Paths.get(targetDir, path.getFileName().toString()), StandardCopyOption.COPY_ATTRIBUTES);
                            processingDetail.incrementMovedFileCounter();
                            copiedFiles.add(path.getFileName().toString());
                        } catch (FileAlreadyExistsException e) {
                            processingDetail.incrementAlreadyExistingFileCounter();
                        } catch (IOException e) {
                            processingDetail.incrementFailedFileCounter();
                        }
                    });
        }
        processingDetail.updateExtensionCounter();
        return processingDetail;
    }

    private HashSet<String> fileSet(String path) throws IOException {
        HashSet<String> copiedFiles = new HashSet<>();
        Path start = Paths.get(path);
        try (Stream<Path> stream = Files.walk(start)) {
            stream.filter(Files::isRegularFile)
                    .forEach(e -> copiedFiles.add(e.getFileName().toString()));
        }
        return copiedFiles;
    }

    public long getTotalFilesCount(Path directory) throws IOException {
        return Files.walk(directory)
                .filter(Files::isRegularFile)
                .count();
    }
}

