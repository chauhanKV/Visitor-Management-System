package io.bootify.my_gate_visitor_management_project.controller;

import io.bootify.my_gate_visitor_management_project.model.VisitDTO;
import io.bootify.my_gate_visitor_management_project.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserPanelController {

    @Autowired
    private VisitService visitService;

    @PutMapping("/approveVisit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> approveVisit(@PathVariable Long visitId, @RequestHeader Long userId) throws BadRequestException {
        visitService.approveVisit(visitId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rejectVisit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> rejectVisit(@PathVariable Long visitId) {
        visitService.rejectVisit(visitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allPendingVisits")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<VisitDTO>> getAllPendingVisits() {
        visitService.findAllWaitingVisits();
        return ResponseEntity.ok().build();
    }
}
