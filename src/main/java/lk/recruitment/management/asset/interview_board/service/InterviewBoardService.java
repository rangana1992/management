package lk.recruitment.management.asset.interview_board.service;


import lk.recruitment.management.asset.interview_board.dao.InterviewBoardDao;
import lk.recruitment.management.asset.interview_board.entity.InterviewBoard;
import lk.recruitment.management.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewBoardService implements AbstractService<InterviewBoard, Integer> {
    private final InterviewBoardDao interviewBordDao;

    public InterviewBoardService(InterviewBoardDao interviewBordDao) {
        this.interviewBordDao = interviewBordDao;
    }

    public List<InterviewBoard> findAll() {
        return interviewBordDao.findAll();
    }

    public InterviewBoard findById(Integer id) {
        return interviewBordDao.getOne(id);
    }

    public InterviewBoard persist(InterviewBoard interview) {
        return interviewBordDao.save(interview);
    }

    public boolean delete(Integer id) {
        interviewBordDao.deleteById(id);
        return true;
    }

    public List<InterviewBoard> search(InterviewBoard interview) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<InterviewBoard> interviewExample = Example.of(interview, matcher);
        return interviewBordDao.findAll(interviewExample);
    }
}
