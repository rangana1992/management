package lk.recruitment_management.asset.user_management.dao;


import lk.recruitment_management.asset.user_management.entity.Enum.UserSessionLogStatus;
import lk.recruitment_management.asset.user_management.entity.User;
import lk.recruitment_management.asset.user_management.entity.UserSessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSessionLogDao extends JpaRepository<UserSessionLog, Integer > {
    UserSessionLog findByUserAndUserSessionLogStatus(User user, UserSessionLogStatus userSessionLogStatus);

  List< UserSessionLog> findByUser(User user);
}
