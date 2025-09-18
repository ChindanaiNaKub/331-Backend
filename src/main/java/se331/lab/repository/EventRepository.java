package se331.lab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByTitle(String title, Pageable pageRequest);
}


