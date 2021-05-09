package lk.recruitment_management.asset.applicant_gazette_interview_result.dao;


import lk.recruitment_management.asset.applicant_gazette_interview_result.entity.ApplicantGazetteInterviewResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantGazetteInterViewResultDao extends JpaRepository< ApplicantGazetteInterviewResult, Integer> {


}
