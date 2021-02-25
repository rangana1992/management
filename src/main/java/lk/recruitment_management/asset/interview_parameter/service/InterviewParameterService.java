package lk.recruitment_management.asset.interview_parameter.service;

import lk.recruitment_management.asset.interview_parameter.dao.InterviewParameterDao;
import lk.recruitment_management.asset.interview_parameter.entity.InterviewParameter;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewParameterService implements AbstractService<InterviewParameter, Integer> {
private final InterviewParameterDao interviewParameterDao;

    public InterviewParameterService(InterviewParameterDao interviewParameterDao) {
        this.interviewParameterDao = interviewParameterDao;
    }

    public List<InterviewParameter> findAll() {
        return interviewParameterDao.findAll();
    }

    public InterviewParameter findById(Integer id) {
        return interviewParameterDao.getOne(id);
    }

    public InterviewParameter persist(InterviewParameter interviewParameter) {
        return interviewParameterDao.save(interviewParameter);
    }

    public boolean delete(Integer id) {
        interviewParameterDao.deleteById(id);
        return true;
    }

    public List<InterviewParameter> search(InterviewParameter interviewParameter) {

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<InterviewParameter> interviewParameterExample = Example.of(interviewParameter, matcher);
        return interviewParameterDao.findAll(interviewParameterExample);
    }
}
