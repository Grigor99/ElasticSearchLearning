package com.example.demo.controller;

import com.example.demo.model.Article;
import com.example.demo.model.dto.ArticleDto;
import com.example.demo.service.ArticleService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    private ArticleService articleService;

    public Controller(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<?> getByTitle(@RequestParam String title) {
        return ResponseEntity.ok(articleService.getByTitle(title));
    }

    @GetMapping("/1")
    public ResponseEntity<?> getByTitleQueryBuilder(@RequestParam String title) {
        return ResponseEntity.ok(articleService.getByTitleQueryBuilder(title));
    }

    @GetMapping("/2")
    public ResponseEntity<?> getByTitleCustom() {
        return ResponseEntity.ok(articleService.getByCustom());
    }

    @GetMapping("/3")
    public ResponseEntity<?> getByTitleCriteria() {
        return ResponseEntity.ok(articleService.getByTitleCriteria());
    }

    @GetMapping("/4")
    public ResponseEntity<?> getByTitleFetch(@RequestParam String title) {
        return ResponseEntity.ok(articleService.fetchSuggestions(title));
    }

    @GetMapping("/5")
    public ResponseEntity<?> getByTitleFuzzy(@RequestParam String title) {
        return ResponseEntity.ok(articleService.processSearch(title));
    }

    @GetMapping("/6")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(articleService.getAll());
    }


    @PostMapping
    public ResponseEntity<?> addDoc(@RequestBody ArticleDto articleDto) {
        return ResponseEntity.ok(articleService.addDoc(articleDto));
    }

    @GetMapping("/get/1")
    public ResponseEntity<?> createIndex() {
        return ResponseEntity.ok(articleService.createIndex());
    }
}
