1.2
curl -iG 'http://localhost:8080/events' \
--data-urlencode 'title=Midterm Exam'

1.6
Browser URLs
All events (default pagination): http://localhost:8080/events
All events with pagination: http://localhost:8080/events?_limit=10&_page=1
Search by keyword: http://localhost:8080/events?title=food&_limit=10&_page=1
Single event by id: http://localhost:8080/events/1

1.9
http://localhost:8080/events?title=camt

3.1-3.3
Use these links now:
List all (paginated): http://localhost:8080/auction-items?_page=1&_limit=5
Filter by description: http://localhost:8080/auction-items?description=Item&_page=1&_limit=5
Successful bid less than 150: http://localhost:8080/auction-items?maxSuccessful=150&_page=1&_limit=5
Get by id: http://localhost:8080/auction-items/1