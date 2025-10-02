package se331.lab.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import se331.lab.entity.Organizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!db")
public class OrganizerDaoImpl implements OrganizerDao {
    List<Organizer> organizerList;

    @PostConstruct
    public void init() {
        organizerList = new ArrayList<>();
        organizerList.add(Organizer.builder()
                .id(1L)
                .name("Nature Care Org")
                .build());
        organizerList.add(Organizer.builder()
                .id(2L)
                .name("Animal Friends Association")
                .build());
        organizerList.add(Organizer.builder()
                .id(3L)
                .name("Community Helpers")
                .build());
    }

    @Override
    public Page<Organizer> getOrganizer(Pageable pageRequest) {
        int pageSize = pageRequest.getPageSize();
        int pageNumber = pageRequest.getPageNumber();
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, organizerList.size());
        
        if (startIndex >= organizerList.size()) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, organizerList.size());
        }
        
        List<Organizer> pageContent = organizerList.subList(startIndex, endIndex);
        return new PageImpl<>(pageContent, pageRequest, organizerList.size());
    }
    
    @Override
    public Optional<Organizer> findById(Long id) {
        return organizerList.stream()
                .filter(organizer -> organizer.getId().equals(id))
                .findFirst();
    }
}


