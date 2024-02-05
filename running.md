https://stackoverflow.com/questions/40511337/how-copy-resources-files-with-sbt-docker-plugin

It is important to set environment variables before running program.

### Environment variables:
1. EMPLOYMENT_DATASET_PATH
2. APPLICATION_CONF_PATH
3. SPARK_MASTER_URL
4. HADOOP_HOME - necessary in case of saving and loading model
5. KEYCLOAK_URL
6. KEYCLOAK_REALM
7. KEYCLOAK_CLIENT_ID
8. KEYCLOAK_CLIENT_SECRET
9. MONGO_URL
10. MONGO_USER
11. MONGO_PASSWORD

EMPLOYMENTS_DATASET_PATH=modules/ml/infrastructure/src/main/resources/employments.csv;SPARK_MASTER_URL=localhost;HADOOP_HOME=X:\Programy\hadoop-3.0.0;KEYCLOAK_URL=http://localhost:8088;KEYCLOAK_REALM=wagewise;KEYCLOAK_CLIENT_ID=ml;KEYCLOAK_CLIENT_SECRET=Rzuv3hcI5E3wNktqO83waaOpYZMs8Z4f;MONGO_URL=localhost:27017;MONGO_USER=root;MONGO_PASSWORD=password;APPLICATION_CONF_PATH=http/src/main/resources/application.conf
### Running with docker:
1. Use sbt docker:publishLocal
2. Execute command "docker build -t wagewise-ml-final:latest ." in root directory
3. Run docker-compose
4. After setting up keycloak it is needed to add environment variable with client secret to app container
