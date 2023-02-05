package com.example.unknowngserver.comment.controller;

import com.example.unknowngserver.comment.dto.CommentDto;
import com.example.unknowngserver.comment.dto.CommentInfo;
import com.example.unknowngserver.comment.dto.DeleteCommentRequest;
import com.example.unknowngserver.comment.dto.SubmitCommentRequest;
import com.example.unknowngserver.comment.service.CommentService;
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
                                                         @RequestParam(defaultValue = "1", required = false) Integer page) {

        page = (page < 1) ? 1 : page;

        List<CommentDto> commentDtoList = commentService.getComments(articleId, page);

        return ResponseEntity.ok(commentDtoList.stream()
                .map(CommentInfo::fromCommentDto)
                .collect(Collectors.toList()));
    }

    @PostMapping("/comments")
    public ResponseEntity<Boolean> submitComments(
            @RequestBody @Valid SubmitCommentRequest submitCommentRequest) {

        return ResponseEntity.ok(commentService.createComment(submitCommentRequest));
    }

    @DeleteMapping("/comments")
    public ResponseEntity<Boolean> deleteComment(
            @RequestBody @Valid DeleteCommentRequest deleteCommentRequest) {

        return ResponseEntity.ok(commentService.deleteComment(deleteCommentRequest));
    }
}
