package se331.lab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import jakarta.transaction.Transactional;
import se331.lab.AuctionItem;
import se331.lab.Bid;
import se331.lab.entity.Event;
import se331.lab.entity.Organizer;
import se331.lab.entity.Organization;
import se331.lab.entity.Participant;
import se331.lab.repository.EventRepository;
import se331.lab.repository.OrganizerRepository;
import se331.lab.repository.OrganizationRepository;
import se331.lab.repository.ParticipantRepository;
import se331.lab.repository.AuctionItemRepository;
import se331.lab.repository.BidRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    final EventRepository eventRepository;
    final OrganizerRepository organizerRepository;
    final OrganizationRepository organizationRepository;
    final AuctionItemRepository auctionItemRepository;
    final BidRepository bidRepository;
    final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
        // Save organizers first and get the saved instances
        organizerRepository.save(Organizer.builder().name("Nature Care Org").build());
        organizerRepository.save(Organizer.builder().name("Animal Friends Association").build());
        organizerRepository.save(Organizer.builder().name("Community Helpers").build());
        Organizer organizer4 = organizerRepository.save(Organizer.builder().name("CAMT").build());
        Organizer organizer5 = organizerRepository.save(Organizer.builder().name("CMU").build());
        Organizer organizer6 = organizerRepository.save(Organizer.builder().name("Chiang Mai").build());
        Organizer organizer7 = organizerRepository.save(Organizer.builder().name("Chiang Mai Municipality").build());
        
        // Initialize organization data
        organizationRepository.save(Organization.builder()
                .name("Tech Innovation Hub")
                .build());
        
        organizationRepository.save(Organization.builder()
                .name("Green Earth Foundation")
                .build());
        
        organizationRepository.save(Organization.builder()
                .name("Community Health Alliance")
                .build());
        
        // Now create events using the saved organizers and properly link them
        Event tempEvent;
        tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
                .title("Midterm Exam")
                .description("A time for taking the exam")
                .location("CAMT Building")
                .date("3rd Sept")
                .time("3.00-4.00 pm.")
                .petAllowed(false)
                .build());
        tempEvent.setOrganizer(organizer4);
        organizer4.getOwnEvents().add(tempEvent);
        
        tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
                .title("Commencement Day")
                .description("A time for celebration")
                .location("CMU Convention hall")
                .date("21th Jan")
                .time("8.00am-4.00 pm.")
                .petAllowed(false)
                .build());
        tempEvent.setOrganizer(organizer5);
        organizer5.getOwnEvents().add(tempEvent);
        
        tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Loy Krathong")
                .description("A time for Krathong")
                .location("Ping River")
                .date("21th Nov")
                .time("8.00-10.00 pm.")
                .petAllowed(false)
                .build());
        tempEvent.setOrganizer(organizer6);
        organizer6.getOwnEvents().add(tempEvent);
        
        tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Songkran")
                .description("Let's Play Water")
                .location("Chiang Mai Moat")
                .date("13th April")
                .time("10.00am - 6.00 pm.")
                .petAllowed(true)
                .build());
        tempEvent.setOrganizer(organizer7);
        organizer7.getOwnEvents().add(tempEvent);

        // Create 5 participants and assign them so each event has at least 3 participants
        Participant p1 = participantRepository.save(Participant.builder().name("Alice Lee").telNo("081-111-1111").build());
        Participant p2 = participantRepository.save(Participant.builder().name("Bob Chen").telNo("082-222-2222").build());
        Participant p3 = participantRepository.save(Participant.builder().name("Cara Wong").telNo("083-333-3333").build());
        Participant p4 = participantRepository.save(Participant.builder().name("David Kim").telNo("084-444-4444").build());
        Participant p5 = participantRepository.save(Participant.builder().name("Eva Park").telNo("085-555-5555").build());

        java.util.List<Event> allEvents = eventRepository.findAll();
        if (allEvents.size() >= 4) {
            Event e1 = allEvents.get(0);
            Event e2 = allEvents.get(1);
            Event e3 = allEvents.get(2);
            Event e4 = allEvents.get(3);

            // ensure lists are initialized
            if (e1.getParticipants() == null) e1.setParticipants(new java.util.ArrayList<>());
            if (e2.getParticipants() == null) e2.setParticipants(new java.util.ArrayList<>());
            if (e3.getParticipants() == null) e3.setParticipants(new java.util.ArrayList<>());
            if (e4.getParticipants() == null) e4.setParticipants(new java.util.ArrayList<>());

            // Assign participants
            e1.getParticipants().add(p1); e1.getParticipants().add(p2); e1.getParticipants().add(p3);
            e2.getParticipants().add(p1); e2.getParticipants().add(p4); e2.getParticipants().add(p5);
            e3.getParticipants().add(p2); e3.getParticipants().add(p3); e3.getParticipants().add(p4);
            e4.getParticipants().add(p1); e4.getParticipants().add(p3); e4.getParticipants().add(p5);

            // Mirror on participant side (owning side is Participant.eventHistory)
            java.util.function.Consumer<Participant> ensureHistory = (pt) -> { if (pt.getEventHistory() == null) pt.setEventHistory(new java.util.ArrayList<>()); };
            ensureHistory.accept(p1); ensureHistory.accept(p2); ensureHistory.accept(p3); ensureHistory.accept(p4); ensureHistory.accept(p5);

            p1.getEventHistory().add(e1); p1.getEventHistory().add(e2); p1.getEventHistory().add(e4);
            p2.getEventHistory().add(e1); p2.getEventHistory().add(e3);
            p3.getEventHistory().add(e1); p3.getEventHistory().add(e3); p3.getEventHistory().add(e4);
            p4.getEventHistory().add(e2); p4.getEventHistory().add(e3);
            p5.getEventHistory().add(e2); p5.getEventHistory().add(e4);

            // persist both sides
            participantRepository.save(p1);
            participantRepository.save(p2);
            participantRepository.save(p3);
            participantRepository.save(p4);
            participantRepository.save(p5);
            eventRepository.save(e1);
            eventRepository.save(e2);
            eventRepository.save(e3);
            eventRepository.save(e4);
        }

        // Seed AuctionItems and Bids with diverse data
        String[][] itemData = {
            {"Vintage Rolex Submariner Watch", "JEWELRY", "1500.0", "1600.0", "1700.0"},
            {"MacBook Pro M3 16-inch", "ELECTRONICS", "2000.0", "2200.0", "2400.0"},
            {"Rare Pokemon Charizard Card", "COLLECTIBLE", "500.0", "600.0", "700.0"},
            {"Antique Persian Rug", "FURNITURE", "800.0", "900.0", "1000.0"},
            {"Nike Air Jordan 1 Retro", "FASHION", "200.0", "250.0", "300.0"},
            {"Vinyl Record Collection - Beatles", "MUSIC", "300.0", "350.0", "400.0"},
            {"Gaming PC RTX 4090", "ELECTRONICS", "2500.0", "2700.0", "2900.0"},
            {"Diamond Engagement Ring", "JEWELRY", "3000.0", "3200.0", "3400.0"},
            {"Vintage Wine Collection", "FOOD", "1200.0", "1300.0", "1400.0"},
            {"Art Painting - Abstract", "ART", "800.0", "900.0", "1000.0"}
        };

        for (int i = 0; i < itemData.length; i++) {
            String[] data = itemData[i];
            AuctionItem item = AuctionItem.builder()
                    .description(data[0])
                    .type(data[1])
                    .build();
            
            // Create three bids with different amounts
            Bid b1 = Bid.builder().amount(Double.parseDouble(data[2])).datetime(LocalDateTime.now().minusDays(3)).build();
            Bid b2 = Bid.builder().amount(Double.parseDouble(data[3])).datetime(LocalDateTime.now().minusDays(2)).build();
            Bid b3 = Bid.builder().amount(Double.parseDouble(data[4])).datetime(LocalDateTime.now().minusDays(1)).build();
            
            item.addBid(b1);
            item.addBid(b2);
            item.addBid(b3);

            // Set successful bid for first 6 items (different types)
            if (i < 6) {
                item.setSuccessfulBid(b2);
            }

            AuctionItem saved = auctionItemRepository.save(item);
            // persist bids (cascade also handles it, but ensure saved)
            bidRepository.save(b1);
            bidRepository.save(b2);
            bidRepository.save(b3);
            // update in case successfulBid needs managed id
            if (saved.getSuccessfulBid() != null) {
                auctionItemRepository.save(saved);
            }
        }
    }
}


