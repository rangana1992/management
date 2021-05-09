package lk.recruitment_management.asset.applicant_gazette_interview.service;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette_interview.dao.ApplicantGazetteInterviewDao;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
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
public class ApplicantGazetteInterviewService implements AbstractService< ApplicantGazetteInterview, Integer> {
    private final ApplicantGazetteInterviewDao applicantGazetteInterviewDao;

    @Autowired
    public ApplicantGazetteInterviewService(ApplicantGazetteInterviewDao applicantGazetteInterviewDao) {

        this.applicantGazetteInterviewDao = applicantGazetteInterviewDao;
    }

    @Cacheable
    public List< ApplicantGazetteInterview > findAll() {
        return applicantGazetteInterviewDao.findAll();
    }

    @Cacheable
    public ApplicantGazetteInterview findById(Integer id) {
        return applicantGazetteInterviewDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "applicantInterview", allEntries = true )},
        put = {@CachePut( value = "applicantInterview", key = "#applicantGazetteInterview.id" )} )
    @Transactional
    public ApplicantGazetteInterview persist(ApplicantGazetteInterview applicantGazetteInterview) {
        return applicantGazetteInterviewDao.save(applicantGazetteInterview);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        applicantGazetteInterviewDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< ApplicantGazetteInterview > search(ApplicantGazetteInterview applicantGazetteInterview) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantGazetteInterview > districtExample = Example.of(applicantGazetteInterview, matcher);
        return applicantGazetteInterviewDao.findAll(districtExample);
    }

    public List< ApplicantGazetteInterview > findByApplicantGazette(ApplicantGazette applicantGazette){
        return applicantGazetteInterviewDao.findByApplicantGazette(applicantGazette);
    }

}
