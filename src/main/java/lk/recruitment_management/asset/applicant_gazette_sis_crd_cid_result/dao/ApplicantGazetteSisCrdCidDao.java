package lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.dao;

import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.ApplicantGazetteSisCrdCid;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.enums.InternalDivision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantGazetteSisCrdCidDao extends JpaRepository< ApplicantGazetteSisCrdCid, Integer > {
  List< ApplicantGazetteSisCrdCid > findByApplicantGazette(ApplicantGazette applicantGazette);

  ApplicantGazetteSisCrdCid findByApplicantGazetteAndInternalDivision(ApplicantGazette applicantGazette, InternalDivision internalDivision);
}
