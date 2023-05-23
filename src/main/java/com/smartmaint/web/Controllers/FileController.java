package com.smartmaint.web.Controllers;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.smartmaint.web.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/files")
    public String addFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileService.addFile(file);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String id) throws IOException {
        GridFSFile file = fileService.getFile(id);
        GridFsResource resource = new GridFsResource(file, fileService.getGridFsBucket().openDownloadStream(file.getObjectId()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(file.getMetadata().get("_contentType").toString()))
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
