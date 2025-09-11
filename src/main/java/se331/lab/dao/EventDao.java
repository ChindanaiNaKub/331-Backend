package se331.lab.dao;

import org.springframework.data.domain.Page;
import se331.lab.Event;

public interface EventDao {
  Integer getEventSize();
  Page<Event> getEvents(Integer pageSize, Integer page);
  Event getEvent(Long id);
}