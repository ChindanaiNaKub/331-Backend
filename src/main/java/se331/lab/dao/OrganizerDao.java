package se331.lab.dao;

import se331.lab.Organizer;
import java.util.List;

public interface OrganizerDao {
  Integer getOrganizerSize();
  List<Organizer> getOrganizers(Integer pageSize, Integer page);
  Organizer getOrganizer(Long id);
}


