package com.springKafka.datasiren.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Sensor implements Serializable {
    
    private int id;

    private double value;
}
