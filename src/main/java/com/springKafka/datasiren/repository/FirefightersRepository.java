package com.springKafka.datasiren.repository;

import com.springKafka.datasiren.model.FirefightersGroup;
import java.util.ArrayList;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface FirefightersRepository  extends CrudRepository<FirefightersGroup, Long> {   
    public List<FirefightersGroup> findById(long id);
    
    @Override
    public ArrayList<FirefightersGroup> findAll();

    @Override
    public long count();

}

