package lk.recruitment_management.asset.applicant_result.dao;


import lk.recruitment_management.asset.applicant_result.entity.ApplicantResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantSubjectResultDao extends JpaRepository<ApplicantResult, Integer > {

}
