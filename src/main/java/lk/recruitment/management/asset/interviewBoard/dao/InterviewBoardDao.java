package lk.recruitment.management.asset.interviewBoard.dao;


import lk.recruitment.management.asset.interviewBoard.entity.InterviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewBoardDao extends JpaRepository<InterviewBoard, Integer> {
}
