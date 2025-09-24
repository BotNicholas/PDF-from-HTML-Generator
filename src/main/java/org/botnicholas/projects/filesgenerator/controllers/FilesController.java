package org.botnicholas.projects.filesgenerator.controllers;

import org.botnicholas.projects.filesgenerator.services.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/files")
public class FilesController {
    @Autowired
    private FilesService filesService;

    @GetMapping("/{fileName}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileName) throws IOException {
        var file = filesService.getFile(fileName);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.parseMediaType(Files.probeContentType(file.getFile().toPath()))).body(file);
//        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_PDF).body(file);

//      Content-Disposition tells browser what to do with provided file
//      inline - will preview the file, while attachment will download it

//      filename is Content-Disposition header's parameter that will provide name the provided file
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=generated.pdf")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.pdf")
                .body(file);
    }
}
