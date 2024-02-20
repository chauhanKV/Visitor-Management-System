package io.bootify.my_gate_visitor_management_project.service;

import io.bootify.my_gate_visitor_management_project.domain.Address;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.Person;
import io.bootify.my_gate_visitor_management_project.model.AddressDTO;
import io.bootify.my_gate_visitor_management_project.model.UserDTO;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.repos.AddressRepository;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    // This interacts with Mockito framework to actually mock the service decorated with @Mock annotation
    private AutoCloseable autoCloseable;
    @Mock
    private PersonRepository personRepository;

    @Mock
    private FlatRepository flatRepository;

    @Mock
    private AddressRepository addressRepository;

    private UserService userService;
    private AddressService addressService;

    private UserDTO userDTO;
    private AddressDTO addressDTO;

    private Person person;
    private Address address;
    private Flat flat;

    @BeforeEach
    public void setup()
    {
        // Whatever service decorated with @Mock annotation will be mocked using below code.
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(personRepository,flatRepository, addressRepository);

        person = new Person();
        person.setName("Taylor");
        person.setEmail("taylor@yopmail.com");
        person.setPhone("9273273523");
        person.setRole("admin");
        person.setStatus(UserStatus.ACTIVE);

        flat = new Flat();
        flat.setNumber("A-2234");
        person.setFlat(flat);

        address = new Address();
        address.setLine1("Test");
        address.setLine2("Test 2");
        address.setPincode("454323");
        address.setCity("Mumbai");
        address.setCountry("India");
        person.setAddress(address);

        addressDTO = new AddressDTO();
        addressDTO.setLine1(address.getLine1());
        addressDTO.setLine2(address.getLine2());
        addressDTO.setPincode(address.getPincode());
        addressDTO.setCity(address.getCity());
        addressDTO.setCountry(address.getCountry());

        userDTO = new UserDTO();
        userDTO.setName(person.getName());
        userDTO.setEmail(person.getEmail());
        userDTO.setPhone(person.getPhone());
        userDTO.setRole(person.getRole());
        userDTO.setStatus(person.getStatus());
        userDTO.setFlatNumber(person.getFlat().getNumber());
        userDTO.setAddress(addressDTO);
    }

    @AfterEach
    public void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testCreateUser()
    {
        // While saving userRepository with any parameter we can pass our "user" object we created above.
        when(personRepository.save(any())).thenReturn(person);
        String actualUserName = userService.create(userDTO);
        assert(actualUserName).equals(person.getName());
    }
}
