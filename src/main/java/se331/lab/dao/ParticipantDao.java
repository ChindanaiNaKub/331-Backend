package se331.lab.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.lab.entity.Participant;

public interface ParticipantDao {
  Integer getParticipantSize();
  Page<Participant> getParticipants(Integer pageSize, Integer page);
  Page<Participant> getParticipants(String name, Pageable page);
  Participant getParticipant(Long id);
  Participant save(Participant participant);
}
