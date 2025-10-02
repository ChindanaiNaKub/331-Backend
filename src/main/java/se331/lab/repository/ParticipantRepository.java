package se331.lab.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.entity.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Page<Participant> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
