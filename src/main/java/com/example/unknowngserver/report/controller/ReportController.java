package com.example.unknowngserver.report.controller;

import com.example.unknowngserver.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public void getReports() {
        //reportService.getReports();
    }

    @PostMapping("/reports")
    public void submitReport() {
        //reportService.createReport();
    }

    @GetMapping("/reports/{id}")
    public void getReport(@PathVariable("id") Long id) {
        //reportService.getReport(id);
    }

    @DeleteMapping("/reports/{id}")
    public void deleteReport(@PathVariable("id") Long id) {
        //reportService.deleteReport(id);
    }

    @GetMapping("/reports/{reportId}/reportDetails")
    public void getReportRecords(@PathVariable("reportId") Long reportId,
                                 @RequestParam(defaultValue = "10", required = false) Integer page) {
        //reportService.getReportDetails(reportId);
    }
}
