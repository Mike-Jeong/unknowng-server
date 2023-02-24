package com.example.unknowngserver.report.entity;

import com.example.unknowngserver.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
@DiscriminatorValue("COMMENT")
@SQLDelete(sql = "UPDATE report SET processed = true, processed_at = current_timestamp WHERE id = ? ")
public class ReportComment extends Report {
    @OneToOne(fetch = FetchType.LAZY)
    private Comment comment;
}
