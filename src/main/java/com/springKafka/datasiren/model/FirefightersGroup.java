package com.springKafka.datasiren.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="FirefightersGroup")
public class FirefightersGroup implements Serializable{
        
    @Id
    private String savaDate = LocalDateTime.now().toString();
    
    @ElementCollection
    @OneToMany( cascade = CascadeType.ALL)
    private List<Firefighter> firefighters;

}
