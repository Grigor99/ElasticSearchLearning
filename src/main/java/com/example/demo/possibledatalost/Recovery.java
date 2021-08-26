package com.example.demo.possibledatalost;

import com.example.demo.jparepo.TransferDbHistoryRepository;
import com.example.demo.model.db.TransferDbHistory;
import com.example.demo.model.dto.TransferDto;
import com.example.demo.service.TransferHistoryService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class Recovery {

    private TransferHistoryService transferHistoryService;
    private TransferDbHistoryRepository transferDbHistoryRepository;

    public Recovery(TransferHistoryService transferHistoryService, TransferDbHistoryRepository transferDbHistoryRepository) {
        this.transferHistoryService = transferHistoryService;
        this.transferDbHistoryRepository = transferDbHistoryRepository;
    }
    //    shard = hash(routing) % number_of_primary_shards
    //never change primary shards number or anything about it!
    //routing _id

    /*

replication
The default value for replication is sync. This causes the primary shard to wait for successful responses from the replica shards before returning.
If you set replication to async, it will return success to the client as soon as the request has been executed on the primary shard. It will still forward the request to the replicas, but you will not know whether the replicas succeeded.
This option is mentioned specifically to advise against using it. The default sync replication allows Elasticsearch to exert back pressure on whatever system is feeding it with data. With async replication, it is possible to overload Elastic‐ search by sending too many requests without waiting for their completion.
     */

    //#discovery.seed_hosts: ["7.14.0"] ip address login google cloud create instance then take ip
    //google cloud create node copy ip and put it in seed hosts

    @Scheduled(fixedRate = 1000 * 60 * 60)//an hour
    public void executeTask1() {
        System.out.println(Thread.currentThread().getName() + " The Task1 executed at " + new Date());
        try {
            Integer elasticCount = transferHistoryService.getRecordsCount();
            Long dbCount = transferDbHistoryRepository.count();
            if (elasticCount.intValue() != dbCount.intValue()) {
                List<String> elasticIds = transferHistoryService.getRecordsIds();
                List<String> dbIds = transferDbHistoryRepository.findAll().stream().map(TransferDbHistory::getElasticId).collect(Collectors.toList());
                List<String> differences = dbIds.stream().filter(element -> !elasticIds.contains(element)).collect(Collectors.toList());
                for (String id : differences) {
                    TransferDbHistory dbHistory = transferDbHistoryRepository.findByElasticIdAndRemovedFalse(id);
                    TransferDto dto = new TransferDto();
                    dto.setId(dbHistory.getElasticId());
                    dto.setFrom(dbHistory.getFrom1());
                    dto.setTo(dbHistory.getTo1());
                    dto.setHowMuch(dbHistory.getHowMuch());
                    transferHistoryService.recoverDoc(dto);
                    dbHistory.set(dbHistory.getDocBeenDeletedAndRecovered());
                    transferDbHistoryRepository.save(dbHistory);
//                    transferDbHistoryRepository.delete(dbHistory);
                }
                System.out.println("data lost happened and recovered again");
            }
            System.out.println("finish");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
