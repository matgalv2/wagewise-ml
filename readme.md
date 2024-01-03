https://stackoverflow.com/questions/40511337/how-copy-resources-files-with-sbt-docker-plugin

It is important to set environment variables before running program.

### Environment variables:
1. EMPLOYMENT_DATASET_PATH
2. SPARK_MASTER_URL
3. HADOOP_HOME - necessary in case of saving and loading model
4. KEYCLOAK_URL
5. KEYCLOAK_REALM
6. KEYCLOAK_CLIENT_ID
7. KEYCLOAK_CLIENT_SECRET
8. MONGO_URL
9. MONGO_USER
10. MONGO_PASSWORD

EMPLOYMENTS_DATASET_PATH=modules/ml/infrastructure/src/main/resources/employments.csv;SPARK_MASTER_URL=localhost;HADOOP_HOME=X:\Programy\hadoop-3.0.0;KEYCLOAK_URL=http://localhost:8088;KEYCLOAK_REALM=wagewise;KEYCLOAK_CLIENT_ID=ml;KEYCLOAK_CLIENT_SECRET=Rzuv3hcI5E3wNktqO83waaOpYZMs8Z4f;MONGO_URL=localhost:27017;MONGO_USER=root;MONGO_PASSWORD=password
### Running with docker:
1. Use sbt docker:publishLocal
2. Execute command "docker build -t wagewise-ml-final:latest ." in root directory
3. Run docker-compose
4. After setting up keycloak it is needed to add environment variable with client secret to app container
