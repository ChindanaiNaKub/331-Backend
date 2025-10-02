package se331.lab.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import se331.lab.entity.Participant;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("manual")
public class ParticipantDaoImpl implements ParticipantDao {
    List<Participant> participantList;

    @PostConstruct
    public void init() {
        participantList = new ArrayList<>();
        participantList.add(Participant.builder()
                .id(1L)
                .name("John Doe")
                .telNo("123-456-7890")
                .build());
        participantList.add(Participant.builder()
                .id(2L)
                .name("Jane Smith")
                .telNo("987-654-3210")
                .build());
    }

    @Override
    public Integer getParticipantSize() {
        return participantList.size();
    }

    @Override
    public Page<Participant> getParticipants(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? participantList.size() : pageSize;
        page = page == null ? 1 : page;
        int firstIndex = (page - 1) * pageSize;
        int toIndex = Math.min(firstIndex + pageSize, participantList.size());
        List<Participant> slice = firstIndex >= participantList.size() ? java.util.Collections.emptyList() : participantList.subList(firstIndex, toIndex);
        return new PageImpl<>(slice, PageRequest.of(page - 1, pageSize), participantList.size());
    }

    @Override
    public Page<Participant> getParticipants(String name, Pageable page) {
        String keyword = name == null ? "" : name.trim().toLowerCase();
        List<Participant> filtered = keyword.isEmpty() ? participantList : participantList.stream()
                .filter(p ->
                        (p.getName() != null && p.getName().toLowerCase().contains(keyword)) ||
                        (p.getTelNo() != null && p.getTelNo().toLowerCase().contains(keyword))
                )
                .toList();

        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        int firstIndex = Math.max(0, pageNumber) * pageSize;
        int toIndex = Math.min(firstIndex + pageSize, filtered.size());

        List<Participant> slice = firstIndex >= filtered.size() ? java.util.Collections.emptyList() : filtered.subList(firstIndex, toIndex);
        return new PageImpl<>(slice, page, filtered.size());
    }

    @Override
    public Participant getParticipant(Long id) {
        return participantList.stream().filter(participant -> participant.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Participant save(Participant participant) {
        if (participantList.isEmpty()) {
            participant.setId(1L);
        } else {
            Long nextId = participantList.get(participantList.size() - 1).getId() + 1;
            participant.setId(nextId);
        }
        participantList.add(participant);
        return participant;
    }
}
