package com.example.unknowngserver.article.controller;

import com.example.unknowngserver.article.dto.ArticleDto;
import com.example.unknowngserver.article.dto.DeleteArticleRequest;
import com.example.unknowngserver.article.dto.SubmitArticleRequest;
import com.example.unknowngserver.article.service.ArticleService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ArticleController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WithMockUser
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    ArticleDto articleDto;

    @BeforeAll
    public void beforeAll() {

        articleDto = ArticleDto.builder()
                .id(1L)
                .title("test 1")
                .content("test 1 content")
                .author("Min1")
                .registeredAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("게시글 조회 API 컨트롤러 테스트 성공")
    void getArticle() throws Exception {

        //given
        given(articleService.getArticle(anyLong()))
                .willReturn(articleDto);

        //when
        //then
        mockMvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("test 1"))
                .andExpect(jsonPath("$.author").value("Min1"))
                .andExpect(jsonPath("$.content").value("test 1 content"))
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 리스트 조회 API 컨트롤러 테스트 성공")
    void getArticles() throws Exception {

        //given
        List<ArticleDto> articleDtoList = new ArrayList<>();

        ArticleDto articleDto2 = ArticleDto.builder()
                .id(2L)
                .title("test 2")
                .content("test 2 content")
                .author("Min2")
                .registeredAt(LocalDateTime.now())
                .build();

        articleDtoList.add(articleDto);
        articleDtoList.add(articleDto2);

        given(articleService.getArticles(any(), any()))
                .willReturn(articleDtoList);

        //when
        //then
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("test 1"))
                .andExpect(jsonPath("$[0].author").value("Min1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].title").value("test 2"))
                .andExpect(jsonPath("$[1].author").value("Min2"))
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 등록 API 컨트롤러 테스트 성공")
    void submitArticle() throws Exception {

        //given
        given(articleService.createArticle(any()))
                .willReturn(articleDto);

        //when
        //then
        mockMvc.perform(post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(
                                new SubmitArticleRequest("테스트", "테스트", "테스트", "1234")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value("1"))
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 삭제 API 컨트롤러 테스트 성공")
    void deleteArticle() throws Exception {

        //given
        //when
        //then
        mockMvc.perform(delete("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(
                                new DeleteArticleRequest(1L, "1234")
                        )))
                .andExpect(status().isOk())
                .andDo(print());

    }
}
