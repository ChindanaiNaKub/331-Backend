package se331.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se331.lab.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
