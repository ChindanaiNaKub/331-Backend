![CmuLogo](https://web.archive.org/web/20250925181300im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfBQDGAI5k37iQ2c1-4Y7J_6RhjecmH37Y4GT8AfI_QTcib2Lml0TLgx52lZ_zO-Gyvrk1r7BuyZucZr6AjiLOPtmWTEUWlOOiGnT_6SLjjCs4EI_Jo8Qm07bXN_3L0gK8hZTZ8I4fcHjTumMvrEnVVLqRNxFlB5=s800)

CHIANG MAI UNIVERSITY

Bachelor of Science (Software Engineering)

College of Arts, Media and Technology

1st Semester / Academic Year 2025

SE 331 Component Based Software Development

![](https://web.archive.org/web/20250925181300im_/https://docs.google.com/drawings/d/17hEAuvyRTA9dITKkrjUmkcalfLCqrAZ78sO2b_0i-FWiqlmjBJ2uE58-1Ahtng8AysckJgbqUR9OpK0a1NAIyP9gfjNr/image?parent=1uKGA-W18sivsL2AWGhp0414OaYZLpE9S&amp;rev=1&amp;drawingRevisionAccessToken=LUCSsveWgH_TLw&amp;h=11&amp;w=11&amp;ac=1)

Entity Relationship and Vue Component  Forms

Name …………………..……………. ID ……………………

_Objective_ The entities have relations. In this session, the relationship management in JPA is declared. In additional used of the component of the Vue

_Suggestion: _You should read the instructions step by step. Please try to answer question by question without skipping some questions which you think are extremely difficult.

_Hint _ The symbol + and – in front of the source code is to show that you have to remove the source code and add the source code only. There are not the part of the source code

1. remove your Organizer entity, service, dao, and repository and use the new one

1.1. Add new Entities in the entity package as given

```
class Organization
public class Organizer {
   Long id;
   String name;
   List<Event> ownEvents;
}
```

```
class Participant
public class Participant {
   Long id;
   String name;
   String telNo;
   List<Event> eventHistory;
}
```

then update the Event to receive the Organizer object instead of Organizer

and add List of participants.

```
    Boolean petAllowed;
   String organizer;
   Organizer organizer;
   List<Participant> participants;
```

```
}
```

now _Draw the class diagram of the Entities and show to the Staff_

1.2.
   ```
   In the InitApp Class, there are errors as the organizer is not String any more. remove the code which setup the organizer and try to run the application.
   ```

```
                .date("3rd Sept")
                .time("3.00-4.00 pm.")
                .petAllowed(false)
               .organizer("CAMT").build());
               .build());
```

2. You may notice that there is an error which halts the application to start. We need to map the relationship among the entities as given

2.1. Update the Entities as given

```
        Event.java
   @ManyToOne
    Organizer organizer;
   @ManyToMany(mappedBy = "eventHistory")
   List<Participant> participants;
}
        Organizer.java
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Organizer {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Exclude
    Long id;
    String name;
   @OneToMany(mappedBy = "organizer")
    List<Event> ownEvents;
}
```

```
        Participant.java
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Participant {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Exclude
    Long id;
    String name;
    String telNo;
   @ManyToMany
   List<Event> eventHistory;
}
```

2.2. Now run the application, show the Database Structure to the staff. Check that the spring.jpa.hibernate.ddl-auto value must be create-drop
2.3.
   ```
   Setup the Repository to manage the Organizer Entity with the Database, add the OrganizerRepository in the repository package
   ```

```
public interface OrganizerRepository extends JpaRepository<Organizer,Long> {
}
```

2.4. Update the InitApp to link the Organizer with the Event

```
@Component
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    EventRepository eventRepository;
```

```
   final OrganizerRepository organizerRepository;
```

…

```
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
       eventRepository.save(Event.builder()
       Organizer org1,org2,org3;
       org1 = organizerRepository.save(Organizer.builder()
               .name("CAMT").build());
       org2 = organizerRepository.save(Organizer.builder()
               .name("CMU").build());
       org3 = organizerRepository.save(Organizer.builder()
       .name("ChiangMai").build());
       Event tempEvent;
       tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
                .title("Midterm Exam")
```

…

```
                .petAllowed(false)
                .build());
       eventRepository.save(Event.builder()
       tempEvent.setOrganizer(org1);
       org1.getOwnEvents().add(tempEvent);
       tempEvent = eventRepository.save(Event.builder()
                .category("Academic")
```

…

```
                .build());
       eventRepository.save(Event.builder()
       tempEvent.setOrganizer(org1);
       org1.getOwnEvents().add(tempEvent);
       tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
```

…

```
                .time("8.00-10.00 pm.")
                .petAllowed(false)
                .build());
       eventRepository.save(Event.builder()
       tempEvent.setOrganizer(org2);
       org2.getOwnEvents().add(tempEvent);
       tempEvent = eventRepository.save(Event.builder()
                .category("Cultural")
                .title("Songkran")
```

…

```
                .petAllowed(true)
                .build());
       tempEvent.setOrganizer(org3);
       org3.getOwnEvents().add(tempEvent);
    }
```

2.5. Run the application, you will see the NullPointerException as we did not create the getOwnEvents List yet, update the Organizer to create the List when build the object

```
    @OneToMany(mappedBy = "organizer")
   List<Event> ownEvents;
   @Builder.Default
   List<Event> ownEvents = new ArrayList<>();
```

2.6. show the result in the Event table. anything in the last column?, and then open the ApiDog to query the Events using the pagination, what did you get?

Note that if there is the error about alter table …, that’s because the constraint cannot be removed. It is ok if you did not change the database structure. But if you have done, you should delete the database and run the application again..

2.7. The Foreign key is not added yet. update the InitApp to set the relationship in the Session

```
Note that you should import jakarta.transaction.Transactional;
```

```
    @Override
   @Transactional
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
```

_        Now rerun the application, show the result in the Database, the id of the Organizer is added to the Event_

2.8. Now open the ApiDog and try to query for the Event using the pagination, what do you get?

3. Using Data Transfer Object

3.1.
   ```
   The Jackson has the recursive call for the Event, and the Organizer. To protect the problem. The DTO is applied. In the entity package, add
   ```

EventDTO.java

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
   Long id;
   String category;
   String title;
   String description;
   String location;
   String date;
   String time;
   Boolean petAllowed;
   EventOrganizerDTO organizer;
}
```

EventOrgainzerDTO.java

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventOrganizerDTO {
   Long id;
   String name;
}
```

3.2. Now we can copy the attribute value from the Event to the EventDTO. Notice that the EventOrganizerDTO class does not contain the Link back to EventDTO, or Event like in the Organizer class. So the StackOverflow problem cannot be occurred
3.3.
   ```
   To copy the value to another Object in the different class, we can use the MapStruct component to help us.
   ```

Add the dependency to the pom.xml

```
    <properties>
        <java.version>17</java.version>
       <org.mapstruct.version>1.6.0 </org.mapstruct.version>
    </properties>
```

…

```
        <dependency>
           <groupId>org.mapstruct</groupId>
           <artifactId>mapstruct</artifactId>
           <version>${org.mapstruct.version}</version>
       </dependency>
       <dependency>
           <groupId>org.mapstruct</groupId>
           <artifactId>mapstruct-processor</artifactId>
           <version>${org.mapstruct.version}</version>
           <scope>provided</scope>
       </dependency>
    </dependencies>
```

3.4. Create the Mapper Interface, create the package util, then add the given file

LabMapper.java

```
@Mapper
public interface LabMapper {
   LabMapper INSTANCE = Mappers.getMapper(LabMapper.class);
   EventDTO getEventDto(Event event);
   List<EventDTO> getEventDto(List<Event> events);
}
```

Note that after you have updated the LabMapper.java, you need to rebuild the project (Build->Rebuild Project menu) to complete the code generation.

3.5. update the EventController to change the Event object to the Mapper object

```
       responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
       return new ResponseEntity<>(pageOutput.getContent(),responseHeader,HttpStatus.OK);
```

```
       return new ResponseEntity<>(LabMapper.INSTANCE.getEventDto(pageOutput.getContent()),responseHeader,HttpStatus.OK);
```

```
    }
```

…

```
        if (output != null) {
           return ResponseEntity.ok(output);
           return  ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given id is not found");
        }
```

…

```
        Event output = eventService.save(event);
       return ResponseEntity.ok(output);
       return ResponseEntity.ok(LabMapper.INSTANCE.getEventDto(output));
    }
```

3.6. Now rerun the ApiDog and see the output

4. Mapping with the Frontend

4.1. Now we will update the EventCard to show the Organizer
4.2. Update the EventCard in the components folder

```
       <h2>{{ event.title }}</h2>
     <span>by</span>
     <h5>{{ event.organizer.name }}</h5>
       <span>{{ event.category }} @ {{ event.location }}</span>
```

Note that the organizer type must be added to the event. add the new type in types.ts

```
   petsAllowed: boolean
 organizer: string
 organizer: Organizer
}
export interface Organizer {
 id: number
 name: string
 }
```

and in the EventFormView.vue

```
   time: '',
   petsAllowed: false,
 organizer: ''
 organizer: {
   id: 0,
   name: ''
 }
```

})

```
        Then run the frontend and show the updated Event card
```

5. Using the Vue Form component

5.1. we can create a small component to use in our form,

From the form we have done last lab, we can create the BaseInput which will receive the input with the setup data as given.

Create a new file name BaseInput.vue in the component folder

```
<script setup lang="ts">
const modelValue = defineModel()
interface BaseInputProps {
 label: string
}
const props = withDefaults(defineProps<BaseInputProps>(), {
 label: ''
})
</script>
<template>
 <label v-if="label">
   {{ props.label }}
 </label>
 <input class="mb-6" v-bind="" v-model="modelValue" :placeholder="label" />
</template>
```

5.2. Update the EventForm.vue to use a new component instead of the old input tag

note that the error is occurred due to the event datatype is changed, updated the event organizer to the given value

```
organizer: { id: 0, name: ''},
```

```
   <div>
     <h1>Create an event</h1>
     <form @submit.prevent="saveEvent">
     <label>Category</label>
     <input v-model="event.category" type="text" placeholder="Category" class="field" />
     <BaseInput v-model="event.category" type="text" label="Category" />
       <h3>Name & describe your event</h3>
```

```
     <label>Title</label>
     <input v-model="event.title" type="text" placeholder="Title" class="field" />
     <BaseInput v-model="event.title" type="text" label="Title" />
```

…

in setup part

```
import EventService from '@/services/EventService.ts'
import BaseInput from '@/components/BaseInput'
```

```
        replace the Description, and the location and show the code to the staff
```

6. Using select in the form, now we want to add the organizer when we create an event

6.1. provide the OrganizerController in the backend part, so that the frontend can query all the organizer we have

Create the given files in the proper packages

OrganizerDao.java

```
public interface OrganizerDao {
   Page<Organizer> getOrganizer(Pageable pageRequest);
}
```

OrganizerDaoImpl.java

```
@Repository
@RequiredArgsConstructor
public class OrganizerDaoImpl implements OrganizerDao{
   final rganizerRepository organizerRepository;
   @Override
   public Page<Organizer> getOrganizer(Pageable pageRequest) {
       return organizerRepository.findAll(pageRequest);
   }
}
```

OrganizerService.java

```
public interface OrganizerService {
   List<Organizer> getAllOrganizer();
   Page<Organizer> getOrganizer(Integer page, Integer pageSize);
}
```

OrganizerServiceImpl.java

```
@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService{
   final OrganizerDao organizerDao;
   @Override
   public List<Organizer> getAllOrganizer() {
       return organizerDao.getOrganizer(Pageable.unpaged()).getContent();
   }
   @Override
   public Page<Organizer> getOrganizer(Integer page, Integer pageSize) {
       return organizerDao.getOrganizer(PageRequest.of(page,pageSize));
   }
}
```

OrganizerController.java

```
@RestController
@RequiredArgsConstructor
public class OrganizerController {
   final OrganizerService organizerService;
   @GetMapping("/organizers")
   ResponseEntity<?> getOrganizers(){
       return ResponseEntity.ok(organizerService.getAllOrganizer());
   }
}
```

```
Show the staff that the files are located in the proper package, and run the ApiDog to show the result
```

6.2. The error is still occurred due to the stack overflow; update the given file to fix the data

add

OrganizerDTO.java

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerDTO {
   Long id;
   String name;
   List<OrganizerOwnEventsDTO> ownEvents = new ArrayList<>();
}
```

OrganizerOwnEventsDTO.java

```
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerOwnEventsDTO {
   Long id;
   String category;
   String title;
   String description;
   String location;
   String date;
   String time;
   Boolean petAllowed;
   List<Participant> participants;
}
```

update

LabMapper.java

```
    List<EventDTO> getEventDto(List<Event> events);
   OrganizerDTO getOrganizerDTO(Organizer organizer);
   List<OrganizerDTO> getOrganizerDTO(List<Organizer> organizers);
```

update

OrganizerController.java

```
    ResponseEntity<?> getOrganizers(){
       return ResponseEntity.ok(organizerService.getAllOrganizer());
       return ResponseEntity.ok(LabMapper.INSTANCE.getOrganizerDTO(organizerService.getAllOrganizer()));
    }
```

_        Now run the ApiDog to get all Organizer again_

6.3. Now add the Select component for selecting the Organization to add to the event

```
        Get back to the front end
```

add an OrganizerService in the service folder to get the organizer data

```
import axios from 'axios'
const apiClient = axios.create({
 baseURL: import.meta.env.VITE_BACKEND_URL,
 withCredentials: false,
 headers: {
   Accept: 'application/json',
   'Content-Type': 'application/json'
 }
})
export default {
 getOrganizers() {
   return apiClient.get('/organizers')
 }
}
```

update the EventFormView.vue to get the organizer objects  in the Event object,and add the select to read the organizers from the servers

in the  part adds

```
import type { Event, Organizer } from '@/types'
import OrganizerSerivce from '@/services/OrganizerSerivce'
```

…

```
const organizers = ref<Organizer[]>([])
onMounted(() => {
 OrganizerSerivce.getOrganizers()
   .then((response) => {
     organizers.value = response.data
   })
   .catch(() => {
     router.push({ name: 'network-error-view' })
   })
})
 </script>
```

in the  part

```
       <BaseInput v-model="event.location" type="text" label="Location" />
     <h3>Who is your organizer?</h3>
     <label>Select an Organizer</label>
     <select v-model="event.organizer.id">
       <option
         v-for="option in organizers"
         :value="option.id"
         :key="option.id"
         :selected="option.id === event.organizer.id"
       >
         {{ option.name }}
       </option>
     </select>
       <button class="button" type="submit">Submit</button>
```

6.4. add a new event, see in the input, show what have sent to the backend
6.5. Only the OrganizerId is sent, updating the save in the backend in order to extract the correct object to be saved.

update the files as given

OrganizerDao.java

```
public interface OrganizerDao {
    Page<Organizer> getOrganizer(Pageable pageRequest);
   Optional<Organizer> findById(Long id);
}
```

OrganizerDaoImpl.java

```
    public Page<Organizer> getOrganizer(Pageable pageRequest) {
        return organizerRepository.findAll(pageRequest);
    }
   @Override
   public Optional<Organizer> findById(Long id) {
       return organizerRepository.findById(id);
   }
```

EventServiceImpl.java

```
    @Autowired
    final EventDao eventDao;
```

```
  final OrganizerDao organizerDao;
    @Override
```

…

```
    @Override
   @Transactional
    public Event save(Event event) {
       Organizer organizer = organizerDao.findById(event.getOrganizer().getId()).orElse(null);
       event.setOrganizer(organizer);
       organizer.getOwnEvents().add(event);
        return eventDao.save(event);
    }
```

Now try to save the event, see what has been returned, and _show in the ApiDog that the new event _has been added to the organizer

6.6. Now we can change the select to be the component

create a new file BaseSelect.vue in the component folder

```
<script setup lang="ts">
import type { Organizer } from '@/types'
const modelValue = defineModel()
interface BaseSelectProps {
 label: string
 options: Organizer[]
}
const props = withDefaults(defineProps<BaseSelectProps>(), {
 label: ''
})
</script>
<template>
 <label v-if="label">
   {{ props.label }}
 </label>
 <select class="mb-6" v-bind="" v-model="modelValue">
   <option
     v-for="option in props.options"
     :key="option.id"
     :value="option.id"
     :selected="option.id === modelValue"
   >
     {{ option.name }}
   </option>
 </select>
</template>
```

then update the EventForm.vue

```
       <h3>Who is your organizer?</h3>
       <label>Select an Organizer</label>
     <select v-model="event.organizer.id">
       <option
         v-for="option in organizers"
         :value="option.id"
         :key="option.id"
         :selected="option.id === event.organizer.id"
       >
         {{ option.name }}
       </option>
     </select>
     <BaseSelect v-model="event.organizer.id" :options="organizers" label="Organizer" />
```

```
       <button class="button" type="submit">Submit</button>
     </form>
```

_Show the staff that the code is still work_

7. Now, create 5 participants with your own data,

7.1. 3 of them should be the participants for 3 events, and any events for the rest. However, every event should have at least 3 participants.
7.2. Show in the ApiDog that each event contains at least 3 participants
7.3. Write the Controller to show a List of participants and their attended events.
