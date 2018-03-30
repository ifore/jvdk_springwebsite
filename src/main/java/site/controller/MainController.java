package site.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.ModelAndView;
import site.service.SteamOpenID;
import site.model.Article;
import site.service.ArticleService;
import site.service.UserSteamService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;


@Controller
@RequestMapping
public class MainController {
    //Віжкриваєм доступ до БД
    @Autowired
    private ArticleService service;
    @Autowired
    private UserSteamService userservice ;
    private SteamOpenID so = new SteamOpenID();

    //Передає дані (дані: service.getAll для поля articles) шаблонізатору (всі статті з БД)
    //і відображає main.html користовачу
    /*@RequestMapping
    public String mainPage(Model model) {
        model.addAttribute("articles", service.getAll());
        return "main";
    }*/

    @RequestMapping
    public String mainPage(Model model, HttpServletRequest request) throws  IOException{
        model.addAttribute("users", userservice.getAll());
        if(request.isRequestedSessionIdValid())
            return "mainv2";
        else
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
    public ModelAndView authentication(HttpServletRequest request, HttpServletResponse response)  throws IOException {

        String urll = "http://localhost:8080/"+ URLEncoder.encode("auth", "UTF-8");
        String user = so.verify(urll, request.getParameterMap());
        System.out.println("user +++ " + user);
        userservice.saveUser(user);
        if(user == null) {
            return new ModelAndView("redirect:" + "http://localhost:8080");
        }
        System.out.println("User 64id :" + user);
        request.getSession().setAttribute("steamid", user);
        return new ModelAndView("redirect:" + "http://localhost:8080");
    }

    @RequestMapping(value = "/logout")
    public ModelAndView loggingout(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        request.getSession().removeAttribute("steamid");
        return new ModelAndView("redirect:" + so.login("http://localhost:8080"));
    }
}