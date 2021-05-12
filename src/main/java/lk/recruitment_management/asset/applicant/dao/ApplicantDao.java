package lk.recruitment_management.asset.applicant.dao;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicantDao extends JpaRepository<Applicant, Integer> {
    Applicant findFirstByOrderByIdDesc();

    Applicant findByNic(String nic);

    Applicant findByEmail(String email);

  List< Applicant> findByCreatedAtIsBetween(LocalDateTime form,LocalDateTime to);
}
