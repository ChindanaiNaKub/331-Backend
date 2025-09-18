package se331.lab.dao;

import jakarta.annotation.PostConstruct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import se331.lab.Event;

import java.util.ArrayList;
import java.util.List;

@Repository
@Profile("manual")
public class EventDaoImpl implements EventDao {
    List<Event> eventList;

    @PostConstruct
    public void init() {
        eventList = new ArrayList<>();
        eventList.add(Event.builder()
                .id(123L)
                .category("animal welfare")
                .title("Cat Adoption Day")
                .description("Find your new feline friend at this event.")
                .location("Meow Town")
                .date("January 28, 2022")
                .time("12:00")
                .petAllowed(true)
                .organizer("Kat Laydee")
                .build());
        eventList.add(Event.builder()
                .id(456L)
                .category("food")
                .title("Community Gardening")
                .description("Join us as we tend to the community edible plants.")
                .location("Flora City")
                .date("March 14, 2022")
                .time("10:00")
                .petAllowed(true)
                .organizer("Fern Pollin")
                .build());
        // Add the rest of the events similar to your db.json as needed
        eventList.add(Event.builder()
                .id(789L)
                .category("sustainability")
                .title("Beach Cleanup")
                .description("Help pick up trash along the shore.")
                .location("Playa Del Carmen")
                .date("July 22, 2022")
                .time("11:00")
                .petAllowed(false)
                .organizer("Carey Wales")
                .build());
        eventList.add(Event.builder()
                .id(1001L)
                .category("animal welfare")
                .title("Dog Adoption Day")
                .description("Find your new canine friend at this event.")
                .location("Woof Town")
                .date("August 28, 2022")
                .time("12:00")
                .petAllowed(true)
                .organizer("Dawg Dahd")
                .build());
        eventList.add(Event.builder()
                .id(1002L)
                .category("food")
                .title("Canned Food Drive")
                .description("Bring your canned food to donate to those in need.")
                .location("Tin City")
                .date("September 14, 2022")
                .time("3:00")
                .petAllowed(true)
                .organizer("Kahn Opiner")
                .build());
        eventList.add(Event.builder()
                .id(1003L)
                .category("sustainability")
                .title("Highway Cleanup")
                .description("Help pick up trash along the highway.")
                .location("Highway 50")
                .date("July 22, 2022")
                .time("11:00")
                .petAllowed(false)
                .organizer("Brody Kill")
                .build());
    }

    @Override
    public Integer getEventSize() {
        return eventList.size();
    }

    @Override
    public Page<Event> getEvents(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? eventList.size() : pageSize;
        page = page == null ? 1 : page;
        int firstIndex = (page - 1) * pageSize;
        int toIndex = Math.min(firstIndex + pageSize, eventList.size());
        List<Event> slice = firstIndex >= eventList.size() ? java.util.Collections.emptyList() : eventList.subList(firstIndex, toIndex);
        return new PageImpl<>(slice, PageRequest.of(page - 1, pageSize), eventList.size());
    }

    @Override
    public Page<Event> getEvents(String title, Pageable page) {
        String keyword = title == null ? "" : title.trim().toLowerCase();
        List<Event> filtered = keyword.isEmpty() ? eventList : eventList.stream()
                .filter(e ->
                        (e.getTitle() != null && e.getTitle().toLowerCase().contains(keyword)) ||
                        (e.getDescription() != null && e.getDescription().toLowerCase().contains(keyword)) ||
                        (e.getOrganizer() != null && e.getOrganizer().toLowerCase().contains(keyword))
                )
                .toList();

        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();
        int firstIndex = Math.max(0, pageNumber) * pageSize;
        int toIndex = Math.min(firstIndex + pageSize, filtered.size());

        List<Event> slice = firstIndex >= filtered.size() ? java.util.Collections.emptyList() : filtered.subList(firstIndex, toIndex);
        return new PageImpl<>(slice, page, filtered.size());
    }

    @Override
    public Event getEvent(Long id) {
        return eventList.stream().filter(event -> event.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Event save(Event event) {
        if (eventList.isEmpty()) {
            event.setId(1L);
        } else {
            Long nextId = eventList.get(eventList.size() - 1).getId() + 1;
            event.setId(nextId);
        }
        eventList.add(event);
        return event;
    }
}
