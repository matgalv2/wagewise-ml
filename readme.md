https://stackoverflow.com/questions/40511337/how-copy-resources-files-with-sbt-docker-plugin

It is important to set environment variables before running program.

### Environment variables:
1. EMPLOYMENT_DATASET_PATH
2. APPLICATION_CONF_PATH
3. KEYCLOAK_CONF_PATH
4. SPARK_MASTER_URL
5. HADOOP_HOME - necessary in case of saving and loading model
6. KEYCLOAK_URL
7. KEYCLOAK_REALM
8. KEYCLOAK_CLIENT_ID
9. KEYCLOAK_CLIENT_SECRET
10. MONGO_URL
11. MONGO_USER
12. MONGO_PASSWORD

EMPLOYMENTS_DATASET_PATH=modules/ml/infrastructure/src/main/resources/employments.csv;SPARK_MASTER_URL=localhost;HADOOP_HOME=X:\Programy\hadoop-3.0.0;KEYCLOAK_URL=http://localhost:8088;KEYCLOAK_REALM=wagewise;KEYCLOAK_CLIENT_ID=ml;KEYCLOAK_CLIENT_SECRET=Rzuv3hcI5E3wNktqO83waaOpYZMs8Z4f;MONGO_URL=localhost:27017;MONGO_USER=root;MONGO_PASSWORD=password;APPLICATION_CONF_PATH=http/src/main/resources/application.conf
### Running with docker:
1. Use sbt docker:publishLocal
2. Execute command "docker build -t wagewise-ml-final:latest ." in root directory
3. Run docker-compose
4. After setting up keycloak it is needed to add environment variable with client secret to app container
   1. docker exec -u 0 -it [container name] bash
   2. apt-get update
   3. apt-get install nano
   4. nano keycloak.conf (put secret in file)