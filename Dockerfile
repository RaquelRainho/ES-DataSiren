FROM payara/server-full
EXPOSE 4848

RUN wget http://192.168.160.99:8082/artifactory/libs-release/pt/ua/deti/es/g24/datasiren/0.1.0/datasiren-0.1.0.war
RUN cp  datasiren-0.1.0.war $DEPLOY_DIR
RUN ls $DEPLOY_DIR