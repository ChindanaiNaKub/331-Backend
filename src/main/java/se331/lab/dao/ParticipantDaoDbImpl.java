package se331.lab.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se331.lab.entity.Participant;
import se331.lab.repository.ParticipantRepository;

@Repository
@RequiredArgsConstructor
@Profile("db")
public class ParticipantDaoDbImpl implements ParticipantDao {
    final ParticipantRepository participantRepository;

    @Override
    public Integer getParticipantSize() {
        return Math.toIntExact(participantRepository.count());
    }

    @Override
    public Page<Participant> getParticipants(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? Math.toIntExact(participantRepository.count()) : pageSize;
        page = page == null ? 1 : page;
        return participantRepository.findAll(PageRequest.of(page - 1, pageSize));
    }

    @Override
    public Page<Participant> getParticipants(String name, Pageable page) {
        return participantRepository.findByNameContainingIgnoreCase(name, page);
    }

    @Override
    public Participant getParticipant(Long id) {
        return participantRepository.findById(id).orElse(null);
    }

    @Override
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }
}
