package com.documentverification.repository;

import com.documentverification.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByUniqueKey(String uniqueKey);
    boolean existsByUniqueKey(String uniqueKey);

    // ✅ New: Advanced Search by optional fields (ID, Unique Key, File Hash)
    @Query("SELECT d FROM Document d WHERE " +
           "(:id IS NULL OR d.id = :id) AND " +
           "(:uniqueKey IS NULL OR LOWER(d.uniqueKey) LIKE LOWER(CONCAT('%', :uniqueKey, '%'))) AND " +
           "(:fileHash IS NULL OR LOWER(d.fileHash) LIKE LOWER(CONCAT('%', :fileHash, '%')))")
    List<Document> advancedSearch(
            @Param("id") Long id,
            @Param("uniqueKey") String uniqueKey,
            @Param("fileHash") String fileHash
    );

    // ✅ Optional: for direct hash-based lookup
    Optional<Document> findByFileHash(String fileHash);
}
