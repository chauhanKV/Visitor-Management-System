package io.bootify.my_gate_visitor_management_project.repo;

import io.bootify.my_gate_visitor_management_project.domain.Address;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.User;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.repos.AddressRepository;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@Configuration
@ContextConfiguration(classes =  FlatRepository.class)
public class FlatRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Flat flat;

    @BeforeEach()
    private void setup()
    {
        User user = new User();
        user.setName("Taylor");
        user.setEmail("taylor@yopmail.com");
        user.setPhone("9273273523");
        user.setRole("admin");
        user.setStatus(UserStatus.ACTIVE);

        flat = new Flat();
        flat.setNumber("A-2234");
        user.setFlat(flat);

        Address address = new Address();
        address.setLine1("Test");
        address.setLine2("Test 2");
        address.setPincode("454323");
        address.setCity("Mumbai");
        address.setCountry("India");
        user.setAddress(address);

        addressRepository.save(address);
        flatRepository.save(flat);
        userRepository.save(user);
    }

    @AfterEach
    private void tearDown()
    {

    }

//    @Test
//    void testFindByNumber()
//    {
//        User actualUser = userRepository.findByEmail("taylor@yopmail.com");
//        Flat actualFlat = flatRepository.findByNumber(actualUser.getFlat().getNumber());
//        String actualFlatNumber = actualFlat.getNumber();
//        assert(actualFlatNumber).equals(flat.getNumber());
//    }
}
