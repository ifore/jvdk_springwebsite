package site.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import site.model.Article;


@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer> {
}