package lk.recruitment_management.asset.applicant_gazette.dao;


import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantGazetteDao extends JpaRepository< ApplicantGazette, Integer> {
}
