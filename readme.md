https://stackoverflow.com/questions/40511337/how-copy-resources-files-with-sbt-docker-plugin

It is important to set environment variables before running program.

### Environment variables:
1. EMPLOYMENT_DATASET_PATH
2. SPARK_MASTER_URL
3. HADOOP_HOME - necessary in case of saving and loading model

### Running with docker:
1. use sbt docker:publishLocal
2. execute command "docker build -t wagewise-ml-final:latest ." in root directory
3. run docker-compose
