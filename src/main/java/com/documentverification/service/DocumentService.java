package com.documentverification.service;

import com.documentverification.model.Document;
import com.documentverification.repository.DocumentRepository;
import com.documentverification.dto.VerificationResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ✅ Generate unique document key
    public String generateUniqueKey() {
        String uniqueKey;
        do {
            uniqueKey = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (documentRepository.existsByUniqueKey(uniqueKey));
        return uniqueKey;
    }

    // ✅ SHA-256 hash generator
    private String generateFileHash(MultipartFile file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = file.getBytes();
        byte[] hashBytes = digest.digest(fileBytes);

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // ✅ Save document + compute hash + store in DB
    public Document saveDocument(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String uniqueKey = generateUniqueKey();
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ✅ Generate SHA-256 hash
        String hashValue;
        try {
            hashValue = generateFileHash(file);
        } catch (Exception e) {
            throw new RuntimeException("Error generating file hash", e);
        }

        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(filePath.toString());
        document.setUniqueKey(uniqueKey);
        document.setUploadTime(LocalDateTime.now());
        document.setFileHash(hashValue); // ✅ Save hash in DB

        return documentRepository.save(document);
    }

    // ✅ Verify document by comparing SHA-256 hashes
    public VerificationResult verifyDocument(MultipartFile file) {
        try {
            // Hash of uploaded file
            String uploadedFileHash = generateFileHash(file);

            List<Document> allDocuments = documentRepository.findAll();

            for (Document doc : allDocuments) {
                if (uploadedFileHash.equals(doc.getFileHash())) {
                    return new VerificationResult(
                        true,
                        "Document verified successfully!",
                        doc,
                        doc.getUniqueKey()
                    );
                }
            }

            return new VerificationResult(false, "Document NOT found in system", null, null);

        } catch (Exception e) {
            return new VerificationResult(false, 
                "Verification error: " + e.getMessage(), null, null);
        }
    }

    // ✅ Get document by unique key
    public Optional<Document> getDocumentByKey(String uniqueKey) {
        return documentRepository.findByUniqueKey(uniqueKey);
    }

    // ✅ Get all documents
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
}
