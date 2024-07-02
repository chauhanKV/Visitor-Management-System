package io.bootify.my_gate_visitor_management_project.service;

import io.bootify.my_gate_visitor_management_project.config.SecurityConfig;
import io.bootify.my_gate_visitor_management_project.domain.Flat;
import io.bootify.my_gate_visitor_management_project.domain.Person;
import io.bootify.my_gate_visitor_management_project.domain.Visit;
import io.bootify.my_gate_visitor_management_project.domain.Visitor;
import io.bootify.my_gate_visitor_management_project.model.VisitDTO;
import io.bootify.my_gate_visitor_management_project.model.VisitStatus;
import io.bootify.my_gate_visitor_management_project.repos.FlatRepository;
import io.bootify.my_gate_visitor_management_project.repos.PersonRepository;
import io.bootify.my_gate_visitor_management_project.repos.VisitRepository;
import io.bootify.my_gate_visitor_management_project.repos.VisitorRepository;
import io.bootify.my_gate_visitor_management_project.util.NotFoundException;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitorRepository visitorRepository;
    private final FlatRepository flatRepository;
    private final PersonRepository personRepository;

    public VisitService(final VisitRepository visitRepository,
            final VisitorRepository visitorRepository, final FlatRepository flatRepository,
            final PersonRepository personRepository) {
        this.visitRepository = visitRepository;
        this.visitorRepository = visitorRepository;
        this.flatRepository = flatRepository;
        this.personRepository = personRepository;
    }

    public List<VisitDTO> findAll() {
        final List<Visit> visits = visitRepository.findAll(Sort.by("id"));
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public List<VisitDTO> findAllWaitingVisits() {
        final List<Visit> visits = visitRepository.findByStatus(VisitStatus.WAITING);
        return visits.stream()
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .toList();
    }

    public VisitDTO get(final Long id) {
        return visitRepository.findById(id)
                .map(visit -> mapToDTO(visit, new VisitDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VisitDTO visitDTO) throws BadRequestException {
        final Visit visit = new Visit();
        visitDTO.setStatus(VisitStatus.WAITING);
        mapToEntity(visitDTO, visit);
        return visitRepository.save(visit).getId();
    }

    public void update(final Long id, final VisitDTO visitDTO) throws BadRequestException {
        final Visit visit = visitRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(visitDTO, visit);
        visitRepository.save(visit);
    }

    public void delete(final Long id) {
        visitRepository.deleteById(id);
    }

    private VisitDTO mapToDTO(final Visit visit, final VisitDTO visitDTO) {
        visitDTO.setId(visit.getId());
        visitDTO.setStatus(visit.getStatus());
        visitDTO.setInTime(visit.getInTime());
        visitDTO.setOutTime(visit.getOutTime());
        visitDTO.setPurpose(visit.getPurpose());
        visitDTO.setUrlOfImage(visit.getUrlOfImage());
        visitDTO.setNoOfPeople(visit.getNoOfPeople());
        visitDTO.setVisitor(visit.getVisitor() == null ? null : visit.getVisitor().getId());
        visitDTO.setFlatNumber(visit.getFlat() == null ? null : visit.getFlat().getNumber());
        return visitDTO;
    }

    private Visit mapToEntity(final VisitDTO visitDTO, final Visit visit) throws BadRequestException {
        visit.setStatus(visitDTO.getStatus());
        visit.setInTime(visitDTO.getInTime());
        visit.setOutTime(visitDTO.getOutTime());
        visit.setPurpose(visitDTO.getPurpose());
        visit.setUrlOfImage(visitDTO.getUrlOfImage());
        visit.setNoOfPeople(visitDTO.getNoOfPeople());
        final Visitor visitor = visitDTO.getVisitor() == null ? null : visitorRepository.findById(visitDTO.getVisitor())
                .orElseThrow(() -> new NotFoundException("visitor not found"));
        visit.setVisitor(visitor);
        final Flat flat = flatRepository.findByNumber(visitDTO.getFlatNumber());
        if(flat == null)
        {
            throw new BadRequestException("Invalid Flat Number");
        }
        visit.setFlat(flat);
        return visit;
    }

    public void updateInTime(Long visitId)
    {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if(visit == null)
        {
            throw new NotFoundException();
        }
        if(visit.getStatus().equals(VisitStatus.APPROVED)) {
            visit.setInTime(LocalDateTime.now());
            visit.setStatus(VisitStatus.INPROGRESS);
            visitRepository.save(visit);
        }
    }

    public void updateOutTime(Long visitId)
    {
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if(visit == null)
        {
            throw new NotFoundException();
        }
        if(visit.getStatus().equals(VisitStatus.INPROGRESS)) {
            visit.setOutTime(LocalDateTime.now());
            visit.setStatus(VisitStatus.COMPLETED);
            visitRepository.save(visit);
        }
    }

    @Transactional
    public void approveVisit(Long visitId) throws BadRequestException {
        Person loggedIn = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loggedIn.getId();
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null) {
            throw new NotFoundException();
        }

        if (visit.getStatus().equals(VisitStatus.WAITING)) {
            Person person = personRepository.findById(userId).get();
            if (person.getFlat() == visit.getFlat()) {
                visit.setStatus(VisitStatus.APPROVED);
                visitRepository.save(visit);
            } else {
                throw new BadRequestException("User is not authorized to approve this request");
            }
        }
    }

    public void rejectVisit(Long visitId)
    {
        Person loggedIn = (Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loggedIn.getId();
        Visit visit = visitRepository.findById(visitId).orElse(null);
        if (visit == null) {
            throw new NotFoundException();
        }
        if(visit.getStatus().equals(VisitStatus.WAITING)) {
            visit.setStatus(VisitStatus.REJECTED);
            visitRepository.save(visit);
        }
    }
}
