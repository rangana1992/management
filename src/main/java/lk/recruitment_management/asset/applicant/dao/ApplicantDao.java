package lk.recruitment_management.asset.applicant.dao;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.entity.enums.ApplicantStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicantDao extends JpaRepository<Applicant, Integer> {
    Applicant findFirstByOrderByIdDesc();

    Applicant findByNic(String nic);

    Applicant findByEmail(String email);

  int countByApplicantStatus(ApplicantStatus applicantStatus);

  List< Applicant> findByApplicantStatus(ApplicantStatus applicantStatus);

  List< Applicant> findByCreatedAtIsBetweenAndApplyingRankAndApplicantStatus(LocalDateTime form,LocalDateTime to, ApplyingRank applyingRank, ApplicantStatus applicantStatus);
}
