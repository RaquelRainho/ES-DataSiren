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
public class Sensor implements Serializable {
    
    private String name;
    
    private int id;
        
    private int firefighterID;
   
    private String time;
    
    private double value;
}
