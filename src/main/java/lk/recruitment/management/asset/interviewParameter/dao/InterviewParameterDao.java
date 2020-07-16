package lk.recruitment.management.asset.interviewParameter.dao;

import lk.recruitment.management.asset.interviewParameter.entity.InterviewParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewParameterDao extends JpaRepository<InterviewParameter, Integer> {
}
