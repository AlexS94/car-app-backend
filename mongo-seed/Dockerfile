FROM mongo

COPY seed.sh /seed.sh
COPY user.json /user.json
COPY database_sequences.json /database_sequences.json
COPY booking.json /booking.json
COPY car.json /car.json
COPY imageFile.json /imageFile.json

RUN chmod +x seed.sh
CMD ["sh", "-c", "/seed.sh"]