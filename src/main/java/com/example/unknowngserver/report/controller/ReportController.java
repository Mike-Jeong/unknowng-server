package com.example.unknowngserver.report.controller;

import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.common.dto.Response;
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
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping()
    public ResponseEntity<List<ReportBoardInfo>> getReports(@ModelAttribute PageNumber page) {

        List<ReportDto> reportDtoList = reportService.getReports(page);

        List<ReportBoardInfo> reportBoardInfoList = reportDtoList.stream()
                .map(ReportBoardInfo::fromReportDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reportBoardInfoList);
    }

    @PostMapping()
    public ResponseEntity<Response> submitReport(@RequestBody @Valid SubmitReportRequest submitReportRequest) {

        reportService.createReport(submitReportRequest);

        return ResponseEntity.ok().body(Response.ok());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailInfo> getReport(@PathVariable("id") Long id) {

        ReportDetailDto reportDetailDto = reportService.getReportDetail(id);

        ReportDetailInfo reportDetailInfo = ReportDetailInfo.fromReportDetailDto(reportDetailDto);

        return ResponseEntity.ok(reportDetailInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteReport(@PathVariable("id") Long id) {

        reportService.deleteReport(id);

        return ResponseEntity.ok().body(Response.ok());
    }

    @GetMapping("/{reportId}/report-records")
    public ResponseEntity<List<ReportRecordBoardInfo>> getReportRecords(@PathVariable("reportId") Long reportId,
                                                                        @ModelAttribute PageNumber page) {

        List<ReportRecordDto> reportRecordDtoList = reportService.getReportRecords(reportId, page);

        List<ReportRecordBoardInfo> reportRecordBoardInfoList = reportRecordDtoList.stream()
                .map(ReportRecordBoardInfo::fromReportRecordDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reportRecordBoardInfoList);
    }
}
