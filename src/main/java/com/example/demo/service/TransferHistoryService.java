package com.example.demo.service;

import com.example.demo.model.dto.TransferDto;
import com.example.demo.model.elastic.TransfersHistoryDoc;

import java.util.List;

public interface TransferHistoryService {
    TransfersHistoryDoc create(TransferDto dto);

    TransfersHistoryDoc recoverDoc(TransferDto dto);

    Integer getRecordsCount();

    List<String> getRecordsIds();
}