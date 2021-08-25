package com.example.demo.controller;

import com.example.demo.model.dto.TransferDto;
import com.example.demo.service.TransferHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    private TransferHistoryService transferHistoryService;

    public TransferController(TransferHistoryService transferHistoryService) {

        this.transferHistoryService = transferHistoryService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransferDto dto){
        return ResponseEntity.ok(transferHistoryService.create(dto));
    }
}
