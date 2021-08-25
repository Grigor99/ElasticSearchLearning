package com.example.demo.model.dto.search;

import com.example.demo.model.elastic.Article;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Response {
    private Integer count;
    private List<Article> article;

    public Response(Integer count, List<Article> article) {
        this.count = count;
        this.article = article;
    }
}
