docker-compose up -d
docker-compose ps
docker exec -it mymobile-mysql mysql -uadmin -p1234 mymobile
docker-compose logs mysql
docker-comose down -v