package lk.recruitment_management.asset.applicant_gazette.dao;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicantGazetteDao extends JpaRepository< ApplicantGazette, Integer> {
  List< ApplicantGazette> findByApplicant(Applicant applicant);

  List< ApplicantGazette> findByCreatedAtIsBetweenAndApplicantGazetteStatusAndApplyingRank(LocalDateTime dateTimeToLocalDateEndInDay, LocalDateTime dateTimeToLocalDateEndInDay1, ApplicantGazetteStatus applicantGazetteStatus, ApplyingRank applyingRank);

  int countByApplicantGazetteStatus(ApplicantGazetteStatus applicantGazetteStatus);

  int countByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus applicantGazetteStatus, Gazette gazette);

  List< ApplicantGazette> findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus applicantGazetteStatus, Gazette gazette);

  ApplicantGazette findByGazetteAndApplicant(Gazette gazette, Applicant applicant);

  List< ApplicantGazette > findByGazette(Gazette gazette);

  ApplicantGazette findFirstByOrderByIdDesc();

  ApplicantGazette findByCode(String code);
}
