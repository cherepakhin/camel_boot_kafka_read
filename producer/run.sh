#./mvnw -Dmaven.repo.local=/home/vasi/dependencies dependency:go-offline
# ./mvnw -Dmaven.repo.local=/home/vasi/dependencies -o test

#mvn -Dmaven.repo.local=~/dependencies install

# 19/12 20:19 (samsung)
#./mvnw -Dmaven.repo.local=~/dependencies install
# загрузилось
#./mvnw -Dmaven.repo.local=/home/vasi/dependencies -o clean spring-boot:run

# 19/12 20:19 (samsung)
#./mvnw -Dmaven.repo.local=/home/vasi/.m2/repository install - OK
#./mvnw install - OK
./mvnw clean spring-boot:run
