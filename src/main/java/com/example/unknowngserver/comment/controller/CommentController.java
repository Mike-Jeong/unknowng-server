package com.example.unknowngserver.comment.controller;

import com.example.unknowngserver.comment.dto.CommentDto;
import com.example.unknowngserver.comment.dto.CommentInfo;
import com.example.unknowngserver.comment.dto.DeleteCommentRequest;
import com.example.unknowngserver.comment.dto.SubmitCommentRequest;
import com.example.unknowngserver.comment.service.CommentService;
import com.example.unknowngserver.common.dto.PageNumber;
import com.example.unknowngserver.common.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentInfo>> getComments(@PathVariable("articleId") Long articleId,
                                                         @ModelAttribute PageNumber page) {

        List<CommentDto> commentDtoList = commentService.getComments(articleId, page);

        List<CommentInfo> commentInfoList = commentDtoList.stream()
                .map(CommentInfo::fromCommentDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(commentInfoList);
    }

    @PostMapping("/comments")
    public ResponseEntity<Response> submitComments(@RequestBody @Valid SubmitCommentRequest submitCommentRequest) {

        commentService.createComment(submitCommentRequest);

        return ResponseEntity.ok().body(Response.ok());
    }

    @DeleteMapping("/comments")
    public ResponseEntity<Response> deleteComment(@RequestBody @Valid DeleteCommentRequest deleteCommentRequest) {

        commentService.deleteComment(deleteCommentRequest);

        return ResponseEntity.ok().body(Response.ok());
    }
}
