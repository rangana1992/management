package lk.recruitment_management.asset.interview.service;

import lk.recruitment_management.asset.interview.dao.InterviewDao;
import lk.recruitment_management.asset.interview.entity.Enum.InterviewName;
import lk.recruitment_management.asset.interview.entity.Interview;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService implements AbstractService<Interview, Integer> {
    private final InterviewDao interviewDao;

    public InterviewService(InterviewDao interviewDao) {
        this.interviewDao = interviewDao;
    }

    public List<Interview> findAll() {
        return interviewDao.findAll();
    }

    public Interview findById(Integer id) {
        return interviewDao.getOne(id);
    }

    public Interview persist(Interview interview) {
        return interviewDao.save(interview);
    }

    public boolean delete(Integer id) {
        interviewDao.deleteById(id);
        return true;
    }

    public List<Interview> search(Interview interview) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Interview> interviewExample = Example.of(interview, matcher);
        return interviewDao.findAll(interviewExample);
    }

  public Interview findByInterviewName(InterviewName interviewName) {
        return interviewDao.findByInterviewName(interviewName);
  }
}
