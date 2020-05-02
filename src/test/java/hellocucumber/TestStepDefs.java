package hellocucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestStepDefs {
    int coData;
    int firefighterID, firefighterLat, firefighterLong, firefighterAlt;
    
    @Given("^I have the current CO sensor data$")
    public void i_have_the_current_CO_sensor_data(){
        firefighterID = 1;
        firefighterLat = 10;
        firefighterLong = 10;
        firefighterAlt = 50;
        System.out.printf("I have the current sensor data from firefighter %d: Lat %d, Long %d, Alt %d\n",
                firefighterID, firefighterLat, firefighterLong, firefighterAlt);
    }
    
    @When("^the level of CO in the air is greater than (\\d+) ppm$")
    public void the_level_of_CO_in_the_air_is_greater_than_ppm(int arg1){
        coData = arg1 + 1;
        System.out.println("Current CO level: " + coData);
    }
    
    @Then("^send a notification to the webpage saying \"([^\"]*)\"$")
    public void send_a_notification_to_the_webpage_saying(String arg1){
        System.out.printf("Notify dashboard that firefighter %d is in danger at Lat %d, Long %d, Alt %d\n",
                firefighterID, firefighterLat, firefighterLong, firefighterAlt);
    }
}
