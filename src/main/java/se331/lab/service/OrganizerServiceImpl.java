package se331.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se331.lab.Organizer;
import se331.lab.dao.OrganizerDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService{
    final OrganizerDao organizerDao;
    @Override
    public Integer getOrganizerSize() {
        return organizerDao.getOrganizerSize();
    }

    @Override
    public List<Organizer> getOrganizers(Integer pageSize, Integer page) {
        return organizerDao.getOrganizers(pageSize, page);
    }

    @Override
    public Organizer getOrganizer(Long id) {
        return organizerDao.getOrganizer(id);
    }
}


