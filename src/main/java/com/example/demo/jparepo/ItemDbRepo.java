package com.example.demo.jparepo;

import com.example.demo.model.db.ItemDbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDbRepo extends JpaRepository<ItemDbEntity, Long> {
    ItemDbEntity findByElasticIdAndRemovedFalse(String id);
}
