package se331.lab.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;
import se331.lab.Event;
import se331.lab.Organizer;
import se331.lab.Organization;
import se331.lab.repository.EventRepository;
import se331.lab.repository.OrganizerRepository;
import se331.lab.repository.OrganizationRepository;

@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    final EventRepository eventRepository;
    final OrganizerRepository organizerRepository;
    final OrganizationRepository organizationRepository;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
        organizerRepository.save(Organizer.builder().organizationName("Nature Care Org").address("123 Green St, Flora City").build());
        organizerRepository.save(Organizer.builder().organizationName("Animal Friends Association").address("55 Paw Ave, Meow Town").build());
        organizerRepository.save(Organizer.builder().organizationName("Community Helpers").address("9 Unity Rd, Tin City").build());
        
        // Initialize organization data
        organizationRepository.save(Organization.builder()
                .name("Tech Innovation Hub")
                .description("A leading technology innovation center focused on developing cutting-edge solutions for modern challenges.")
                .address("123 Tech Street, Innovation District, Silicon Valley, CA 94000")
                .contactPerson("Sarah Johnson")
                .email("contact@techinnovationhub.com")
                .phone("+1-555-0123")
                .website("https://www.techinnovationhub.com")
                .establishedDate("2018-03-15")
                .build());
        
        organizationRepository.save(Organization.builder()
                .name("Green Earth Foundation")
                .description("Environmental conservation organization dedicated to protecting natural ecosystems and promoting sustainable practices.")
                .address("456 Green Avenue, Eco City, Portland, OR 97200")
                .contactPerson("Michael Chen")
                .email("info@greenearthfoundation.org")
                .phone("+1-555-0456")
                .website("https://www.greenearthfoundation.org")
                .establishedDate("2015-07-22")
                .build());
        
        organizationRepository.save(Organization.builder()
                .name("Community Health Alliance")
                .description("Non-profit organization providing healthcare services and health education to underserved communities.")
                .address("789 Health Plaza, Medical District, Austin, TX 73300")
                .contactPerson("Dr. Emily Rodriguez")
                .email("admin@communityhealthalliance.org")
                .phone("+1-555-0789")
                .website("https://www.communityhealthalliance.org")
                .establishedDate("2012-11-08")
                .build());
        
        eventRepository.save(Event.builder()
                .category("Academic")
                .title("Midterm Exam")
                .description("A time for taking the exam")
                .location("CAMT Building")
                .date("3rd Sept")
                .time("3.00-4.00 pm.")
                .petAllowed(false)
                .organizer("CAMT").build());
        eventRepository.save(Event.builder()
                .category("Academic")
                .title("Commencement Day")
                .description("A time for celebration")
                .location("CMU Convention hall")
                .date("21th Jan")
                .time("8.00am-4.00 pm.")
                .petAllowed(false)
                .organizer("CMU").build());
        eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Loy Krathong")
                .description("A time for Krathong")
                .location("Ping River")
                .date("21th Nov")
                .time("8.00-10.00 pm.")
                .petAllowed(false)
                .organizer("Chiang Mai").build());
        eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Songkran")
                .description("Let's Play Water")
                .location("Chiang Mai Moat")
                .date("13th April")
                .time("10.00am - 6.00 pm.")
                .petAllowed(true)
                .organizer("Chiang Mai Municipality").build());
    }
}


