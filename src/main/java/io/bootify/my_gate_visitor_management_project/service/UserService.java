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
import io.bootify.my_gate_visitor_management_project.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final FlatRepository flatRepository;
    private final AddressRepository addressRepository;

    public UserService(final PersonRepository personRepository, final FlatRepository flatRepository,
                       final AddressRepository addressRepository) {
        this.personRepository = personRepository;
        this.flatRepository = flatRepository;
        this.addressRepository = addressRepository;
    }

    @Autowired
    private AddressService addressService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> findAll() {
        final List<Person> people = personRepository.findAll(Sort.by("id"));
        return people.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    @Transactional
    public List<UserDTO> findAllWithPagination(Pageable pageable) {
        final List<Person> people = personRepository.findAll(pageable).stream().toList();
        return people.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return personRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

//    @Transactional
//    public Long create(final UserDTO userDTO) {
//        final User user = new User();
//        mapToEntity(userDTO, user);
//        return userRepository.save(user).getId();
//    }

    @Transactional
    public String create(final UserDTO userDTO) {
        final Person person = new Person();
        mapToEntity(userDTO, person);
        person.setPassword(passwordEncoder.encode("test@123"));
        return personRepository.save(person).getName();
    }

    public void markInActive(Long userId)
    {
        Person person = personRepository.findById(userId).get();
        if(person == null)
        {
            throw new NotFoundException("User not found");
        }
        person.setStatus(UserStatus.INACTIVE);
        personRepository.save(person);
    }

    public void markActive(Long userId)
    {
        Person person = personRepository.findById(userId).get();
        if(person == null)
        {
            throw new NotFoundException("User not found");
        }
        person.setStatus(UserStatus.ACTIVE);
        personRepository.save(person);
    }

    public void update(final Long id, final UserDTO userDTO) {
        final Person person = personRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, person);
        personRepository.save(person);
    }

    public void delete(final Long id) {
        personRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final Person person, final UserDTO userDTO) {
        userDTO.setId(person.getId());
        userDTO.setName(person.getName());
        userDTO.setEmail(person.getEmail());
        userDTO.setPhone(person.getPhone());
        userDTO.setRole(person.getRole());
        userDTO.setStatus(person.getStatus());

        Flat flat = person.getFlat();
        if(flat != null)
        {
            userDTO.setFlatNumber(flat.getNumber());
        }

        AddressDTO addressDTO = new AddressDTO();
        addressService.mapToDTO(person.getAddress(), addressDTO);
        userDTO.setAddress(addressDTO);

        return userDTO;
    }


    private Person mapToEntity(final UserDTO userDTO, final Person person) {
        person.setName(userDTO.getName());
        person.setEmail(userDTO.getEmail());
        person.setPhone(userDTO.getPhone());
        person.setRole(userDTO.getRole());
        person.setStatus(userDTO.getStatus());
        Flat flat = flatRepository.findByNumber(userDTO.getFlatNumber());
        if(flat == null) {
            flat = new Flat();
            flat.setNumber(userDTO.getFlatNumber());
            flatRepository.save(flat);
        }
        person.setFlat(flat);
        final Address address = new Address();
        // Changed from line 149 to this because test case was throwing error for addressService being null.
        address.setLine1(userDTO.getAddress().getLine1());
        address.setLine2(userDTO.getAddress().getLine2());
        address.setPincode(userDTO.getAddress().getPincode());
        address.setCity(userDTO.getAddress().getCity());
        address.setCountry(userDTO.getAddress().getCountry());
        //addressService.mapToEntity(userDTO.getAddress(), address);
        person.setAddress(address);
        addressRepository.save(address);
        return person;
    }

    public boolean emailExists(final String email) {
        return personRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneExists(final String phone) {
        return personRepository.existsByPhoneIgnoreCase(phone);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = personRepository.findByEmail(username);
        if(userDetails == null)
        {
            throw new UsernameNotFoundException("User does not exist");
        }
        return userDetails;
    }
}
