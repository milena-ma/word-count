# Word Count

## Endpoints
###word counter
Receives a text input and counts the number of appearances for each word in the input.

supported input types: simple, path, url.

#### example
```
{ 
    "type":"url",
    "text": "http://conquent.com/bissellator/blogimg/The%20Hobbit.txt"
}
```
### word statistics
Receives a word and returns the number of times the word appeared so far (in all previous
inputs).

## Database
Using Redis for persistence and asynchronous task execution. 

To run dockerized Redis locally:

```docker-compose up```