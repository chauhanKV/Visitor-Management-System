package io.bootify.my_gate_visitor_management_project.repos;

import io.bootify.my_gate_visitor_management_project.domain.Flat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlatRepository extends JpaRepository<Flat, Long> {
    Flat findByNumber(String number);
}
