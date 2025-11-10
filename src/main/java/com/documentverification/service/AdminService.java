package com.documentverification.service;

import com.documentverification.model.Document;
import com.documentverification.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private DocumentRepository documentRepository;

    // ✅ Total count
    public long getTotalDocuments() {
        return documentRepository.count();
    }

    // ✅ Advanced search
    public List<Document> advancedSearch(Long id, String uniqueKey, String fileHash) {
        return documentRepository.advancedSearch(id, uniqueKey, fileHash);
    }

    // ✅ Optional helper: find by file hash
    public Document getByFileHash(String fileHash) {
        return documentRepository.findByFileHash(fileHash).orElse(null);
    }
}
