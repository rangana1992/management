package lk.recruitment.management.asset.applicant_interview_result.service;




import lk.recruitment.management.asset.applicant_interview_result.dao.ApplicantInterViewResultDao;
import lk.recruitment.management.asset.applicant_interview_result.entity.ApplicantInterviewResult;
import lk.recruitment.management.asset.common_asset.model.Enum.Province;
import lk.recruitment.management.util.interfaces.AbstractService;
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
public class ApplicantInterviewResultService implements AbstractService< ApplicantInterviewResult, Integer> {
    private final ApplicantInterViewResultDao applicantInterViewResultDao;

    @Autowired
    public ApplicantInterviewResultService(ApplicantInterViewResultDao applicantInterViewResultDao) {

        this.applicantInterViewResultDao = applicantInterViewResultDao;
    }

    @Cacheable
    public List<ApplicantInterviewResult> findAll() {
        return applicantInterViewResultDao.findAll();
    }

    @Cacheable
    public ApplicantInterviewResult findById(Integer id) {
        return applicantInterViewResultDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "applicantInterviewResult", allEntries = true)},
            put = {@CachePut(value = "applicantInterviewResult", key = "#applicantInterviewResult.id")})
    @Transactional
    public ApplicantInterviewResult persist(ApplicantInterviewResult applicantInterviewResult) {
        return applicantInterViewResultDao.save(applicantInterviewResult);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        applicantInterViewResultDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<ApplicantInterviewResult> search(ApplicantInterviewResult applicantInterviewResult) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ApplicantInterviewResult> districtExample = Example.of(applicantInterviewResult, matcher);
        return applicantInterViewResultDao.findAll(districtExample);
    }


}
