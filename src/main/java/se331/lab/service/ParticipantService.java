package se331.lab.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.lab.entity.Participant;

public interface ParticipantService {
    Integer getParticipantSize();
    Page<Participant> getParticipants(Integer pageSize, Integer page);
    Page<Participant> getParticipants(String name, Pageable pageable);
    Participant getParticipant(Long id);
    Participant save(Participant participant);
}
