package io.bootify.my_gate_visitor_management_project.rest;

import io.bootify.my_gate_visitor_management_project.model.VisitorDTO;
import io.bootify.my_gate_visitor_management_project.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@RestController
//@RequestMapping(value = "/api/visitors", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitorResource {

    private final VisitorService visitorService;

    public VisitorResource(final VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping
    public ResponseEntity<List<VisitorDTO>> getAllVisitors() {
        return ResponseEntity.ok(visitorService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorDTO> getVisitor(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(visitorService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final VisitorDTO visitorDTO) {
        final Long createdId = visitorService.create(visitorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVisitor(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final VisitorDTO visitorDTO) {
        visitorService.update(id, visitorDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVisitor(@PathVariable(name = "id") final Long id) {
        visitorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
