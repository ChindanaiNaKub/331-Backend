package se331.lab;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    Long id;
    String title;
    String description;
    String location;
    String date;
    String time;
    Boolean petAllowed;
    String organizer;
    String category;
}
