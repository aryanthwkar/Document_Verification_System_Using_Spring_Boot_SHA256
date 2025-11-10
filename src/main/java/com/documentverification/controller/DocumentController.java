package com.documentverification.controller;

import com.documentverification.model.Document;
import com.documentverification.service.DocumentService;
import com.documentverification.dto.VerificationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public String uploadDocument(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/upload";
        }

        try {
            Document savedDocument = documentService.saveDocument(file);
            redirectAttributes.addFlashAttribute("success",
                    "Document uploaded successfully! Your verification key: " + savedDocument.getUniqueKey());
            redirectAttributes.addFlashAttribute("verificationKey", savedDocument.getUniqueKey());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to upload document: " + e.getMessage());
        }

        return "redirect:/upload";
    }

    @PostMapping("/verify")
    public String verifyDocument(@RequestParam("file") MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to verify.");
            return "redirect:/verify";
        }

        try {
            VerificationResult result = documentService.verifyDocument(file);
            model.addAttribute("verificationResult", result);
        } catch (Exception e) {
            VerificationResult result = new VerificationResult(false,
                    "Error during verification: " + e.getMessage(), null, null);
            model.addAttribute("verificationResult", result);
        }

        return "verify";
    }

    @GetMapping("/verify/{key}")
    public String verifyByKey(@PathVariable String key, Model model) {
        Optional<Document> document = documentService.getDocumentByKey(key.toUpperCase());

        if (document.isPresent()) {
            VerificationResult result = new VerificationResult(true,
                    "Document verified successfully", document.get(), key);
            model.addAttribute("verificationResult", result);
        } else {
            VerificationResult result = new VerificationResult(false,
                    "Invalid verification key", null, key);
            model.addAttribute("verificationResult", result);
        }

        return "verify";
    }

    @PostMapping("/admin/upload")
    public String adminUploadDocument(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        return uploadDocument(file, redirectAttributes);
    }

    @GetMapping("/admin/documents")
    public String viewAllDocuments(Model model) {
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "admin";
    }
}