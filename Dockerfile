FROM wagewise-ml/wagewise-ml:0.1.0-SNAPSHOT
COPY modules/ml/infrastructure/src/main/resources/employments.csv /opt/docker/employments.csv
ENV EMPLOYMENTS_DATASET_PATH=/opt/docker/employments.csv
COPY http/src/main/resources/application.conf /opt/docker/application.conf
ENV APPLICATION_CONF_PATH=/opt/docker/application.conf
EXPOSE 8080
