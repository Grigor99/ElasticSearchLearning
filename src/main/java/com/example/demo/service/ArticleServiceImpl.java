package com.example.demo.service;

import com.example.demo.model.Article;
import com.example.demo.model.dto.ArticleDto;
import com.example.demo.repo.ArticleRepository;
import org.elasticsearch.common.recycler.Recycler;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;

@Service
public class ArticleServiceImpl implements ArticleService {
    private ElasticsearchOperations elasticsearchOperations;
    private ArticleRepository articleRepository;

    public ArticleServiceImpl(ElasticsearchOperations elasticsearchOperations, ArticleRepository articleRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getByTitle(String title) {
        Query searchQuery = new NativeSearchQueryBuilder()

                .withFilter(regexpQuery("title", ".*" + title + ".*"))
                .build();
        SearchHits<Article> articles =
                elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        Iterator<SearchHit<Article>> iterator = articles.stream().iterator();
        List<Article> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getContent());
        }
        return list;
    }

    @Override
    public List<Article> getByTitleQueryBuilder(String title) {
        QueryBuilder queryBuilder = QueryBuilders
                .matchQuery("title", title);

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();
        SearchHits<Article> hits = elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        Iterator<SearchHit<Article>> iterator = hits.stream().iterator();
        List<Article> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getContent());
        }
        return list;
    }

    @Override
    public List<Article> getByCustom() {
        Query searchQuery = new StringQuery("{\"query_string\":{\"query\":\"take\" ,\"default_field\":\"title\"}}");
        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        Iterator<SearchHit<Article>> iterator = searchHits.stream().iterator();
        List<Article> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getContent());
        }
        return list;
    }

    @Override
    public List<Article> getByTitleCriteria() {
        Criteria criteria = new Criteria("title")
                .startsWith("take")
                .endsWith("hand")
                .contains("my");
        Query searchQuery = new CriteriaQuery(criteria);
        SearchHits<Article> searchHits = elasticsearchOperations.search(searchQuery, Article.class, IndexCoordinates.of("blog"));
        Iterator<SearchHit<Article>> iterator = searchHits.stream().iterator();
        List<Article> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next().getContent());
        }
        return list;
    }

    @Override
    public List<String> fetchSuggestions(String query) {
        QueryBuilder queryBuilder = QueryBuilders
                .wildcardQuery("title", query + "*");

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(0, 5))
                .build();

        SearchHits<Article> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        Article.class,
                        IndexCoordinates.of("blog"));

        List<String> suggestions = new ArrayList<String>();

        searchSuggestions.getSearchHits().forEach(searchHit -> {
            suggestions.add(searchHit.getContent().getTitle());
        });
        return suggestions;
    }

    @Override
    public List<Article> processSearch(final String query) {
        QueryBuilder queryBuilder =
                QueryBuilders
                        .multiMatchQuery(query, "title")//many field names
                        .fuzziness(Fuzziness.AUTO);
        //Changing a character (box → fox)
        //Removing a character (black → lack)
        //Inserting a character (sic → sick)
        //Transposing two adjacent characters (act → cat)
        //fuzzy does this

        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();

        // 2. Execute search
        SearchHits<Article> productHits =
                elasticsearchOperations
                        .search(searchQuery, Article.class,
                                IndexCoordinates.of("blog"));

        // 3. Map searchHits to product list
        List<Article> productMatches = new ArrayList<Article>();
        productHits.forEach(searchHit -> {
            productMatches.add(searchHit.getContent());
        });
        return productMatches;
    }

    @Override
    public Article addDoc(ArticleDto dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        articleRepository.save(article);
        return article;
    }

    @Override
    public List<String> getAll() {
        QueryBuilder queryBuilder = QueryBuilders
                .matchAllQuery();
        Query searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .build();
        SearchHits<Article> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        Article.class,
                        IndexCoordinates.of("blog"));

        List<String> suggestions = new ArrayList<String>();

        searchSuggestions.getSearchHits().forEach(searchHit -> {
            suggestions.add(searchHit.getContent().getTitle());
        });
        return suggestions;
    }
}
