package lk.recruitment.management.asset.applicant.dao;


import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantDao extends JpaRepository<Applicant, Integer> {
    Applicant findFirstByOrderByIdDesc();

    Applicant findByNic(String nic);

    Applicant findByEmail(String email);

  int countByApplicantStatus(ApplicantStatus applicantStatus);

  List< Applicant> findByApplicantStatus(ApplicantStatus applicantStatus);
}
