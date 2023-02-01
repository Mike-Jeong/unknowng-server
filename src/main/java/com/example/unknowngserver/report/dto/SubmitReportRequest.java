package com.example.unknowngserver.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class SubmitReportRequest {
    @NotNull
    private String contentType;
    @NotNull
    private Long contentId;
    @NotNull
    private String reportType;
    private String memo;
}
