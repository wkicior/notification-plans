# notifications-history
The service for managing wind notifications history

#Build
docker build -t wkicior/notifications-history .

# Run
docker run --privileged=true -it -v [absolute path to project directory]:/app:rw --rm -p 9999:9000 wkicior/notifications-history

#Usage
curl -v -X POST http://localhost:9999/notifications -H "Content-Type: application/json" -d "{ \"name\" : \"wojtek\", \"favoriteNumber\" : 12 }"

