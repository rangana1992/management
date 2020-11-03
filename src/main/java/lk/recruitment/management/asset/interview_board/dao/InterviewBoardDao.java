package lk.recruitment.management.asset.interview_board.dao;


import lk.recruitment.management.asset.interview_board.entity.InterviewBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewBoardDao extends JpaRepository<InterviewBoard, Integer> {
}
