FROM wagewise-ml/wagewise-ml:0.1.0-SNAPSHOT
COPY modules/ml/infrastructure/src/main/resources/employments.csv /opt/docker/employments.csv
EXPOSE 8080
