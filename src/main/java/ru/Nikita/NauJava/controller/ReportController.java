package ru.Nikita.NauJava.controller;

import ru.Nikita.NauJava.transaction.ReportService;
import ru.Nikita.NauJava.repository.ReportRepository;
import ru.Nikita.NauJava.entity.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    @Autowired
    public ReportController(ReportService reportService, ReportRepository reportRepository) {
        this.reportService = reportService;
        this.reportRepository = reportRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> createAndGenerateReport() {
        Long reportId = reportService.createReport();
        reportService.generateReport(reportId);
        Map<String, Object> response = new HashMap<>();
        response.put("reportId", reportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable Long reportId) {
        var report = reportRepository.findById(reportId);
        if (report.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Отчет не найден");
            return ResponseEntity.badRequest().body(response);
        }

        var reportEntity = report.get();

        if (reportEntity.getStatus() == ReportStatus.CREATED) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", reportEntity.getId());
            response.put("status", reportEntity.getStatus());
            response.put("message", "Отчет еще формируется");
            return ResponseEntity.ok(response);
        } else if (reportEntity.getStatus() == ReportStatus.ERROR) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", reportEntity.getId());
            response.put("status", reportEntity.getStatus());
            response.put("message", "Ошибка при формировании отчета");
            response.put("content", reportEntity.getContent());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")
                    .body(reportEntity.getContent());
        }
    }
}
