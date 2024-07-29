package com.project.demo.logic.entity.Azure;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Indicates that this class contains Spring configuration
public class AzureBlobConfig {

    @Value("${azure.storage.connection-string}") // Injects the value of 'azure.storage.connection-string' from the properties file
    private String connectionString;

    @Value("${azure.storage.name}") // Injects the value of 'azure.storage.name' from the properties file
    private String containerName;

    @Bean // Indicates that this method returns a Spring bean to be managed by the Spring container
    public BlobServiceClient blobServiceClient() {
        // Creates a BlobServiceClient using the connection string
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
        return blobServiceClient; // Returns the BlobServiceClient bean
    }

    @Bean // Indicates that this method returns a Spring bean to be managed by the Spring container
    public BlobContainerClient blobContainerClient() {
        // Creates a BlobContainerClient using the BlobServiceClient and the container name
        BlobContainerClient blobContainerClient = blobServiceClient()
                .getBlobContainerClient(containerName);
        return blobContainerClient; // Returns the BlobContainerClient bean
    }
}

