package com.documentverification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "unique_key", unique = true, nullable = false)
    private String uniqueKey;

    @Column(name = "upload_time")
    private LocalDateTime uploadTime;

    // ✅ NEW FIELD FOR SHA-256 HASH
    @Column(name = "file_hash", length = 1000)
    private String fileHash;

    // Constructors
    public Document() {}

    public Document(String fileName, String filePath, String uniqueKey) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.uniqueKey = uniqueKey;
        this.uploadTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getUniqueKey() { return uniqueKey; }
    public void setUniqueKey(String uniqueKey) { this.uniqueKey = uniqueKey; }

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }

    public String getFileHash() { return fileHash; }
    public void setFileHash(String fileHash) { this.fileHash = fileHash; }
}
