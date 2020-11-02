package lk.recruitment.management.asset.interview_parameter.dao;

import lk.recruitment.management.asset.interview_parameter.entity.InterviewParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewParameterDao extends JpaRepository<InterviewParameter, Integer> {
}
