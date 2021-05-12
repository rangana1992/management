package lk.recruitment_management.asset.applicant_gazette_interview_result.service;




import lk.recruitment_management.asset.applicant_gazette_interview_result.dao.ApplicantGazetteInterViewResultDao;
import lk.recruitment_management.asset.applicant_gazette_interview_result.entity.ApplicantGazetteInterviewResult;
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
@CacheConfig(cacheNames = "applicantInterviewResult")
public class ApplicantGazetteInterviewResultService implements AbstractService< ApplicantGazetteInterviewResult, Integer> {
    private final ApplicantGazetteInterViewResultDao applicantGazetteInterViewResultDao;

    @Autowired
    public ApplicantGazetteInterviewResultService(ApplicantGazetteInterViewResultDao applicantGazetteInterViewResultDao) {

        this.applicantGazetteInterViewResultDao = applicantGazetteInterViewResultDao;
    }

    @Cacheable
    public List< ApplicantGazetteInterviewResult > findAll() {
        return applicantGazetteInterViewResultDao.findAll();
    }

    @Cacheable
    public ApplicantGazetteInterviewResult findById(Integer id) {
        return applicantGazetteInterViewResultDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "applicantInterviewResult", allEntries = true)},
            put = {@CachePut(value = "applicantInterviewResult", key = "#applicantGazetteInterviewResult.id")})
    @Transactional
    public ApplicantGazetteInterviewResult persist(ApplicantGazetteInterviewResult applicantGazetteInterviewResult) {
        return applicantGazetteInterViewResultDao.save(applicantGazetteInterviewResult);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        applicantGazetteInterViewResultDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< ApplicantGazetteInterviewResult > search(ApplicantGazetteInterviewResult applicantGazetteInterviewResult) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantGazetteInterviewResult > districtExample = Example.of(applicantGazetteInterviewResult, matcher);
        return applicantGazetteInterViewResultDao.findAll(districtExample);
    }


}
