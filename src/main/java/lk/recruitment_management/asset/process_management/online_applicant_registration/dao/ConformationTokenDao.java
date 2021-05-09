package lk.recruitment_management.asset.process_management.online_applicant_registration.dao;


import lk.recruitment_management.asset.process_management.online_applicant_registration.entity.ConformationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConformationTokenDao extends JpaRepository<ConformationToken, Integer> {
    ConformationToken findByToken(String token);

    ConformationToken findByEmail(String email);
}
