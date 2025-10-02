package se331.lab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByTitle(String title, Pageable pageRequest);
    Page<Event> findByTitleContaining(String title, Pageable pageRequest);
    Page<Event> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageRequest);
    Page<Event> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageRequest);
    Page<Event> findByTitleContainingOrDescriptionContainingOrOrganizerNameContaining(String title, String description, String organizerName, Pageable pageRequest);
    Page<Event> findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrOrganizerNameIgnoreCaseContaining(String title, String description, String organizerName, Pageable pageRequest);
}


