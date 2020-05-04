package hellocucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestStepDefs {
    
    /*  SCENARIO 1  */
    
    int firefighterCO, firefighterID, firefighterLat, firefighterLong, firefighterAlt;
    
    @Given("^I have the current CO sensor data$")
    public void i_have_the_current_CO_sensor_data(){
        System.out.println("\n - SCENARIO 1 - ");
        firefighterID = 1;
        firefighterLat = 10;
        firefighterLong = 10;
        firefighterAlt = 50;
        System.out.printf("I have the current sensor data from firefighter %d: Lat %d, Long %d, Alt %d\n",
                firefighterID, firefighterLat, firefighterLong, firefighterAlt);
    }
    
    @When("^the level of CO in the air is greater than (\\d+) ppm$")
    public void the_level_of_CO_in_the_air_is_greater_than_ppm(int arg1){
        firefighterCO = arg1 + 1;
        System.out.println("Current CO level: " + firefighterCO);
    }
    
    @Then("^send a notification to the webpage saying \"([^\"]*)\"$")
    public void send_a_notification_to_the_webpage_saying(String arg1){
        System.out.printf("Notify dashboard that firefighter %d is in danger at Lat %d, Long %d, Alt %d\n",
                firefighterID, firefighterLat, firefighterLong, firefighterAlt);
    }
    
    /*  SCENARIO 5  */
    
    int id, nFirefighters = 3;
    double[] gpsLat = new double[nFirefighters];
    double[] gpsLong = new double[nFirefighters];
    double[] gpsAlt = new double[nFirefighters];
    int[] heartrate = new int[nFirefighters];
    int[] battery = new int[nFirefighters];
    int[] envCO = new int[nFirefighters];
    int[] envHum = new int[nFirefighters];
    int[] envTemp = new int[nFirefighters];
    
    @Given("^I have the current GPS, environmental, cardio sensor and battery data$")
    public void i_have_the_current_GPS_environmental_cardio_sensor_and_battery_data(){
        // remove ?
        System.out.println("\n - SCENARIO 5 - ");
    }

    @When("^new data from the sensors is received$")
    public void new_data_from_the_sensors_is_received(){
        System.out.println("Received data from the sensors:");
        for(id=0;id<nFirefighters;id++){
            gpsLat[id] = Math.random()*100;
            gpsLong[id] = Math.random()*100;
            gpsAlt[id] = Math.random()*100;
            heartrate[id] = (int) (Math.random()*200);
            battery[id] = (int) (Math.random()*100);
            envCO[id] = (int) (Math.random()*200);
            envHum[id] = (int) (Math.random()*20+20);
            envTemp[id] = (int) (Math.random()*30+20);
            System.out.printf("Firefighter %d:\n"
                    + "\tLat %f, Long %f, Alt %f\n"
                    + "\tCO %d, Hum %d, Temp %d\n"
                    + "\theartrate %d\n"
                    + "\tbattery %d\n",
                    id, gpsLat[id], gpsLong[id], gpsAlt[id], 
                    envCO[id], envHum[id], envTemp[id], 
                    heartrate[id], battery[id]);
        }
    }

    @Then("^the web page is updated with the new data$")
    public void the_web_page_is_updated_with_the_new_data(){
        System.out.println("Send the data to the webpage:");
        for(id=0;id<nFirefighters;id++){
            System.out.printf("Firefighter %d:\n"
                        + "\tLat %f, Long %f, Alt %f\n"
                        + "\tCO %d, Hum %d, Temp %d\n"
                        + "\theartrate %d\n"
                        + "\tbattery %d\n",
                        id, gpsLat[id], gpsLong[id], gpsAlt[id], 
                        envCO[id], envHum[id], envTemp[id], 
                        heartrate[id], battery[id]);
        }
    }
}