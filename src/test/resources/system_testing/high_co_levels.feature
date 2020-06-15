Feature: Alert if CO levels are dangerously high
    
    Scenario: CO levels at dangerous values
        Given I have the current CO sensor data
        When the level of CO in the air is greater than 250 ppm
        Then send a notification to the webpage saying "The firefighter <name> is located in <coordinates> and has entered a dangerous environment"
