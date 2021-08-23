package com.example.demo.model.dto.search;

import com.example.demo.model.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
