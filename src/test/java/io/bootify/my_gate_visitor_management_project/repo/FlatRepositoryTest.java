package io.bootify.my_gate_visitor_management_project.repo;

import io.bootify.my_gate_visitor_management_project.domain.Address;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.Person;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.repos.AddressRepository;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;

// We did not add any mockito dependency because this will directly interact with DB.
// Also using @DataJpaTest , its automatically tests the CRUD operations defined internally by JPARepository interface.
@DataJpaTest
public class FlatRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Flat flat;

    @BeforeEach()
    private void setup()
    {
        Person person = new Person();
        person.setName("Taylor");
        person.setEmail("taylor@yopmail.com");
        person.setPhone("9273273523");
        person.setRole("admin");
        person.setDateCreated(OffsetDateTime.now());
        person.setLastUpdated(OffsetDateTime.now());
        person.setStatus(UserStatus.ACTIVE);

        flat = new Flat();
        flat.setNumber("A-2234");
        flat.setDateCreated(OffsetDateTime.now());
        flat.setLastUpdated(OffsetDateTime.now());
        person.setFlat(flat);

        Address address = new Address();
        address.setLine1("Test");
        address.setLine2("Test 2");
        address.setPincode("454323");
        address.setCity("Mumbai");
        address.setCountry("India");
        address.setDateCreated(OffsetDateTime.now());
        address.setLastUpdated(OffsetDateTime.now());
        person.setAddress(address);

        addressRepository.save(address);
        flatRepository.save(flat);
        personRepository.save(person);
    }

    @AfterEach
    private void tearDown()
    {
        flatRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void testFindByNumber()
    {
        Person actualPerson = personRepository.findByEmail("taylor@yopmail.com");
        Flat actualFlat = flatRepository.findByNumber(actualPerson.getFlat().getNumber());
        String actualFlatNumber = actualFlat.getNumber();
        assert(actualFlatNumber).equals(flat.getNumber());
    }
}
