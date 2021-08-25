package com.example.demo.repo;

import com.example.demo.model.elastic.TransfersHistoryDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TransferHistoryDocRepository extends ElasticsearchRepository<TransfersHistoryDoc,String> {
}
