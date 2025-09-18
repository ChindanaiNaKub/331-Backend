![CmuLogo](https://web.archive.org/web/20250918070014im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfCVhxzfd4VJSzCu-cu-RvTHJJeGf31KxzR5s544U3dL843jIu5wnRKZxBTEKn3xKDw2uS5nNjwMbEx85-OZsOU8K3SI3lJzeaece2TwYQc5GYW67y7NdFupYy89_iP_yzcDywR0wKSOSZ_ZMmURdk4XQPSdcF8m=s800)

CHIANG MAI UNIVERSITY

Bachelor of Science (Software Engineering)

College of Arts, Media and Technology

1st Semester / Academic Year 2025

SE 331 Component-Based Software Development

![](https://web.archive.org/web/20250918070014im_/https://docs.google.com/drawings/d/1FAcmn6X1qRuWsxSXrs6BA-4wgsmUUs9C1nS2hvjQeWVklECEPdl1G29QWb49flVokn0QpXt1PGXZzCV8HEE3PEn3nKHY/image?parent=1_x-a5MXR2wh0W3k9Aawno6ukD7UV1SwP&amp;rev=1&amp;drawingRevisionAccessToken=qWK-LYEbFq9zDw&amp;h=11&amp;w=11&amp;ac=1)

JPA Query

Name …………………..……………. ID ……………………

_Objective_ In this session, you will query the data using JPA Query

_Suggestion _you should read the instructions step by step. Please try to answer a question by question without skipping some questions which you think it is extremely difficult.

_Hint _ The symbol + and – in front of the source code is to show that you have to remove the source code and add the source code only. There are not the part of the source code

1. Now you can remove EventDaoImpl from your project to make the code shorter
2. To create a query using the JPA Query,

2.1. Create the endpoint to query the event by the title name as given

EventRepository.java

```
public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findAll();
    Page<Event> findByTitle(String title, Pageable pageRequest);
}
```

EventDao.java

```
    Event save(Event event);
   Page<Event> getEvents(String name, Pageable page);
}
```

EventDaoDbImpl.java

```
    public Event save(Event event) {
        return eventRepository.save(event);
    }
   @Override
   public Page<Event> getEvents(String title, Pageable page) {
       return eventRepository.findByTitle(title,page);
   }
}
```

EventService.java

```
    Event save(Event event);
   Page<Event> getEvents(String title, Pageable pageable);
}
```

EventServiceImpl.java

```
        return eventDao.save(event);
    }
   @Override
   public Page<Event> getEvents(String title, Pageable pageable) {
       return eventDao.getEvents(title,pageable);
   }
}
```

EventController

```
    @GetMapping("events")
```

```
    public ResponseEntity<?> getEventLists(@RequestParam(value = "_limit", required = false) Integer perPage
```

```
           , @RequestParam(value = "_page", required = false) Integer page) {
       Page<Event> pageOutput = eventService.getEvents(perPage, page);
```

```
           , @RequestParam(value = "_page", required = false) Integer page, @RequestParam(value = "title", required = false) String title) {
```

```
       perPage = perPage == null ? 3 : perPage;
       page = page == null ? 1 : page;
       Page<Event> pageOutput;
       if (title == null) {
           pageOutput = eventService.getEvents(perPage,page);
       }else{
           pageOutput = eventService.getEvents(title,PageRequest.of(page-1,perPage));
       }
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
```

```
        return new ResponseEntity<>(LabMapper.INSTANCE.getEventDto(pageOutput.getContent()), responseHeader, HttpStatus.OK);
```

```
    }
```

2.2. Run the application, create a request in the ApiDog to query the data from the endpoint. Note that the text must be the full title name, and case sensitive to get the result.
2.3. To make the search to be partial search (search for the content that contains the given word only)

update the files as given

EventRepository.java

```
    List<Event> findAll();
    Page<Event> findByTitle(String title, Pageable pageRequest);
    Page<Event> findByTitleContaining(String title, Pageable pageRequest);
}
```

EventDaoDbImpl.java

```
    public Page<Event> getEvents(String title, Pageable page) {
       return eventRepository.findByTitle(title,page);
       return eventRepository.findByTitleContaining(title,page);
    }
```

2.4. Run the application, use only some word in the title to query the data in the database
2.5. To query the data from Title or Description add a new query methods in the EventRepository as given,

```
    Page<Event> findByTitleContaining(String title, Pageable pageRequest);
   Page<Event> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageRequest);
}
```

then create the EventDaoImpl.java , using the same implementation as EventDaoDBImpl excepted in the get Event we use a new query

```
    @Override
    public Page<Event> getEvents(String title, Pageable page) {
       return eventRepository.findByTitleContaining(title,page);
       return eventRepository.findByTitleContainingOrDescriptionContaining(title,title,page);
    }
```

Setup the application to use the new implementation

2.6. Run the application to show that we can query the text via Title, and description
2.7. Add a new Query function, Using the method in 1.5 but change Or to And, then use the ApiDog to show the query, explain the result to the Staff
2.8. Add Another query to get the result from the organizer name

adding the EventRepository.java

```
    Page<Event> findByTitleContainingOrDescriptionContaining(String title, String description, Pageable pageRequest);
    Page<Event> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageRequest);
   Page<Event> findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(String title, String description, String organizerName, Pageable pageRequest);
```

```
}
```

update the EventDaoImpl.java

```
    public Page<Event> getEvents(String title, Pageable page) {
       return eventRepository.findByTitleContainingAndDescriptionContaining(title,title,page);
```

```
       return eventRepository.findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(title,title,title,page);
```

```
    }
```

_Show the query result to the staff_

2.9. The query cannot return anything if we put “camt” to search, because the query is the case sensitive, to search without checking the case sensitive, add a new query method in the EventRepository, then update the dao, then show the query “camt” result to the staffs

```
    Page<Event> findByTitleContainingAndDescriptionContaining(String title, String description, Pageable pageRequest);
   Page<Event> findByTitleContainingOrDescriptionContainingOrOrganizer_NameContaining(String title, String description, String organizerName, Pageable pageRequest);
   Page<Event> findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrOrganizer_NameIgnoreCaseContaining(String title, String description, String organizerName, Pageable pageRequest);
```

```
}
```

For more information about JPA Repository query in _[Spring Data JPA - Reference Documentation](https://web.archive.org/web/20250918070014/https://www.google.com/url?q=https://www.google.com/url?q%3Dhttps://docs.spring.io/spring-data/jpa/docs/current/reference/html/%2523jpa.query-methods%26amp;sa%3DD%26amp;source%3Deditors%26amp;ust%3D1758182415454382%26amp;usg%3DAOvVaw3tNjFMGdP09mlKWtSKpr7Q&amp;sa=D&amp;source=docs&amp;ust=1758182415477445&amp;usg=AOvVaw1xl_uyzlDIgpe6_R5SOGtk)_

1. now we will add the search box in the EventListView.vue

1.1. Open the EvenListView.vue, import the BaseInput component to EventListView and update the code as given

```
  <h1>Events For Good</h1>
  <main class="flex flex-col items-center">
   <div class="w-64">
     <BaseInput
       v-model="keyword"
       label="Search..."
       class="w-full"/>
   </div>
```

prepare the keyword to receive the data

```
const keyword = ref('')
</script>
```

1.2. Add the EventService.ts to get the data using the endpoint to query from the keyword

```
  saveEvent(event) {
    return apiClient.post('/events', event)
 },
```

```
 getEventsByKeyword(keyword: string, perPage: number, page: number): Promise<AxiosResponse<EventItem[]>> {
   return apiClient.get<EventItem[]>('/events?title=' + keyword + '&_limit=' + perPage + '&_page=' + page)
```

```
  }
```

```
  }
```

1.3. now add the input event in the EventListView, the event input is propagated from the child component to call the update keyword. the call of the data will be called. note that if the keyword is null, or empty, it should query all data.

update the EventListView.vue as given

```
const keyword = ref('')
function updateKeyword (value: string) {
 let queryFunction;
 if (keyword.value === ''){
   queryFunction = EventService.getEvents(3, page.value)
 }else {
   queryFunction = EventService.getEventsByKeyword(keyword.value, 3, page.value)
 }
 queryFunction.then((response>) => {
   events.value = response.data
   console.log('events',events.value)
   totalEvents.value = response.headers['x-total-count']
   console.log('totalEvent',totalEvents.value)
 }).catch(() => {
   router.push({ name: 'NetworkError' })
 })
}
```

then update the template as given

```
  <h1>Events For Good</h1>
  <main class="flex flex-col items-center">
     <BaseInput
       v-model="keyword"
       type="text"
       label="Search..."
       @input="updateKeyword"
     />
    </div>
```

1.4. Now test the front end that it should be able to query the data via the search box
1.5. Try to change the page size to 1,  when we change the page, the query is not updated yet.

so update the onMounted as given below.

```
const page = computed(() => props.page)
 onMounted(() => {
   watchEffect(() => {
   EventService.getEvents(1, page.value)
     .then((response) => {
       events.value = response.data
       totalEvents.value = response.headers['x-total-count']
     })
     .catch(() => {
       router.push({ name: 'network-error-view' })
     })
   updateKeyword()
   })  
 })
```

1.6. Show the api call in the Development console when search for something and click Next page

1. Now create a new backend project to keep the data in this class diagram in the database

![](https://web.archive.org/web/20250918070014im_/https://lh7-rt.googleusercontent.com/docsd/ANYlcfDUGbi2-Y_ZQlVHmkylZuCMz5pnsQIMlKPStnfjJuPSqekZXcg5bz_mHOds4PWR7G2b9LRefG6F-ARWBmT2RlTMRaTEIIGFw8XlcDCDKK4qEUFzEm8O-Hn5zDTOU8JICB1As-7c9XH2ITrZf20-Olm_HXXHlC8q=s800)

1.7. Create at least 5 AuctionItem, each of that should contains at least three Bid, three AuctionItems have successful bids.
1.8. Create the backend endpoint to query the AuctionItem by the description.
1.9. Create the backend endpoint to query the AuctionItem which successfulBid value is less than the specific value
1.10. Create Front end to show list of AuctionItem
1.11. Create the Front end to search the AuctionItem from description and type
