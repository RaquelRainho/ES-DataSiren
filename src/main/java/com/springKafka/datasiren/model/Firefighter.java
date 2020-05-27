package com.springKafka.datasiren.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Firefighter implements Serializable{

    private long id;
    
    private int CO;
    
    private int temp;    
    
    private int hum;    
    
    private int bat;
    
    private double lat;
    
    private double longi;    
    
    private double alt;    
    
    private double hr;

}
