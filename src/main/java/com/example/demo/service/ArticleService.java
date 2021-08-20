package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.model.dto.ArticleDto;

import java.util.List;

public interface ArticleService {

    List<Article> getByTitle(String title);

    List<Article> getByTitleQueryBuilder(String title);

    List<Article> getByCustom();

    List<Article> getByTitleCriteria();

    List<String> fetchSuggestions(String query);

    List<Article> processSearch(String query);

    Article addDoc(ArticleDto dto);
}
