package lk.recruitment_management.asset.applicant_non_relative.dao;


import lk.recruitment_management.asset.applicant_non_relative.entity.ApplicantNonRelative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantNonRelativeDao extends JpaRepository< ApplicantNonRelative, Integer> {

}
