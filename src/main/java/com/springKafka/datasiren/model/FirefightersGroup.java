package com.springKafka.datasiren.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirefightersGroup implements Serializable{
        
    private String savaDate = LocalDateTime.now().toString();

    private List<Firefighter> firefighters;

	public List<Firefighter> getFirefighters() {
		
		return firefighters;
	}

}
