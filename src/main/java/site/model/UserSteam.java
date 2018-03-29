package site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class UserSteam implements Serializable, Comparable<UserSteam>{

    @Id
    private String steamid;
    @Column
    private String personaname;
    @Column
    private String avatarsmall;
    @Column
    private String avatarmedium;
    @Column
    private String avatarbig;
    @Column
    private long creationTimestamp;




    public UserSteam(String id64 ) {
        this.creationTimestamp = System.currentTimeMillis();
        this.steamid = id64;
    }

    @Override
    public int compareTo(UserSteam that) {
        return Long.compare(this.creationTimestamp, that.creationTimestamp);
    }


    public String getSteamid() {
        return steamid;
    }

    public void setSteamid(String steamid) {
        this.steamid = steamid;
    }

    public String getPersonaname() {
        return personaname;
    }

    public void setPersonaname(String personaname) {
        this.personaname = personaname;
    }

    public String getAvatarsmall() {
        return avatarsmall;
    }

    public void setAvatarsmall(String avatarsmall) {
        this.avatarsmall = avatarsmall;
    }

    public String getAvatarmedium() {
        return avatarmedium;
    }

    public void setAvatarmedium(String avatarmedium) {
        this.avatarmedium = avatarmedium;
    }

    public String getAvatarbig() {
        return avatarbig;
    }

    public void setAvatarbig(String avatarbig) {
        this.avatarbig = avatarbig;
    }
}
