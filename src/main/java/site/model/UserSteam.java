package site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class UserSteam implements Serializable{

    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private int steamid;
    @Column
    private String personaname;
    @Column
    private String avatarsmall;
    @Column
    private String avatarmedium;
    @Column
    private String avatarbig;
}
