package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.model.dto.ArticleDto;
import com.example.demo.model.dto.search.ArticleSearch;
import com.example.demo.model.dto.search.Response;

import java.util.List;

public interface ArticleService {

    List<Article> getByTitle(String title);

    List<Article> getByTitleQueryBuilder(String title);

    List<Article> getByCustom();

    List<Article> getByTitleCriteria();

    List<String> fetchSuggestions(String query);

    List<Article> processSearch(String query);

    Article addDoc(ArticleDto dto);

    List<Article> getAll();

    boolean createIndex();

    Response searchByFields(ArticleSearch search);
}
