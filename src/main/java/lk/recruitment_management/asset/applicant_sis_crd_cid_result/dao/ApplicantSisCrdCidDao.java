package lk.recruitment_management.asset.applicant_sis_crd_cid_result.dao;

import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.ApplicantSisCrdCid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantSisCrdCidDao extends JpaRepository< ApplicantSisCrdCid, Integer > {
  List< ApplicantSisCrdCid> findByApplicantGazette(ApplicantGazette applicantGazette);
}
