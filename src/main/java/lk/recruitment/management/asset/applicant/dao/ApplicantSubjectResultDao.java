package lk.recruitment.management.asset.applicant.dao;


import lk.recruitment.management.asset.applicant.entity.ApplicantResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantSubjectResultDao extends JpaRepository<ApplicantResult, Integer > {

}
