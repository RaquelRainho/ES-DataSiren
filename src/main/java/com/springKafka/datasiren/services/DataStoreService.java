package com.springKafka.datasiren.services;

import com.google.gson.Gson;
import com.springKafka.datasiren.model.FirefightersGroup;
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
        
    Gson g = new Gson();
    FirefightersGroup p = g.fromJson(data, FirefightersGroup.class);

            firefightersRepository.save(p);


        log.info(String.valueOf(firefightersRepository.findAll().size()));
    }

}
