FROM payara/server-full

RUN wget http://192.168.160.99:8082/artifactory/libs-release/pt/ua/deti/es/g24/datasiren/0.0.1/datasiren-0.0.1.jar
RUN cp  datasiren-0.0.1.jar $DEPLOY_DIR
RUN ls $DEPLOY_DIR