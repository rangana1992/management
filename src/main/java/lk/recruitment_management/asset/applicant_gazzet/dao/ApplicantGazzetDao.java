package lk.recruitment_management.asset.applicant_gazzet.dao;


import lk.recruitment_management.asset.applicant_gazzet.entity.ApplicantGazzet;
import lk.recruitment_management.asset.gazzet.entity.Gazzet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantGazzetDao extends JpaRepository< ApplicantGazzet, Integer> {
}
