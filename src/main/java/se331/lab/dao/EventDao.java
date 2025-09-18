package se331.lab.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import se331.lab.Event;

public interface EventDao {
  Integer getEventSize();
  Page<Event> getEvents(Integer pageSize, Integer page);
  Page<Event> getEvents(String title, Pageable page);
  Event getEvent(Long id);
  Event save(Event event);
}