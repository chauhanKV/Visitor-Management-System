package io.bootify.my_gate_visitor_management_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bootify.my_gate_visitor_management_project.domain.Address;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.Person;
import io.bootify.my_gate_visitor_management_project.model.AddressDTO;
import io.bootify.my_gate_visitor_management_project.model.UserDTO;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.repos.AddressRepository;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.PersonRepository;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
// @AutoConfigureMockMvc is the annotation used to create a bean of MockMVC. Spring will not create on itself.
@AutoConfigureMockMvc
// To ask this test class to use application properties from this folder's location
@TestPropertySource(
        locations = ("classpath:application-it.properties")
)
public class PersonAPITests {

    // SpringBootTest provides a class for making API call for integration test cases.
    // Using this we can make call to the controller.
    // Similar to API call with JSON.
    @Autowired
    private MockMvc mockMvc;
    private Person person;
    private Address address;

    private Flat flat;
    private UserDTO userDTO;

    // To check the state of the db after creating the user in TestCreateUser() function, we autowire PersonRepository
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private FlatRepository flatRepository;

    @BeforeEach
    public void setup()
    {
        // We will create actual API call here. Not the method of service layer.

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

        AddressDTO addressDTO = new AddressDTO();
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
    private void tearDown()
    {
        // This is required to clear the data after running the test cases.
        personRepository.deleteAll();
    }

    @Test
    public void testCreatePersonAPI() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData = objectMapper.writeValueAsString(userDTO); // This is used to create json.

        // First we grab the size of the table before making the API call
        Integer initialSize = personRepository.findAll().size();

        // Perform API call
        mockMvc.perform(post("/api/admin/createUser")
                .contentType("application/json")
                .content(jsonData))  // Once the API call is done , we print and expect the status to be isCreated.
                .andDo(print()).andExpect(status().isCreated());

        // Now we check the size of the db after creating the user
        Integer finalSize = personRepository.findAll().size();
        Integer result = finalSize-initialSize;
        assert(result).equals(1);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        flatRepository.save(flat);
        addressRepository.save(address);
        personRepository.save(person);
        mockMvc.perform(get("/api/admin/allUsers?pageSize=1&pageNumber=0"))
                .andDo(print()).andExpect(status().isOk());
    }
}
