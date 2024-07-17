package com.project.demo.rest.Azure;


import com.project.demo.logic.entity.Azure.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class AzureController {

    @Autowired
    private AzureBlobService azureBlobAdapter;

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = azureBlobAdapter.upload(file);
        return ResponseEntity.ok(fileName);

    }

    @GetMapping
    public ResponseEntity<List<String>> getAllFiles() throws IOException {
        List<String> items = azureBlobAdapter.listBlobs();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws URISyntaxException {
        ByteArrayResource resource = new ByteArrayResource(azureBlobAdapter.getFile(fileName));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + fileName+ "\"");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).headers(headers).body(resource);

    }

    @GetMapping("/{fileName}")
    public ByteArrayResource File(@PathVariable String fileName) throws URISyntaxException {
        ByteArrayResource resource = new ByteArrayResource(azureBlobAdapter.getFile(fileName));
        return resource;
    }

}
