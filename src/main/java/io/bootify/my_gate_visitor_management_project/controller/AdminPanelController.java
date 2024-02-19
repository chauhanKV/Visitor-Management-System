package io.bootify.my_gate_visitor_management_project.controller;

import io.bootify.my_gate_visitor_management_project.model.AddressDTO;
import io.bootify.my_gate_visitor_management_project.model.UserDTO;
import io.bootify.my_gate_visitor_management_project.model.UserStatus;
import io.bootify.my_gate_visitor_management_project.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPanelController {

    Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final String createdUserName = userService.create(userDTO);
        return new ResponseEntity<>(createdUserName, HttpStatus.CREATED);
    }

//    @PostMapping("/createUser")
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
//        final Long createdId = userService.create(userDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }

    @PutMapping("/markUserInactive/{userId}")
    public ResponseEntity<Void> markUserInActive(@PathVariable Long userId)
    {
        userService.markInActive(userId);
        return ResponseEntity.ok().build();
        // ok() returns data of type builderbody.
        // build() is the function that actually builds the object and returns in responseentity format hence build() method is used.
    }

    @PutMapping("/markUserActive/{userId}")
    public ResponseEntity<Void> markUserActive(@PathVariable Long userId)
    {
        userService.markActive(userId);
        return ResponseEntity.ok().build();
        // ok() returns data of type builderbody.
        // build() is the function that actually builds the object and returns in responseEntity format hence build() method is used.
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam Integer pageSize, @RequestParam Integer pageNumber)
    {
        // Pagination not working in sorted manner.
        // PageNumber = 0 is not working
//        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);
//        return ResponseEntity.ok(userService.findAllWithPagination(pageable));

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("id")); // or another field for sorting
        return ResponseEntity.ok(userService.findAllWithPagination(pageRequest));
    }

    @PostMapping(consumes = "multipart/form-data", value = "/user-csv-upload")
    public ResponseEntity<List<String>> uploadFileForUserCreation(@RequestParam("file") MultipartFile file) {
        List<String> response = new ArrayList<>();
        LOGGER.info("File available : {}", file.getName());

        // Write a code to check the format or other validations

        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for(CSVRecord csvRecord : csvRecords) {
                UserDTO userDTO = new UserDTO();
                userDTO.setName(csvRecord.get("name"));
                userDTO.setEmail(csvRecord.get("email"));
                userDTO.setPhone(csvRecord.get("phone"));
                userDTO.setFlatNumber(csvRecord.get("flatNumber"));
                userDTO.setRole(csvRecord.get("role"));
                userDTO.setStatus(UserStatus.ACTIVE);

                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setLine1(csvRecord.get("line1"));
                addressDTO.setLine2(csvRecord.get("line2"));
                addressDTO.setPincode(csvRecord.get("pincode"));
                addressDTO.setCity(csvRecord.get("city"));
                addressDTO.setCountry(csvRecord.get("country"));

                userDTO.setAddress(addressDTO);

                try {
                    //Long id = userService.create(userDTO);
                    String name = userService.create(userDTO);
                    //response.add("Created user : " + userDTO.getName() + " with id " + id);
                    response.add("Created user : " + name);
                } catch (Exception ex) {
                    response.add("Unable to create user : " + userDTO.getName() + " msg : " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(response);
    }
}
