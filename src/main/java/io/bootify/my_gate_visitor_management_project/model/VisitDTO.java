package io.bootify.my_gate_visitor_management_project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class VisitDTO {

    private Long id;

    private VisitStatus status;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    @NotNull
    @Size(max = 255)
    private String purpose;

    @Size(max = 255)
    private String urlOfImage;

    @NotNull
    private Integer noOfPeople;

    @NotNull
    private Long visitor;

    @NotNull
    private String flatNumber;

    private String userName;

}
