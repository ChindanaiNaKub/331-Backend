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

