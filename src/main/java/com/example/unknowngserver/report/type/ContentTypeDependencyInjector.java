package com.example.unknowngserver.report.type;

import com.example.unknowngserver.report.service.ReportArticleService;
import com.example.unknowngserver.report.service.ReportCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class ContentTypeDependencyInjector {

    private final ReportArticleService reportArticleService;
    private final ReportCommentService reportCommentService;

    @PostConstruct
    public void inject() {
        for (ContentType type : ContentType.values()) {

            if (type.equals(ContentType.ARTICLE)) {
                type.addReportContentService(reportArticleService);
            }

            if (type.equals(ContentType.COMMENT)) {
                type.addReportContentService(reportCommentService);
            }
        }
    }
}
