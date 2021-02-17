package lk.recruitment_management.asset.applicant_interview.service;


import lk.recruitment_management.asset.applicant_interview.dao.ApplicantInterviewDao;
import lk.recruitment_management.asset.applicant_interview.entity.ApplicantInterview;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig(cacheNames = "applicantInterview")
public class ApplicantInterviewService implements AbstractService<ApplicantInterview, Integer> {
    private final ApplicantInterviewDao applicantInterviewDao;

    @Autowired
    public ApplicantInterviewService(ApplicantInterviewDao applicantInterviewDao) {

        this.applicantInterviewDao = applicantInterviewDao;
    }

    @Cacheable
    public List< ApplicantInterview > findAll() {
        return applicantInterviewDao.findAll();
    }

    @Cacheable
    public ApplicantInterview findById(Integer id) {
        return applicantInterviewDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "applicantInterview", allEntries = true )},
        put = {@CachePut( value = "applicantInterview", key = "#applicantInterview.id" )} )
    @Transactional
    public ApplicantInterview persist(ApplicantInterview applicantInterview) {
        return applicantInterviewDao.save(applicantInterview);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        applicantInterviewDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< ApplicantInterview > search(ApplicantInterview applicantInterview) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantInterview > districtExample = Example.of(applicantInterview, matcher);
        return applicantInterviewDao.findAll(districtExample);
    }

}
