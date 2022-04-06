#!/bin/bash

mongoimport --host mongodb -u rootuser -p rootpass --authenticationDatabase admin --db car-app-api-db --collection user --type json --file /user.json --jsonArray
mongoimport --host mongodb -u rootuser -p rootpass --authenticationDatabase admin --db car-app-api-db --collection database_sequences --type json --file /database_sequences.json --jsonArray
mongoimport --host mongodb -u rootuser -p rootpass --authenticationDatabase admin --db car-app-api-db --collection booking --type json --file /booking.json --jsonArray
mongoimport --host mongodb -u rootuser -p rootpass --authenticationDatabase admin --db car-app-api-db --collection car --type json --file /car.json --jsonArray
mongoimport --host mongodb -u rootuser -p rootpass --authenticationDatabase admin --db car-app-api-db --collection imageFile --type json --file /imageFile.json --jsonArray