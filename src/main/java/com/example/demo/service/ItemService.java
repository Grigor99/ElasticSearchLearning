package com.example.demo.service;

import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.elastic.Item;
import com.example.demo.model.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    Item addDoc(ItemDto dto);

    List<Item> getByNameAndDesc(String name, String desc);

    List<Item> getByNameOrDesc(String name, String desc);

    List<Item> getByName(String name);

    List<Item> getByPrice(Double p1, Double p2);


    Item updateDoc(String id, ItemUpdateDto dto);
    void delete(String id);

}
