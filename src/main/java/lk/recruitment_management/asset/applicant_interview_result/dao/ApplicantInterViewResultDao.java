package lk.recruitment_management.asset.applicant_interview_result.dao;


import lk.recruitment_management.asset.applicant_interview_result.entity.ApplicantInterviewResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantInterViewResultDao extends JpaRepository< ApplicantInterviewResult, Integer> {


}
