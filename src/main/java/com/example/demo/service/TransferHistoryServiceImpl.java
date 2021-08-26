package com.example.demo.service;

import com.example.demo.jparepo.TransferDbHistoryRepository;
import com.example.demo.model.db.TransferDbHistory;
import com.example.demo.model.dto.TransferDto;
import com.example.demo.model.dto.TransferUpdateDto;
import com.example.demo.model.elastic.Article;
import com.example.demo.model.elastic.TransfersHistoryDoc;
import com.example.demo.repo.TransferHistoryDocRepository;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

@Service
public class TransferHistoryServiceImpl implements TransferHistoryService {
    private TransferDbHistoryRepository dbHistoryRepository;
    private TransferHistoryDocRepository docRepository;
    private ElasticsearchOperations elasticsearchOperations;

    public TransferHistoryServiceImpl(TransferDbHistoryRepository dbHistoryRepository, TransferHistoryDocRepository docRepository, ElasticsearchOperations elasticsearchOperations) {
        this.dbHistoryRepository = dbHistoryRepository;
        this.docRepository = docRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public TransfersHistoryDoc create(TransferDto dto) {
        TransfersHistoryDoc doc = new TransfersHistoryDoc();
        doc.setFrom(dto.getFrom());
        doc.setTo(dto.getTo());
        doc.setHowMuch(dto.getHowMuch());
        TransfersHistoryDoc saved = docRepository.save(doc);
        TransferDbHistory dbHistory = new TransferDbHistory();
        dbHistory.setFrom1(dto.getFrom());
        dbHistory.setTo1(dto.getTo());
        dbHistory.setHowMuch(dto.getHowMuch());
        dbHistory.setElasticId(doc.getId());
        dbHistory.setRemoved(false);
        dbHistory.setScore(1);
        dbHistoryRepository.save(dbHistory);
        return saved;
    }

    @Override
    public TransfersHistoryDoc update(String id, TransferUpdateDto dto) {
        TransfersHistoryDoc transfersHistoryDoc = docRepository.findById(id).get();
        transfersHistoryDoc.setFrom(dto.getFrom());
        transfersHistoryDoc.setTo(dto.getTo());
        transfersHistoryDoc.setHowMuch(dto.getHowMuch());

        TransferDbHistory dbHistory = dbHistoryRepository.findByElasticIdAndRemovedFalse(id);
        dbHistory.setFrom1(dto.getFrom());
        dbHistory.setTo1(dto.getTo());
        dbHistory.setHowMuch(dto.getHowMuch());
        dbHistory.setScore(dbHistory.getScore() + 1);
        dbHistoryRepository.save(dbHistory);

        return docRepository.save(transfersHistoryDoc);
    }

    @Override
    public TransfersHistoryDoc recoverDoc(TransferDto dto) {
        TransfersHistoryDoc doc = new TransfersHistoryDoc();
        doc.setId(dto.getId());
        doc.setFrom(dto.getFrom());
        doc.setTo(dto.getTo());
        doc.setHowMuch(dto.getHowMuch());
        TransfersHistoryDoc saved = docRepository.save(doc);
        return saved;
    }

    @Override
    public Integer getRecordsCount() {
        QueryBuilder queryBuilder = QueryBuilders
                .matchAllQuery();
        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();
        SearchHits<TransfersHistoryDoc> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        TransfersHistoryDoc.class,
                        IndexCoordinates.of("transfer_history"));
        return searchSuggestions.getSearchHits().size();
    }

    @Override
    public List<String> getRecordsIds() {
        QueryBuilder queryBuilder = QueryBuilders
                .matchAllQuery();
        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();
        SearchHits<TransfersHistoryDoc> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        TransfersHistoryDoc.class,
                        IndexCoordinates.of("transfer_history"));
        List<String> suggestions = new ArrayList<String>();

        searchSuggestions.getSearchHits().forEach(searchHit -> {
            suggestions.add(searchHit.getContent().getId());
        });
        return suggestions;
    }



}
