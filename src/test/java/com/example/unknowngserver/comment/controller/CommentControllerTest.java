package com.example.unknowngserver.comment.controller;

import com.example.unknowngserver.comment.dto.CommentDto;
import com.example.unknowngserver.comment.dto.DeleteCommentRequest;
import com.example.unknowngserver.comment.dto.SubmitCommentRequest;
import com.example.unknowngserver.comment.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@WebMvcTest(value = CommentController.class)
@WithMockUser
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 조회 API 컨트롤러 테스트 성공")
    void getComments() throws Exception {

        //given
        List<CommentDto> commentDtoList = new ArrayList<>();

        commentDtoList.add(CommentDto.builder()
                .id(1L)
                .content("test 1 comment content")
                .author("Min1")
                .registeredAt(LocalDateTime.now())
                .build());
        commentDtoList.add(CommentDto.builder()
                .id(2L)
                .content("test 2 comment content")
                .author("Min2")
                .registeredAt(LocalDateTime.now())
                .build());
        commentDtoList.add(CommentDto.builder()
                .id(3L)
                .content("test 3 comment content")
                .author("Min3")
                .registeredAt(LocalDateTime.now())
                .build());

        given(commentService.getComments(anyLong(), anyInt()))
                .willReturn(commentDtoList);

        //when
        //then
        mockMvc.perform(get("/articles/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].content").value("test 1 comment content"))
                .andExpect(jsonPath("$[0].author").value("Min1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].content").value("test 2 comment content"))
                .andExpect(jsonPath("$[1].author").value("Min2"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[2].content").value("test 3 comment content"))
                .andExpect(jsonPath("$[2].author").value("Min3"))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 등록 API 컨트롤러 테스트 성공")
    void submitComments() throws Exception {

        //given
        given(commentService.createComment(any()))
                .willReturn(true);

        //when
        //then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(
                                new SubmitCommentRequest(1L, "test", "test", "test")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 삭제 API 컨트롤러 테스트 성공")
    void deleteComment() throws Exception {

        //given
        given(commentService.deleteComment(any()))
                .willReturn(true);

        //when
        //then
        mockMvc.perform(delete("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(
                                new DeleteCommentRequest(1L, "1234")
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andDo(print());

    }
}