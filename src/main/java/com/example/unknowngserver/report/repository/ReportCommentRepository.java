package com.example.unknowngserver.report.repository;

import com.example.unknowngserver.report.entity.Report;
import com.example.unknowngserver.report.entity.ReportComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportCommentRepository extends JpaRepository<ReportComment, Long> {

}
