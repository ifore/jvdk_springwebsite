package site.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import site.model.UserSteam;

@Repository
public interface UserSteamRepository extends CrudRepository<UserSteam, String> {

}