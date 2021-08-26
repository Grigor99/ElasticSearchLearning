package com.example.demo.controller;

import com.example.demo.model.dto.TransferDto;
import com.example.demo.model.dto.TransferUpdateDto;
import com.example.demo.service.TransferHistoryService;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private TransferHistoryService transferHistoryService;

    public TransferController(TransferHistoryService transferHistoryService) {

        this.transferHistoryService = transferHistoryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransferDto dto) {
        return ResponseEntity.ok(transferHistoryService.create(dto));
    }

    @PostMapping("/id/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody TransferUpdateDto dto) {
        return ResponseEntity.ok(transferHistoryService.update(id, dto));
    }


    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(transferHistoryService.getAll());
    }




}
