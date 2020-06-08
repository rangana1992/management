package lk.recruitment.management.asset.applicant.service;


import lk.recruitment.management.asset.applicant.dao.ApplicantSubjectResultDao;
import lk.recruitment.management.asset.applicant.entity.ApplicantSubjectResult;
import lk.recruitment.management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig( cacheNames = "applicantSubjectResult" )
public class ApplicantSubjectResultService implements AbstractService<ApplicantSubjectResult, Integer > {

    private final ApplicantSubjectResultDao applicantSubjectResultDao;

    @Autowired
    public ApplicantSubjectResultService(ApplicantSubjectResultDao applicantSubjectResultDao) {
        this.applicantSubjectResultDao = applicantSubjectResultDao;
    }

    @Cacheable
    public List<ApplicantSubjectResult> findAll() {
        return applicantSubjectResultDao.findAll();
    }

    @Cacheable
    public ApplicantSubjectResult findById(Integer id) {
        return applicantSubjectResultDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "applicantSubjectResult", allEntries = true)},
            put = {@CachePut(value = "applicantSubjectResult", key = "#applicantSubjectResult.id")})
    @Transactional
    public ApplicantSubjectResult persist(ApplicantSubjectResult applicantSubjectResult) {
        return applicantSubjectResultDao.save(applicantSubjectResult);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        applicantSubjectResultDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<ApplicantSubjectResult> search(ApplicantSubjectResult applicantSubjectResult) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ApplicantSubjectResult> applicantSubjectResultExample = Example.of(applicantSubjectResult, matcher);
        return applicantSubjectResultDao.findAll(applicantSubjectResultExample);
    }

    public boolean isApplicantSubjectResultPresent(ApplicantSubjectResult applicantSubjectResult) {
        return applicantSubjectResultDao.equals(applicantSubjectResult);
    }

}
