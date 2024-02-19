package io.bootify.my_gate_visitor_management_project.controller;

import io.bootify.my_gate_visitor_management_project.model.VisitDTO;
import io.bootify.my_gate_visitor_management_project.model.VisitorDTO;
import io.bootify.my_gate_visitor_management_project.service.VisitService;
import io.bootify.my_gate_visitor_management_project.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/gatekeeper")
public class GateKeeperPanelController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitService visitService;

    @PostMapping("/createVisitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final VisitorDTO visitorDTO) {
        final Long createdId = visitorService.create(visitorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/createVisit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) throws BadRequestException {
        final Long createdId = visitService.create(visitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/markEntry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> markEntry(@PathVariable Long visitId) {
        visitService.updateInTime(visitId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/markExit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> markExit(@PathVariable Long visitId) {
        visitService.updateOutTime(visitId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = "multipart/form-data", value = "/upload-photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file)
    {
        String url = "";
        String fileName = "testFile_"+ UUID.randomUUID() + "_" + file.getOriginalFilename();
        String response = "/content/"+fileName;
        String uploadPath = "/tmp/images/"+fileName;
        try {
            File newFile = new File(uploadPath);
            // File existence is required to check because its not explicitly checked in the transferto function. Otherwise it gives (no such file or directory) error.
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            file.transferTo(newFile);
        } catch (IOException e) {
            return ResponseEntity.ok("Exception uploading an image");
        }

        return ResponseEntity.ok(response);
    }
}
