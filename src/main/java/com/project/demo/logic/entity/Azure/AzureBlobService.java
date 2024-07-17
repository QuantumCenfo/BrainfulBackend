package com.project.demo.logic.entity.Azure;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;

@Component
public class AzureBlobService {
    @Autowired
    BlobServiceClient blobServiceClient;
    @Autowired
    BlobContainerClient blobContainerClient;

    public String upload(MultipartFile multipartFile) throws IOException{
        BlobClient blobClient = blobContainerClient.getBlobClient(multipartFile.getOriginalFilename());


                blobClient.upload(multipartFile.getInputStream(),multipartFile.getSize());
        return blobClient.getBlobUrl();
    }

    public byte[] getFile(String filename) throws URISyntaxException{
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        blobClient.download(byteArrayOutputStream);
        final byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;

    }

    public List<String> listBlobs(){
        PagedIterable<BlobItem> blobItems = blobContainerClient.listBlobs();
        List<String> names = new ArrayList<String>();
        for (BlobItem blobItem : blobItems) {
            names.add(blobItem.getName());
        }
        return names;
    }

    public Boolean delteBlob(String blobName){
        BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
        blobClient.delete();
        return true;
    }




}
