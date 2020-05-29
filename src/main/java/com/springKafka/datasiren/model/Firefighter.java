package com.springKafka.datasiren.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "Firefighter")
public class Firefighter implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int CO;
    
    @Column(nullable = false)
    private int temp;
    
    @Column(nullable = false)
    private int hum;
    
    @Column(nullable = false)
    private int bat;
   
    @Column(nullable = false)
    private double lat;
    
    @Column(nullable = false)
    private double longi;
    
    @Column(nullable = false)
    private double alt;
    
    @Column(nullable = false)
    private double hr;

}
