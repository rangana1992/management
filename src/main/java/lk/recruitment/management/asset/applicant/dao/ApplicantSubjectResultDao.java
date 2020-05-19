package lk.recruitment.management.asset.applicant.dao;


import lk.recruitment.management.asset.applicant.entity.ApplicantSubjectResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantSubjectResultDao extends JpaRepository<ApplicantSubjectResult, Integer > {

}
