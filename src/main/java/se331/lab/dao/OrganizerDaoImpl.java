package se331.lab.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import se331.lab.Organizer;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("!db")
public class OrganizerDaoImpl implements OrganizerDao {
    List<Organizer> organizerList;

    @PostConstruct
    public void init() {
        organizerList = new ArrayList<>();
        organizerList.add(Organizer.builder()
                .id(1L)
                .organizationName("Nature Care Org")
                .address("123 Green St, Flora City")
                .build());
        organizerList.add(Organizer.builder()
                .id(2L)
                .organizationName("Animal Friends Association")
                .address("55 Paw Ave, Meow Town")
                .build());
        organizerList.add(Organizer.builder()
                .id(3L)
                .organizationName("Community Helpers")
                .address("9 Unity Rd, Tin City")
                .build());
    }

    @Override
    public Integer getOrganizerSize() {
        return organizerList.size();
    }

    @Override
    public List<Organizer> getOrganizers(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? organizerList.size() : pageSize;
        page = page == null ? 1 : page;
        int firstIndex = (page - 1) * pageSize;
        int toIndex = Math.min(firstIndex + pageSize, organizerList.size());
        if (firstIndex >= organizerList.size()) {
            return List.of();
        }
        return organizerList.subList(firstIndex, toIndex);
    }

    @Override
    public Organizer getOrganizer(Long id) {
        return organizerList.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Organizer save(Organizer organizer) {
        Long nextId = organizerList.stream().map(Organizer::getId).max(Long::compareTo).orElse(0L) + 1;
        organizer.setId(nextId);
        organizerList.add(organizer);
        return organizer;
    }
}


