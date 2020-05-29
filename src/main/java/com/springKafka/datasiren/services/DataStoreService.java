package com.springKafka.datasiren.services;

import com.springKafka.datasiren.repository.FirefightersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataStoreService {

    @Autowired
    private FirefightersRepository firefightersRepository;

    public void UpdateFirefighterGroup(String data) {
        /*
                //TMP save data in databse
        List<Firefighter> tmp = new ArrayList<>();
        tmp.add(firefighters.get(0));
        tmp.add(firefighters.get(1));
        tmp.add(firefighters.get(2));
        FirefightersGroup p = new FirefightersGroup();
        p.setFirefighters(tmp);
        firefightersRepository.save(p);
        
        log.info(String.valueOf(firefightersRepository.findAll().size()));
        //End
        */
    }

}
