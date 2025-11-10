package com.documentverification.controller;

import com.documentverification.model.Document;
import com.documentverification.repository.DocumentRepository;
import com.documentverification.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private DocumentRepository documentRepository;

    // ✅ Dashboard Statistics
    @GetMapping("/admin/stats")
    public String getAdminStats(Model model) {

        long totalDocs = adminService.getTotalDocuments();

        // Dummy chart data (optional)
        List<String> chartLabels = List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun");
        List<Integer> chartData = List.of(12, 19, 3, 5, 2, 3);

        model.addAttribute("totalDocs", totalDocs);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "statisics"; // your stats HTML page
    }

    // ✅ View all documents (main user-management page)
    @GetMapping("/admin/users")
    public String viewDocuments(Model model) {
        List<Document> users = documentRepository.findAll();
        model.addAttribute("users", users); // matches your Thymeleaf variable
        return "UserManagment"; // your single HTML page with modals
    }

    // ✅ Add new document (from modal)
    @PostMapping("/admin/add")
    public String addDocument(@RequestParam("fileName") String fileName,
            @RequestParam("uniqueKey") String uniqueKey) {

        // Example file path (update this to your actual upload path if needed)
        String filePath = "/uploads/" + fileName;

        Document document = new Document(fileName, filePath, uniqueKey);
        documentRepository.save(document);
        return "redirect:/admin/users";
    }

    // ✅ Update document (from edit modal)
    @PostMapping("/admin/update/{id}")
    public String updateDocument(@PathVariable Long id,
            @RequestParam("fileName") String fileName,
            @RequestParam("uniqueKey") String uniqueKey) {

        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isPresent()) {
            Document document = optionalDoc.get();
            document.setFileName(fileName);
            document.setUniqueKey(uniqueKey);
            documentRepository.save(document);
        }

        return "redirect:/admin/users";
    }

    // ✅ Delete document
    @GetMapping("/admin/delete/{id}")
    public String deleteDocument(@PathVariable Long id) {
        documentRepository.deleteById(id);
        return "redirect:/admin/users";
    }

 @GetMapping("/admin/search")
    public String searchDocuments(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String uniqueKey,
            @RequestParam(required = false) String fileHash,
            Model model
    ) {
        List<Document> results = adminService.advancedSearch(id, uniqueKey, fileHash);
        model.addAttribute("documents", results);
        return "AdvSearch"; // Your Thymeleaf page
    }

}
