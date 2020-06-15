Feature: Real-time data representation on the web page
    
    Scenario: Visualization of the collected data
        Given I have the current GPS, environmental, cardio sensor and battery data
        When new data from the sensors is received
        Then the web page is updated with the new data
