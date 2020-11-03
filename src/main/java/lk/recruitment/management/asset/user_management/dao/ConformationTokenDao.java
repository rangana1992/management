package lk.recruitment.management.asset.user_management.dao;


import lk.recruitment.management.asset.user_management.entity.ConformationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConformationTokenDao extends JpaRepository<ConformationToken, Integer> {
    ConformationToken findByToken(String token);

    ConformationToken findByEmail(String email);
}
