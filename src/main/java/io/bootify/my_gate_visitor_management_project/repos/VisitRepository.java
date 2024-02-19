package io.bootify.my_gate_visitor_management_project.repos;

import io.bootify.my_gate_visitor_management_project.domain.Visit;
import io.bootify.my_gate_visitor_management_project.model.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByStatus(VisitStatus status);
}
