package io.bootify.my_gate_visitor_management_project.service;

import io.bootify.my_gate_visitor_management_project.domain.Address;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.User;
import io.bootify.my_gate_visitor_management_project.model.AddressDTO;
import io.bootify.my_gate_visitor_management_project.model.UserDTO;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.repos.AddressRepository;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    // This interacts with Mockito framework to actually mocked which is decorated with @Mock annotation
    private AutoCloseable autoCloseable;
    @Mock
    private UserRepository userRepository;

    @Mock
    private FlatRepository flatRepository;

    @Mock
    private AddressRepository addressRepository;

    private UserService userService;
    private AddressService addressService;

    private UserDTO userDTO;
    private AddressDTO addressDTO;

    private User user;
    private Address address;
    private Flat flat;

    @BeforeEach
    public void setup()
    {
        // Whatever service decorated with @Mock annotation will be mocked using below code.
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository,flatRepository, addressRepository);

        user = new User();
        user.setName("Taylor");
        user.setEmail("taylor@yopmail.com");
        user.setPhone("9273273523");
        user.setRole("admin");
        user.setStatus(UserStatus.ACTIVE);

        flat = new Flat();
        flat.setNumber("A-2234");
        user.setFlat(flat);

        address = new Address();
        address.setLine1("Test");
        address.setLine2("Test 2");
        address.setPincode("454323");
        address.setCity("Mumbai");
        address.setCountry("India");
        user.setAddress(address);

        addressDTO = new AddressDTO();
        address.setLine1(address.getLine1());
        address.setLine2(address.getLine2());
        address.setPincode(address.getPincode());
        address.setCity(address.getCity());
        address.setCountry(address.getCountry());

        userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getStatus());
        userDTO.setFlatNumber(user.getFlat().getNumber());
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
        when(userRepository.save(any())).thenReturn(user);
        //when(addressRepository.save(any())).thenReturn(address);


        String actualUserName = userService.create(userDTO); // Need to check for error in create() function
        assert(actualUserName).equals(user.getName());

    }
}
