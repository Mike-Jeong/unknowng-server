package com.example.unknowngserver.report.type;

import com.example.unknowngserver.exception.ErrorCode;
import com.example.unknowngserver.exception.ReportException;
import com.example.unknowngserver.report.service.ReportContentService;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ContentType {
    ARTICLE, COMMENT;

    private ReportContentService reportContentService;

    public void addReportContentService(ReportContentService reportContentService) {
        this.reportContentService = reportContentService;
    }

    public static ReportContentService getReportContentService(String input) {
        return Arrays.stream(values())
                .filter(e -> e.toString().equals(input))
                .findFirst()
                .orElseThrow(() -> new ReportException(ErrorCode.REPORT_DETAIL_CONTENT_TYPE_NOT_SUPPORTED))
                .getReportContentService();
    }

}
