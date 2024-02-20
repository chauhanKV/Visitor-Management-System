package io.bootify.my_gate_visitor_management_project.repos;

import io.bootify.my_gate_visitor_management_project.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneIgnoreCase(String phone);

}
