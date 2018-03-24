package site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.dao.ArticleRepository;
import site.model.Article;


import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Управління БД
@Service
public class ArticleService {


    @Autowired
    private ArticleRepository repository;
    // Додавання статей до БД
    public void save(Article article) {
        repository.save(article);
    }

    // Метод, який повертає колекцію всіх посортованих по даті статей
    public List<Article> getAll() {
        return StreamSupport
                .stream(
                        Spliterators.spliteratorUnknownSize(repository.findAll().iterator(), Spliterator.NONNULL),
                        false)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}