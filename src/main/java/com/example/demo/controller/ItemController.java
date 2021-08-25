package com.example.demo.controller;

import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.dto.ItemUpdateDto;
import com.example.demo.model.dto.search.ItemSearch;
import com.example.demo.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

//    @PostMapping
//    public ResponseEntity<?> addDoc(@RequestBody ItemDto dto) {
//        return ResponseEntity.ok(itemService.addDoc(dto));
//    }

//    @PostMapping("/search")
//    public ResponseEntity<?> search(@RequestBody ItemSearch search) {
//        return ResponseEntity.ok(itemService.search(search));
//    }


    @GetMapping("/nad")
    public ResponseEntity<?> getByNameAndDesc(@RequestParam String name,
                                              @RequestParam String description) {
        return ResponseEntity.ok(itemService.getByNameAndDesc(name, description));
    }

    @GetMapping("/nod")
    public ResponseEntity<?> getByNameOrDesc(@RequestParam String name,
                                             @RequestParam String description) {
        return ResponseEntity.ok(itemService.getByNameOrDesc(name, description));
    }

    @GetMapping("/name")
    public ResponseEntity<?> getByName(@RequestParam String name) {
        return ResponseEntity.ok(itemService.getByName(name));
    }

    @GetMapping("/b")
    public ResponseEntity<?> getByPrice(@RequestParam Double from,
                                        @RequestParam Double to) {
        return ResponseEntity.ok(itemService.getByPrice(from, to));
    }

    @PutMapping("/up/{id}")
    public ResponseEntity<?> update(@PathVariable String id,
                                    @RequestBody ItemUpdateDto dto) {
        return ResponseEntity.ok(itemService.updateDoc(id,dto));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        itemService.delete(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

}
