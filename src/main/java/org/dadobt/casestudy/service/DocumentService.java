package org.dadobt.casestudy.service;

import org.dadobt.casestudy.config.DocumentStorageProperty;
import org.dadobt.casestudy.models.Invoice;
import org.dadobt.casestudy.models.Document;
import org.dadobt.casestudy.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class DocumentService{

    @Autowired
    private DocumentRepository documentRepository;

    private final Path docStorageLocation;

    @Autowired
    public DocumentService(DocumentStorageProperty documentStorageProperty) throws IOException {
        this.docStorageLocation = Paths.get(documentStorageProperty.getUploadDirectory()).toAbsolutePath().normalize();
        Files.createDirectories(this.docStorageLocation);
    }

    @Transactional
    public void create(MultipartFile multipartFile, Invoice invoice) throws IOException {
        Document document = new Document();
        document.setName(multipartFile.getOriginalFilename());
        document.setMimeType(multipartFile.getContentType());
        document.setSize(multipartFile.getSize());
        document.setInvoice(invoice);
        //should implement proper hash,but this is good enough
        //for example if a factura.jpg or factura.pdf is uploaded , it will create UUID.jpg or pdf
        document.setHash(String.valueOf(UUID.randomUUID()).concat(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."))));
        storeDocument(multipartFile, document.getHash());
        documentRepository.save(document);
    }

    private void storeDocument(MultipartFile file, String hash) throws IOException {
        Path targetLocation = this.docStorageLocation.resolve(hash);
        Files.copy(file.getInputStream(), targetLocation);
    }
}