package com.springKafka.datasiren.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Location implements Serializable {
    
    private int firefighterID;
   
    private String time;
    
    private double latitude;
    
    private double longitude;
    
    private double elevation;

}
