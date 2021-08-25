package com.example.demo.jparepo;

import com.example.demo.model.db.TransferDbHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDbHistoryRepository extends JpaRepository<TransferDbHistory,Long> {

    TransferDbHistory findByElasticIdAndRemovedFalse(String id);
}
