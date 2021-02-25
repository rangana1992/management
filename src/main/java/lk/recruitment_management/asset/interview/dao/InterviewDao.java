package lk.recruitment_management.asset.interview.dao;

import lk.recruitment_management.asset.interview.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewDao extends JpaRepository<Interview, Integer> {
}
