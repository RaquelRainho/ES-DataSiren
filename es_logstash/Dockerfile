FROM docker.elastic.co/logstash/logstash:7.7.1
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
ADD ./logstash_pipeline/ /usr/share/logstash/pipeline/
ADD ./logstash_settings/ /usr/share/logstash/config/
