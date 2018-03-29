package site.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.dao.UserSteamRepository;
import site.model.UserSteam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserSteamService {

    private String APIkey = "F61245A3B081FA8EC1FD647B8C7F93F4";
    @Autowired
    private UserSteamRepository repository;

    public void saveUser(String id64){
        String id64s = id64;
        System.out.println("id64s ");
        String jsondata = "";
        String username = "";
        String useravatarsmall = "";
        String useravatarmedium = "";
        String useravatarfull = "";
        try {
            URL getPlayerSummaries = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="+APIkey+"&steamids="+ id64);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(getPlayerSummaries.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsondata += inputLine;
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("json data " + jsondata);
        JSONObject jsonObject = new JSONObject(jsondata);
        JSONObject jsonResponse = jsonObject.getJSONObject("response");
        JSONArray jsonPlayers = jsonResponse.getJSONArray("players");

        for (int i = 0; i < jsonPlayers.length() ; i++) {
            username = jsonPlayers.getJSONObject(i).getString("personaname");
            useravatarsmall = jsonPlayers.getJSONObject(i).getString("avatar");
            useravatarmedium = jsonPlayers.getJSONObject(i).getString("avatarmedium");
            useravatarfull = jsonPlayers.getJSONObject(i).getString("avatarfull");
        }

        if (repository.existsById(id64s)){
            repository.findById(id64s).get().setPersonaname(username);
            repository.findById(id64s).get().setAvatarsmall(useravatarsmall);
            repository.findById(id64s).get().setAvatarmedium(useravatarmedium);
            repository.findById(id64s).get().setAvatarbig(useravatarfull);
        } else{
            UserSteam user = new UserSteam(id64s);
            user.setPersonaname(username);
            user.setAvatarsmall(useravatarsmall);
            user.setAvatarmedium(useravatarmedium);
            user.setAvatarbig(useravatarfull);

        }
    }

    // Метод, який повертає колекцію всіх користувачів
    public List<UserSteam> getAll() {
        return StreamSupport
                .stream(
                        Spliterators.spliteratorUnknownSize(repository.findAll().iterator(), Spliterator.NONNULL),
                        false)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
