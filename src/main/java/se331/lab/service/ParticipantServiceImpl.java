package se331.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.lab.entity.Participant;
import se331.lab.dao.ParticipantDao;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{
    final ParticipantDao participantDao;
    @Override
    public Integer getParticipantSize() {
        return participantDao.getParticipantSize();
    }

    @Override
    public Page<Participant> getParticipants(Integer pageSize, Integer page) {
        return participantDao.getParticipants(pageSize, page);
    }

    @Override
    public Page<Participant> getParticipants(String name, Pageable pageable) {
        return participantDao.getParticipants(name, pageable);
    }

    @Override
    public Participant getParticipant(Long id) {
        return participantDao.getParticipant(id);
    }

    @Override
    public Participant save(Participant participant) {
        return participantDao.save(participant);
    }
}
