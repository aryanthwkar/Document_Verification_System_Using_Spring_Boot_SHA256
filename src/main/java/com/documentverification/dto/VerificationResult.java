package com.documentverification.dto;

import com.documentverification.model.Document;

public class VerificationResult {
    private boolean verified;
    private String message;
    private Document document;
    private String verificationKey;

    public VerificationResult() {}

    public VerificationResult(boolean verified, String message, Document document, String verificationKey) {
        this.verified = verified;
        this.message = message;
        this.document = document;
        this.verificationKey = verificationKey;
    }

    // Getters and Setters
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Document getDocument() { return document; }
    public void setDocument(Document document) { this.document = document; }

    public String getVerificationKey() { return verificationKey; }
    public void setVerificationKey(String verificationKey) { this.verificationKey = verificationKey; }
}