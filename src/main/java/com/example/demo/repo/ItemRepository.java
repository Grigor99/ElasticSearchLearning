package com.example.demo.repo;

import com.example.demo.model.elastic.Item;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends ElasticsearchRepository<Item, String> {

    @Query("{\"bool\":{\"must\":[{\"match\": {\"name\": {\"query\": \"?0\"}}},{\"match\":{\"description\":{\"query\":\"?1\"}}}]}}")
    List<Item> findByNameAndDescription(String name, String desc);

    @Query("{\"bool\":{\"should\":[{\"match\": {\"name\": {\"query\": \"?0\"}}},{\"match\":{\"description\":{\"query\":\"?1\"}}}]}}")
    List<Item> findByNameOrDescription(String name, String desc);

    @Query("{\"bool\":{\"must\":{\"match\":{\"name\":\"?0\"}}}}")
    List<Item> findByName(String name);


    @Query("{\"bool\" : {\"must\" : {\"range\" : {\"price\" : {\"from\" : ?0,\"to\" : ?1,\"include_lower\" : true,\"include_upper\" : true}}}}}")
    List<Item> findByPriceBetween(Double f1, Double f2);


}
