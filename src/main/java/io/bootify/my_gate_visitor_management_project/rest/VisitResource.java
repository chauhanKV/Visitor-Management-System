package io.bootify.my_gate_visitor_management_project.rest;

import io.bootify.my_gate_visitor_management_project.model.VisitDTO;
import io.bootify.my_gate_visitor_management_project.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@RestController
//@RequestMapping(value = "/api/visits", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisitResource {

    private final VisitService visitService;

    public VisitResource(final VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getAllVisits() {
        return ResponseEntity.ok(visitService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisit(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(visitService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) throws BadRequestException {
        final Long createdId = visitService.create(visitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVisit(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final VisitDTO visitDTO) throws BadRequestException {
        visitService.update(id, visitDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVisit(@PathVariable(name = "id") final Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
