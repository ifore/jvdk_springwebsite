package site.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

import javax.servlet.ServletException;
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

    private String getFullUrl(HttpServletRequest request, String path) {

        StringBuilder builder = new StringBuilder("localhost:8080");
        System.out.println(builder);
        builder.insert(0, "http://");
        builder.append(path);
        return builder.toString();
    }
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
        String id = null;
        if(null != request.getSession(true).getAttribute("steamid"))
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
    public String method(HttpServletRequest request,HttpServletResponse response)  throws IOException  {
        response.sendRedirect(so.login(getFullUrl(request, "/auth")));
        return null;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String authentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String user = so.verify(request.getRequestURL().toString(), request.getParameterMap());
        String fullUrl = getFullUrl(request, "/");
        userservice.saveUser(user);
        if(user == null) {
            response.sendRedirect(fullUrl);
        }
        request.getSession(true).setAttribute("steamid", user);
        response.sendRedirect(fullUrl);
        return null;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("sjkfnskjdnfkjdsnjkfnkjnf");
        request.getSession(true).removeAttribute("steamid");
        response.sendRedirect(getFullUrl(request,"/"));
        return null;
    }

}