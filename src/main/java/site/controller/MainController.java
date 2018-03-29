package site.controller;


import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import site.configuration.SteamOpenID;
import site.model.Article;
import site.service.ArticleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping
public class MainController {
    //Віжкриваєм доступ до БД
    @Autowired
    private ArticleService service;
    private SteamOpenID so = new SteamOpenID();

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

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView method() {
        return new ModelAndView("redirect:" + so.login("http://localhost:8080/auth"));

    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public ModelAndView dosomething(HttpServletRequest request, HttpServletResponse response)  throws IOException {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:../");
        String user = so.verify(request.getRequestURI(), request.getParameterMap());
        if(user == null) {
            return new ModelAndView("redirect:" + so.login("http://localhost:8080"));
        }
        request.getSession(true).setAttribute("steamid", user);
        System.out.println("USER information" + user);
        return new ModelAndView("redirect:" + so.login("http://localhost:8080"));
    }
}