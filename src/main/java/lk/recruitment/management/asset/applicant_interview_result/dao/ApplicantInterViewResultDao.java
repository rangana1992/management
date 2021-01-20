package lk.recruitment.management.asset.applicant_interview_result.dao;


import lk.recruitment.management.asset.applicant_interview_result.entity.ApplicantInterviewResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantInterViewResultDao extends JpaRepository< ApplicantInterviewResult, Integer> {


}
