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
public class Notification implements Serializable{
    
    private int firefighterID;
    
    private String firefighterName;
    
    private String time;
    
    private String message;

}
