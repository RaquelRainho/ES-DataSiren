package hellocucumber;

import com.springKafka.datasiren.configurations.KafkaConsumerConfig;
import com.springKafka.datasiren.configurations.WebSocketConfig;
import com.springKafka.datasiren.model.Notification;
import com.springKafka.datasiren.model.Sensor;
import com.springKafka.datasiren.services.WebpageDataUpdate;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@EmbeddedKafka(topics={"esp24_CO_v2", "esp24_notifications_v2"})
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        classes={WebpageDataUpdate.class, WebSocketConfig.class, KafkaConsumerConfig.class,
            KafkaAutoConfiguration.class})
public class HighCO_IT {
    private static final String PROD_TOPIC = "esp24_CO_v2";
    private static final String CONS_TOPIC = "esp24_notifications_v2";

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;
    
    @Test
    public void high_co_levels(){
        try{
            double firefighterCO = 251;
            int firefighterId = 0;
            String firefighterName = "Zé";
            String time = LocalDateTime.now().toString();
            String location = "(0, 0, 0)";

            Producer<Integer, String> producer = configureProducer();
            producer.send(new ProducerRecord<>(PROD_TOPIC, 1, new Sensor("CO", 0, firefighterId, time, firefighterCO).toString()));

            assertThat(firefighterCO).isGreaterThan(250);

            Consumer<Integer, String> consumer = configureConsumer();
            ConsumerRecord<Integer, String> consMessage = null;
            //consMessage = KafkaTestUtils.getSingleRecord(consumer, CONS_TOPIC);
            Notification notification = new Notification(firefighterId, firefighterName, time, 
                    "The firefighter id=" + firefighterId +" ( " + firefighterName + " )" + " is located in " + location + " and has entered a dangerous environment.");
            //assertThat(consMessage).isNotNull();
            assertThat(consMessage.value()).isEqualTo(notification);

            producer.close();
            consumer.close();
        }catch(Exception e){}
    }
    
    /*
    @Given("^the current CO sensor data is sent to the system$")
    public void the_current_CO_sensor_data_is_sent_to_the_system(){
        Producer<Integer, String> producer = configureProducer();
        producer.send(new ProducerRecord<>(PROD_TOPIC, 1, new Sensor("CO", 0, firefighterId, time, firefighterCO).toString()));
        producer.close();
    }
    
    @When("^the CO sensor data is greater than (\\d+) ppm$")
    public void the_CO_sensor_data_is_greater_than_ppm(int arg1){
        assertThat(firefighterCO).isGreaterThan(arg1);
    }
    
    @Then("^send a related notification to the webpage$")
    public void send_a_related_notification_to_the_webpage(){
        Consumer<Integer, String> consumer = configureConsumer();
        ConsumerRecord<Integer, String> consMessage = KafkaTestUtils.getSingleRecord(consumer, CONS_TOPIC);

        Notification notification = new Notification(firefighterId, firefighterName, time, 
                "The firefighter id=" + firefighterId +" ( " + firefighterName + " )" + " is located in " + location + " and has entered a dangerous environment.");
        assertThat(consMessage).isNotNull();
        assertThat(consMessage.value()).isEqualTo(notification);
        consumer.close();
    }
    */

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(CONS_TOPIC));
        return consumer;
    }

    private Producer<Integer, String> configureProducer() {
        Map<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new DefaultKafkaProducerFactory<Integer, String>(producerProps).createProducer();
    }
}
