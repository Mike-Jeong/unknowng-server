package com.example.unknowngserver.report.controller;

import com.example.unknowngserver.report.dto.ReportDetailDto;
import com.example.unknowngserver.report.dto.ReportDto;
import com.example.unknowngserver.report.dto.ReportRecordDto;
import com.example.unknowngserver.report.dto.SubmitReportRequest;
import com.example.unknowngserver.report.service.ReportService;
import com.example.unknowngserver.report.type.ReportType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = ReportController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class ReportControllerTest {

    @MockBean
    private ReportService reportService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    ReportDetailDto reportDetailDto;

    List<ReportDto> reportDtoList = new ArrayList<>();
    List<ReportRecordDto> reportRecordDtoList = new ArrayList<>();

    @BeforeAll
    public void beforeAll() {

        reportDetailDto = ReportDetailDto.builder()
                .reportId(1L)
                .reportedContentType("ARTICLE")
                .targetId(1L)
                .targetContent("test")
                .reportedCount(1)
                .firstReportedAt(LocalDateTime.now())
                .build();

        reportDtoList.add(ReportDto.builder()
                .reportId(1L)
                .reportedContentType("ARTICLE")
                .firstReportedAt(LocalDateTime.now())
                .reportedCount(1)
                .build());
        reportDtoList.add(ReportDto.builder()
                .reportId(2L)
                .reportedContentType("COMMENT")
                .firstReportedAt(LocalDateTime.now())
                .reportedCount(2)
                .build());

        reportRecordDtoList.add(ReportRecordDto.builder()
                .id(1L)
                .reportType(ReportType.INSULT)
                .memo("test")
                .reportedDt(LocalDateTime.now())
                .build());
        reportRecordDtoList.add(ReportRecordDto.builder()
                .id(2L)
                .reportType(ReportType.SEXUAL)
                .memo("test")
                .reportedDt(LocalDateTime.now())
                .build());
    }

    @Test
    @DisplayName("신고 리스트 조회 API 컨트롤러 테스트 성공")
    void getReports() throws Exception {

        //given
        given(reportService.getReports(any()))
                .willReturn(reportDtoList);

        //when
        //then
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reportId").value("1"))
                .andExpect(jsonPath("$[0].reportedContentType").value("ARTICLE"))
                .andExpect(jsonPath("$[0].reportedCount").value("1"))
                .andExpect(jsonPath("$[1].reportId").value("2"))
                .andExpect(jsonPath("$[1].reportedContentType").value("COMMENT"))
                .andExpect(jsonPath("$[1].reportedCount").value("2"))
                .andDo(print());

    }

    @Test
    @DisplayName("신고 등록 API 컨트롤러 테스트 성공")
    void submitReport() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(post("/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(
                                new SubmitReportRequest("ARTICLE", 1L, "INSULT", "test")
                        )))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("신고 상세내역 조회 API 컨트롤러 테스트 성공")
    void getReport() throws Exception {

        //given
        given(reportService.getReportDetail(anyLong()))
                .willReturn(reportDetailDto);

        //when
        //then
        mockMvc.perform(get("/reports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value("1"))
                .andExpect(jsonPath("$.reportedContentType").value("ARTICLE"))
                .andExpect(jsonPath("$.targetId").value("1"))
                .andExpect(jsonPath("$.targetContent").value("test"))
                .andExpect(jsonPath("$.reportedCount").value("1"))
                .andDo(print());

    }

    @Test
    @DisplayName("신고 삭제 API 컨트롤러 테스트 성공")
    void deleteReport() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(delete("/reports/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("신고 기록 리스트 조회 API 컨트롤러 테스트 성공")
    void getReportRecords() throws Exception {

        //given
        given(reportService.getReportRecords(anyLong(), any()))
                .willReturn(reportRecordDtoList);

        //when
        //then
        mockMvc.perform(get("/reports/1/report-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reportType").value("INSULT"))
                .andExpect(jsonPath("$[0].memo").value("test"))
                .andExpect(jsonPath("$[1].reportType").value("SEXUAL"))
                .andExpect(jsonPath("$[1].memo").value("test"))
                .andDo(print());

    }
}
