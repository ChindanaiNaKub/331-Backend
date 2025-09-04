package se331.lab.dao;

import se331.lab.Event;
import java.util.List;

public interface EventDao {
  Integer getEventSize();
  List<Event> getEvents(Integer pageSize, Integer page);
  Event getEvent(Long id);
}