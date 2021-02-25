package lk.recruitment_management.asset.applicant_sis_crd_cid_result.dao;

import lk.recruitment_management.asset.applicant_sis_crd_cid_result.entity.ApplicantSisCrdCid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantSisCrdCidDao extends JpaRepository< ApplicantSisCrdCid, Integer > {
}
