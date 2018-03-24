package site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import site.model.Article;
import site.service.ArticleService;


@Controller
@RequestMapping
public class MainController {
    //Віжкриваєм доступ до БД
    @Autowired
    private ArticleService service;

    //Передає дані (дані: service.getAll для поля articles) шаблонізатору (всі статті з БД)
    //і відображає main.html користовачу
    @RequestMapping
    public String mainPage(Model model) {
        model.addAttribute("articles", service.getAll());
        return "main";
    }
    //Добавляє в модель новий обєкт Article
    //і відображає editor.html
    @RequestMapping(value = "/editor")
    public String editorPage(Model model) {
        model.addAttribute("article", new Article());
        return "editor";
    }
    //Получає той самий обєкт Article і зберігає його до БД
    //і перенаправляє нас на головну сторінку
    @RequestMapping(value = "/editor/submit", method = RequestMethod.POST)
    public String submitArticle(@ModelAttribute Article article) {
        service.save(article);
        return "redirect:../";
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "login";
    }
}