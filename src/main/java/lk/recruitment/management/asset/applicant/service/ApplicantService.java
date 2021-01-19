package lk.recruitment.management.asset.applicant.service;


import lk.recruitment.management.asset.applicant.dao.ApplicantDao;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.Enum.ApplicantStatus;
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
@CacheConfig(cacheNames = "applicant")
public class ApplicantService implements AbstractService<Applicant, Integer> {

    private final ApplicantDao applicantDao;

    @Autowired
    public ApplicantService(ApplicantDao applicantDao) {
        this.applicantDao = applicantDao;
    }

    @Cacheable
    public List<Applicant> findAll() {
        return applicantDao.findAll();
    }

    @Cacheable
    public Applicant findById(Integer id) {
        return applicantDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "applicant", allEntries = true)},
            put = {@CachePut(value = "applicant", key = "#applicant.id")})
    @Transactional
    public Applicant persist(Applicant applicant) {
        if(applicant.getId()==null){
            applicant.setApplicantStatus(ApplicantStatus.P);
        }
        return applicantDao.save(applicant);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        applicantDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<Applicant> search(Applicant applicant) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Applicant> applicantExample = Example.of(applicant, matcher);
        return applicantDao.findAll(applicantExample);
    }

    public boolean isApplicantPresent(Applicant applicant) {
        return applicantDao.equals(applicant);
    }


    public Applicant lastApplicant() {
        return applicantDao.findFirstByOrderByIdDesc();
    }

    @Cacheable
    public Applicant findByNic(String nic) {
        return applicantDao.findByNic(nic);
    }

    public Applicant findByEmail(String email) {
        return applicantDao.findByEmail(email);
    }

    public int accordingToApplicantStatus(ApplicantStatus applicantStatus){
        return applicantDao.countByApplicantStatus(applicantStatus);
    }
}
