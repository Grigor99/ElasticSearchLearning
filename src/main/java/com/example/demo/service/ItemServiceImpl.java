package com.example.demo.service;

import com.example.demo.jparepo.ItemDbRepo;
import com.example.demo.model.db.ItemDbEntity;
import com.example.demo.model.dto.ItemDto;
import com.example.demo.model.elastic.Item;
import com.example.demo.model.dto.ItemUpdateDto;
import com.example.demo.repo.ItemRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private ItemDbRepo dbRepo;
    private ElasticsearchOperations elasticsearchOperations;

    public ItemServiceImpl(ItemRepository itemRepository, ItemDbRepo dbRepo, ElasticsearchOperations elasticsearchOperations) {
        this.itemRepository = itemRepository;
        this.dbRepo = dbRepo;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public Item addDoc(ItemDto dto) {
        Item item = new Item(dto.getName(), dto.getDescription(), dto.getQuantity(), dto.getPrice(), dto.getFirm());
        itemRepository.save(item);
        ItemDbEntity dbEntity = new ItemDbEntity(item.getId(), 1, item.getName(), item.getDescription(), item.getQuantity()
                , item.getPrice(), item.getFirm());
        dbEntity.setRemoved(false);
        dbRepo.save(dbEntity);
        return item;
    }

//    @Override
//    public List<Item> search(ItemSearch search) {
//        QueryBuilder nameBuilder = null;
//        if (search.getName() != null) {
//            nameBuilder = QueryBuilders
//                    .regexpQuery("name", ".*" + search.getName() + ".*");
//        }
//
//        QueryBuilder descriptionBuilder = null;
//        if (search.getDescription() != null) {
//            descriptionBuilder = QueryBuilders
//                    .matchQuery("description", search.getDescription()).
//                    fuzziness(Fuzziness.AUTO)
//                    .prefixLength(3);
//        }
//
//        Query nameQuery = null;
//        if (nameBuilder != null) {
//            nameQuery = new NativeSearchQueryBuilder()
//                    .withFilter(nameBuilder).build();
//        }
//
//        Query descQuery = null;
//        if (descriptionBuilder != null) {
//            descQuery = new NativeSearchQueryBuilder()
//                    .withFilter(descriptionBuilder).build();
//        }
//
//
//        Criteria quantityCriteria = null;
//        if (search.getQuantityFrom() != null && search.getQuantityTo() != null) {
//            quantityCriteria = new Criteria("quantity")
//                    .between(search.getQuantityFrom(), search.getQuantityTo());
//        } else if (search.getQuantityFrom() != null && search.getQuantityTo() == null) {
//            quantityCriteria = new Criteria("quantity")
//                    .greaterThan(search.getQuantityFrom());
//        } else if (search.getQuantityFrom() == null && search.getQuantityTo() != null) {
//            quantityCriteria = new Criteria("quantity")
//                    .lessThan(search.getQuantityTo());
//        }
//
//        Criteria priceCriteria = null;
//        if (search.getPriceFrom() != null && search.getPriceTo() != null) {
//            priceCriteria = new Criteria("price")
//                    .between(search.getPriceFrom(), search.getPriceTo());
//        } else if (search.getPriceFrom() != null && search.getPriceTo() == null) {
//            priceCriteria = new Criteria("price")
//                    .greaterThan(search.getPriceFrom());
//        } else if (search.getPriceFrom() == null && search.getPriceTo() != null) {
//            priceCriteria = new Criteria("price")
//                    .lessThan(search.getPriceTo());
//        }
//        Query quantityQuery = null;
//        if (quantityCriteria != null) {
//            quantityQuery = new CriteriaQuery(quantityCriteria);
//        }
//        Query priceQuery = null;
//        if (priceCriteria != null) {
//            priceQuery = new CriteriaQuery(priceCriteria);
//        }
//
//
//        QueryBuilder firmBuilder = null;
//        if (search.getFirm() != null) {
//            firmBuilder = QueryBuilders.termQuery("firm", search.getFirm());
//        }
//
//
//        Query firmQuery = null;
//        if (firmBuilder != null) {
//            firmQuery = new NativeSearchQueryBuilder()
//                    .withFilter(firmBuilder).build();
//        }
//
//
//        List<Query> queries = new ArrayList<>();
//        if (nameQuery != null) {
//
//            queries.add(nameQuery);
//        }
//        if (descQuery != null) {
//
//            queries.add(descQuery);
//        }
//        if (quantityQuery != null) {
//            queries.add(quantityQuery);
//        }
//        if (priceQuery != null) {
//            queries.add(priceQuery);
//        }
//        if (firmQuery != null) {
//            queries.add(firmQuery);
//        }
//        List<SearchHits<Item>> searchHits =
//                elasticsearchOperations.multiSearch(queries, Item.class, IndexCoordinates.of("items"));
//
//        List<Item> list = new ArrayList<>();
//        for (SearchHits<Item> itemSearchHits : searchHits) {
//            itemSearchHits.forEach(searchHit -> {
//                list.add(searchHit.getContent());
//            });
//        }
//
//        return list;
//    }

    @Override
    public List<Item> getByNameAndDesc(String name, String desc) {
        return itemRepository.findByNameAndDescription(name, desc);
    }

    @Override
    public List<Item> getByNameOrDesc(String name, String desc) {
        return itemRepository.findByNameOrDescription(name, desc);
    }

    @Override
    public List<Item> getByName(String name) {
        return itemRepository.findByName(name);
    }

    @Override
    public List<Item> getByPrice(Double p1, Double p2) {
        return itemRepository.findByPriceBetween(p1, p2);
    }


    @Override
    public Item updateDoc(String id, ItemUpdateDto dto) {
        Item item = itemRepository.findById(id).get();
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        Item savedItem = itemRepository.save(item);
        ItemDbEntity dbEntity = dbRepo.findByElasticIdAndRemovedFalse(id);
        dbEntity.setDescription(dto.getDescription());
        dbEntity.setPrice(dto.getPrice());
        dbEntity.setScore(dbEntity.getScore() + 1);
        dbRepo.save(dbEntity);
        return savedItem;
    }

    @Override
    public void delete(String id) {
        Item item = itemRepository.findById(id).get();
        itemRepository.delete(item);
        ItemDbEntity dbEntity = dbRepo.findByElasticIdAndRemovedFalse(id);
        dbEntity.setRemoved(true);
        dbEntity.setScore(dbEntity.getScore() + 1);
        dbRepo.save(dbEntity);
    }
}
