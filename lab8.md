![CmuLogo](https://lh7-rt.googleusercontent.com/docsd/ANYlcfDGHO0QLhGn9-u-Pi0WIbIbPA_QlcK4I-m71q0NzuZ_D5qK9ukXk4NgYqyDjUF2XMvwrAkWlM_IFjYiFejQRnXFbmlNNs562WpiBkW5cek4UP3lYb3oZx3Z0mSUCPdvvzLGjLmQZo3QPPCZ0whPVoLKL9B0uZ1y=s600)

CHIANG MAI UNIVERSITY

Bachelor of Science (Software Engineering)

College of Arts, Media and Technology

1st Semester / Academic Year 2025

SE 331 Component Based Software Development

![](https://docs.google.com/drawings/d/1SZkB8LYAfC66sIoPJ5mZ95T5mGSYyEyRW6HCZKTbqZ3I4czowd-EB8ncBdGZLAj7_1esIMx_p-YfODIGFg--zXd7QlMM/image?parent=1mSc5LnfdGLSGnJDW9xHmwfdq6kDvk8y2&amp;rev=1&amp;drawingRevisionAccessToken=qQuUmd2zowch6A&amp;h=11&amp;w=11&amp;ac=1)

Spring JPA and Http Post Methods

Name …………………..……………. ID ……………………

_Objective_ In this session, you will try to work with Spring JPA and Http Post method

_Suggestion: _You should read the instructions step by step. Please try to answer question by question without skipping some questions which you think are extremely difficult.

_Hint _ The symbol + and – in front of the source code is to show that you have to remove the source code and add the source code only. There are not the part of the source code

1. Setup the Database Management System (DBMS)

1.1. Install the Docker for your OS
1.2. Now we will work with the DBMS, create the docker-compose.yml in the root of the project as given

```
services:
 db:
   image: mysql
   ports:
     3306:3306
   environment:
     MYSQL_ROOT_PASSWORD: password
 phpmyadmin:
   image: phpmyadmin
   ports:
     9000:80
   environment:
     PMA_HOST: db
     MYSQL_ROOT_PASSWORD: password
```

1.3. In the terminal of your root folder run the command docker compose up, wait for the setup and then open the browser in localhost:9000. Login to the php my admin using the username root, password is password

2. Using the Spring Data to connect our application with the DBMS

2.1. Add the Spring-boot JPA dependency in pom.xml file as given

```
       <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa -->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-data-jpa</artifactId>
       </dependency>
       <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
       <dependency>
           <groupId>com.mysql</groupId>
           <artifactId>mysql-connector-j</artifactId>
           <version>8.4.0</version>
       </dependency>
```

_note _you may avoid typing this dependency by using google and search for maven [artifact-id]. so the result will be shown in the maven repository so you can copy the dependency from the location.

2.2. Update the Event class to be able to store inside the database as given below

```
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Event {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @EqualsAndHashCode.Exclude
    Long id;
```

_note _you have to import the class package, you may use the fix hint of the IDE to help you to fix the import problem. All required dependencies should be in lombok, and jakarta.persistance

2.3. create file using this layout

![](https://lh7-rt.googleusercontent.com/docsd/ANYlcfCAz4-8nVMyhqLFsC8SeUyW99ty9FuAn54tAv17m039LrpK2RO0EirEP9c4WWRIVjW3kgDf43YTCZ9MpOoFZjt9pqQT_gVe5k-F3-QKI8iDJ2KkvgjH373HWB0qL2rLAMcfD-ElnlBozRWHSGciQJfs4ihHUX--=s600)

```
        then update the each file as given
```

the EventRepository.java

```
package se331.lab.repository;
import org.springframework.data.repository.CrudRepository;
import se331.lab.rest.entity.Event;
import java.util.List;
public interface EventRepository extends CrudRepository<Event,Long> {
   List<Event> findAll();
}
```

the EventDaoDbImpl

```
@Repository
@RequiredArgsConstructor
public class EventDaoDbImpl implements EventDao{
   final EventRepository eventRepository;
   @Override
   public Integer getEventSize() {
       return Math.toIntExact(eventRepository.count());
   }
   @Override
   public List<Event> getEvents(Integer pageSize, Integer page) {
       List<Event> events = eventRepository.findAll();
       pageSize = pageSize == null ? events.size() : pageSize;
       page = page == null ? 1 : page;
       int firstIndex = (page - 1) * pageSize;
       List<Event> output = events.subList(firstIndex, firstIndex + pageSize);
       return output;
   }
   @Override
   public Event getEvent(Long id) {
       return eventRepository.findById(id).orElse(null);
   }
}
```

then rename the application.properties to application.yml then update the file as given below.

```
spring:
 application:
   name: 331-backend
 datasource:
```

```
   url: jdbc:mysql://localhost:3306/selabdb?createDatabaseIfNotExist=true&autoReconnect=true&characterEncoding=UTF-8&allowMultiQueries=true&allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true
```

```
   driver-class-name: com.mysql.cj.jdbc.Driver
   username: root
   password: password
 jpa:
   properties:
     hibernate:
       dialect: org.hibernate.dialect.MySQLDialect
   hibernate:
     ddl-auto: create
```

_note_ that the indentation must be as given content

2.4. Run the application, you should see the error that there are beans with the same type
2.5. To select which bean to be used add the Profile annotation to the EventDaoDbImpl, and EventDaoImpl as given

EventDaoDbImpl.java

```
@Repository
@RequiredArgsConstructor
@Profile("db")
public class EventDaoDbImpl implements EventDao{
```

EventDaoImpl.java

```
@Repository
@Profile("manual")
public class EventDaoImpl implements EventDao {
```

then update the application.yml

spring:

```
 profiles:
   active:
     manual
  datasource:
```

```
        run the application, you should be able to run the application. open to phpMyAdmin (localhost:9000) to see the value in the database
```

2.6. Open the front end part, see the output
2.7. Change the application.yml as given

spring:

```
  profiles:
    active:
     manual
     db
        then run the backend application again, refresh the browser of phpMyAdmin to show the result
```

3. Adding the data to database

3.1. Call the /events endpoint to see the output, and show to the staff
3.2. Create the package config then add an InitApp.java as given

```
@Component
@RequiredArgsConstructor
public class InitApp implements ApplicationListener<ApplicationReadyEvent> {
   final EventRepository eventRepository;
   @Override
   public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
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
```

This code will add the data to the database

3.3. Show the data in the phpMyAdmin, and show the result in the web browser.
3.4. Call the /events endpoint to see the output, and show to the staff

4. Introduction to the Pageable Object in the Spring boot web.

4.1. update the EventDao to change the getEvents return type to Page

```
import org.springframework.data.domain.Page;
import se331.lab.rest.entity.Event;
import java.util.List;
public interface EventDao {
    Integer getEventSize();
   List<Event> getEvents(Integer pageSize, Integer page);
   Page<Event> getEvents(Integer pageSize, Integer page);
    Event getEvent(Long id);
}
```

4.2. Update the EventRepository as given

```
public interface EventRepository extends CrudRepository<Event,Long> {
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAll();
```

4.3. Update the EventDaoImpl as given

```
    @Override
   public List<Event> getEvents(Integer pageSize, Integer page) {
   public Page<Event> getEvents(Integer pageSize, Integer page) {
        pageSize = pageSize == null ? eventList.size() : pageSize;
        page = page == null ? 1 : page;
        int firstIndex = (page - 1) * pageSize;
       return eventList.subList(firstIndex,firstIndex+pageSize);
```

```
       return new PageImpl<Event>(eventList.subList(firstIndex,firstIndex+pageSize),PageRequest.of(page,pageSize),eventList.size());
```

```
    }
```

4.4. Update the EventDaoDbImpl as given

```
    @Override
   public List<Event> getEvents(Integer pageSize, Integer page) {
       List<Event> events = eventRepository.findAll();
       pageSize = pageSize == null ? events.size() : pageSize;
       page = page == null ? 1 : page;
       int firstIndex = (page - 1) * pageSize;
       List<Event> output = events.subList(firstIndex, firstIndex + pageSize);
       return output;
   public Page<Event> getEvents(Integer pageSize, Integer page) {
       return eventRepository.findAll(PageRequest.of(page - 1, pageSize));
    }
```

4.5. Update the EventService.java to return the Page Object

```
public interface EventService {
    Integer getEventSize();
   List<Event> getEvents(Integer pageSize,Integer page);
   Page<Event> getEvents(Integer pageSize, Integer page);
    Event getEvent(Long id);
```

4.6. Update the EventSerivceImpl to update the getEvents implementation.

```
    @Override
   public List<Event> getEvents(Integer pageSize, Integer page) {
   public Page<Event> getEvents(Integer pageSize, Integer page) {
        return eventDao.getEvents(pageSize, page);
    }
```

4.7. Update the getEventList For the EventController

```
    @GetMapping("event")
```

```
    public ResponseEntity<?> getEventLists(@RequestParam(value = "_limit", required = false) Integer perPage
```

```
            , @RequestParam(value = "_page", required = false) Integer page) {
       List<Event> output = null;
       Integer eventSize = eventService.getEventSize();
       Page<Event> pageOutput = eventService.getEvents(perPage, page);
        HttpHeaders responseHeader = new HttpHeaders();
       responseHeader.set("x-total-count", String.valueOf(eventSize));
       try {
          output = eventService.getEvents(perPage, page);
           return new ResponseEntity<>(output,responseHeader,HttpStatus.OK);
       } catch (IndexOutOfBoundsException ex) {
           return new ResponseEntity<>(output,responseHeader,HttpStatus.OK);
       }
       responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
       return new ResponseEntity<>(pageOutput.getContent(),responseHeader,HttpStatus.OK);
    }
```

4.8. Then show the browser that the output is the same

5. Create the Post method on the backend to add new data to the database.

5.1. Update the dao and service class to get the save method as given

EventDao

```
    Page<Event> getEvents(Integer pageSize, Integer page);
    Event getEvent(Long id);
   Event save(Event event);
}
```

EventDaoImpl

```
   @Override
   public Event save(Event event) {
       event.setId(eventList.get(eventList.size()-1).getId()+1);
       eventList.add(event);
       return event;
   }
}
```

EventDaoDbImpl

```
   @Override
   public Event save(Event event) {
       return eventRepository.save(event);
   }
```

EventService

```
   Event save(Event event);
```

EventServiceImpl

```
   @Override
   public Event save(Event event) {
       return eventDao.save(event);
   }
```

5.2. update the EventController to receive the Post method as given

```
   @PostMapping("/events")
   public ResponseEntity<?> addEvent(@RequestBody Event event){
       Event output = eventService.save(event);
       return ResponseEntity.ok(output);
   }
```

5.3. Run the server, then open the ApiDog to post the object as given

![A screenshot of a computer

Description automatically generated](https://lh7-rt.googleusercontent.com/docsd/ANYlcfDoxyT-Z6hPbjYSb3ItZcjx9omtqqVKBMJnn5PT8Y-6VBaqbbLPWODLnN3F3RvXrFKxsp2h8eQFBTZ0xlr2RQ4ogAMw8tthUBc4pBCd-F-4-n6OFhrJeqGoaGHWa4oj4RwqbPta-zpTkLUMRalZCme9H8hfMg=s600)

5.4. add a new data from the ApiDog, and show the data in the phpMyAdmin

6. Implement your own backend application.

6.1. Create a new backend, or use the backend which you have done previously, to save the organization data in the database
6.2. Create the controller to receive the Organization from the Post method, then save the object to the database.

7. Add a new data from the user

7.1. Using the Front end from the previous lab
7.2. Create the file .env.development in the root folder and then add the values

```
VITE_BACKEND_URL=http://localhost:80
```

7.3. Update the EventService.ts as given

```
const apiClient = axios.create({
 baseURL: 'http://localhost:8080',
 baseURL: import.meta.env.VITE_BACKEND_URL,
   withCredentials: false,
   headers: {
     Accept: 'application/json',
```

7.4. create the EventFormView.vue in the viewsEvent folder as given.

note that the css file can be copied from _[here](https://www.google.com/url?q=https://www.google.com/url?q%3Dhttps://drive.google.com/file/d/18sTPdvSHIIoch6kFM4sTTn9xN19ThAf7/view?usp%253Ddrive_link%26amp;sa%3DD%26amp;source%3Deditors%26amp;ust%3D1757594565850920%26amp;usg%3DAOvVaw1ob23x4A4qJNBGM29pio60&sa=D&source=docs&ust=1757594565915260&usg=AOvVaw0MJqTZIErd_qH5qdRxtgQk)_

```
<script setup lang="ts">
import type { Event } from '@/types'
import { ref } from 'vue'
const event = ref<Event>({
 id: 0,
 category: '',
 title: '',
 description: '',
 location: '',
 date: '',
 time: '',
 petsAllowed: false,
 organizer: ''
})
</script>
<template>
 <div>
   <h1>Create an event</h1>
   <form>
     <label>Category</label>
     <input v-model="event.category" type="text" placeholder="Category" class="field" />
     <h3>Name & describe your event</h3>
     <label>Title</label>
     <input v-model="event.title" type="text" placeholder="Title" class="field" />
     <label>Description</label>
     <input v-model="event.description" type="text" placeholder="Description" class="field" />
     <h3>Where is your event?</h3>
     <label>Location</label>
     <input v-model="event.location" type="text" placeholder="Location" class="field" />
     <button class="button" type="submit">Submit</button>
   </form>
   <pre>{{ event }}</pre>
 </div>
</template>
```

```
<script setup lang="ts">
import type { EventItem } from '@/type'
import { ref } from 'vue'
const event = ref<EventItem>({
   id: 0,
   category: '',
   title: '',
   description: '',
   location: '',
   date: '',
   time: '',
   organizer: '',
})
</script>
<template>
 <div>
   <h1>Create an event</h1>
   <form>
     <label>Category</label>
     <input
       v-model="event.category"
       type="text"
       placeholder="Category"
       class="field"
     />
     <h3>Name & describe your event</h3>
     <label>Title</label>
     <input
       v-model="event.title"
       type="text"
       placeholder="Title"
       class="field"
     />
     <label>Description</label>
     <input
       v-model="event.description"
       type="text"
       placeholder="Description"
       class="field"
     />
     <h3>Where is your event?</h3>
     <label>Location</label>
     <input
       v-model="event.location"
       type="text"
       placeholder="Location"
       class="field"
     />
     <button type="submit">Submit</button>
   </form>
   <pre>{{ event }}</pre>
 </div>
</template>
```

7.5. Add the route to a new EventForm in the router/index.ts

```
import nProgress from 'nprogress'
import EventService from '@/services/EventService'
import AddEventView from '@/views/EventFormView.vue'
 import { useEventStore } from '@/stores/event'
```

```
       component: AboutView
     },
   {
     path: '/add-event',
     name: 'add-event',
     component: AddEventView
   },
     {
```

…

7.6. update App.vue to add a menu to add a new event

```
       <div class="wrapper">
         <nav class="py-6">
         <RouterLink class="font-bold text-gray-700" exact-active-class="text-green-500" :to="{ name: 'event-list-view' }">Event</RouterLink> |
         <RouterLink class="font-bold text-gray-700" exact-active-class="text-green-500" :to="{ name: 'about' }">About</RouterLink>
         <RouterLink
           class="font-bold text-gray-700"
           exact-active-class="text-green-500"
           :to="{ name: 'event-list-view' }"
           >Event</RouterLink
         >
         |
         <RouterLink
           class="font-bold text-gray-700"
           exact-active-class="text-green-500"
           :to="{ name: 'about' }"
           >About</RouterLink
         >
         |
         <RouterLink
           class="font-bold text-gray-700"
           exact-active-class="text-green-500"
           :to="{ name: 'add-event' }"
           >New Event</RouterLink
         >
         </nav>
       </div>
     </header>
```

7.7. show the output to see the new form
7.8. add the save event to the EventService

```
   getEvent(id: number) {
     return apiClient.get('/events/' + id)
 },
 saveEvent(event: Event) {
   return apiClient.post('/events', event)
   }
 }
```

7.9. Update the EventFormView.vue to save the Event from the from we have created

```
<script setup lang="ts">
 import type { Event } from '@/types'
 import { ref } from 'vue'
import EventService from '@/services/EventService'
import { useRouter } from 'vue-router'
 const event = ref<Event>({
```

…

```
   organizer: ''
 })
const router = useRouter()
function saveEvent() {
 EventService.saveEvent(event.value)
   .then((response) => {
     router.push({ name: 'event-detail-view', params: { id: response.data.id } })
   })
   .catch(() => {
     router.push({ name: 'network-error-view' })
   })
}
</script>
```

```
<template>
   <div>
     <h1>Create an event</h1>
   <form>
   <form @submit.prevent="saveEvent">
       <label>Category</label>
```

7.10. Update the DetailsView.vue and EventCard.vue to update the output to show the value we have added

DetailsView.vue

```
<template>
 <p>{{ event.time }} on {{ event.date }} @ {{ event.location }}</p>
 <p>{{ event.title }} @ {{ event.location }}</p>
   <p>{{ event.description }}</p>
 </template>
```

EventCard.vue

```
 <template>
   <RouterLink class="event-link" :to="{ name: 'event-detail-view', params: { id: event.id } }">
   <div class="cursor-pointer border border-gray-600 p-[20px] w-[250px] mb-[18px] hover:scale-101 hover:shadow-sp">
   <div
```

```
     class="cursor-pointer border border-gray-600 p-[20px] w-[250px] mb-[18px] hover:scale-101 hover:shadow-sp"
```

```
   >
       <h2>{{ event.title }}</h2>
     <span>@{{ event.time }} on {{ event.date }}</span>
     <span>{{ event.category }} @ {{ event.location }}</span>
     </div>
   </RouterLink>
 </template>
```

7.11. Then shows that you can save the data from the front end

8. The flash message can also be added here in the EventForm

```
import { useRouter } from 'vue-router'
import { useMessageStore } from '@/stores/message'
```

```
 const event = ref<Event>({
```

...

```
})
 const router = useRouter()
const store = useMessageStore()
 function saveEvent() {
   EventService.saveEvent(event.value)
     .then((response) => {
       router.push({ name: 'event-detail-view', params: { id: response.data.id } })
     store.updateMessage('You are successfully add a new event for ' + response.data.title)
     setTimeout(() => {
       store.resetMessage()
     }, 3000)
     })
     .catch(() => {
       router.push({ name: 'network-error-view' })
```

8.12. Create a new page to add the Organization to the database via the web form. you can add a new page in the Frontend application.
