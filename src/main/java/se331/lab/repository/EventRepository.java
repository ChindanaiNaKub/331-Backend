package se331.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}


