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
public class Manager {

    private TransferHistoryService transferHistoryService;
    private TransferDbHistoryRepository transferDbHistoryRepository;

    public Manager(TransferHistoryService transferHistoryService, TransferDbHistoryRepository transferDbHistoryRepository) {
        this.transferHistoryService = transferHistoryService;
        this.transferDbHistoryRepository = transferDbHistoryRepository;
    }

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
                System.out.println("data lost happened and saved back again");
            }
            System.out.println("finish");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
