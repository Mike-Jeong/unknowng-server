package com.example.unknowngserver.report.controller;

import com.example.unknowngserver.report.dto.*;
import com.example.unknowngserver.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/reports")
    public ResponseEntity<List<ReportBoardInfo>> getReports(@RequestParam(defaultValue = "1", required = false) Integer page) {

        page = (page < 1) ? 1 : page;

        List<ReportDto> reportDtoList = reportService.getReports(page);

        return ResponseEntity.ok(reportDtoList.stream()
                .map(ReportBoardInfo::fromReportDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/reports")
    public ResponseEntity<Boolean> submitReport(
            @RequestBody @Valid SubmitReportRequest submitReportRequest) {
        return ResponseEntity.ok(reportService.createReport(submitReportRequest));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportDetailInfo> getReport(@PathVariable("id") Long id) {

        ReportDetailDto reportDetailDto = reportService.getReportDetail(id);

        return ResponseEntity.ok(ReportDetailInfo.fromReportDetailDto(reportDetailDto));
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Boolean> deleteReport(@PathVariable("id") Long id) {

        return ResponseEntity.ok(reportService.deleteReport(id));
    }

    @GetMapping("/reports/{reportId}/reportRecords")
    public ResponseEntity<List<ReportRecordBoardInfo>> getReportRecords(@PathVariable("reportId") Long reportId,
                                                                        @RequestParam(defaultValue = "1", required = false) Integer page) {
        List<ReportRecordDto> reportRecordDtoList = reportService.getReportRecords(reportId, page);

        return ResponseEntity.ok(reportRecordDtoList.stream()
                .map(ReportRecordBoardInfo::fromReportRecordDto)
                .collect(Collectors.toList()));
    }
}
