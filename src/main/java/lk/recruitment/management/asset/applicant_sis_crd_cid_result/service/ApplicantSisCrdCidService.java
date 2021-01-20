package lk.recruitment.management.asset.applicant_sis_crd_cid_result.service;



import lk.recruitment.management.asset.applicant_sis_crd_cid_result.dao.ApplicantSisCrdCidDao;
import lk.recruitment.management.asset.applicant_sis_crd_cid_result.entity.ApplicantSisCrdCid;
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
@CacheConfig(cacheNames = "applicantSisCrdCid")
public class ApplicantSisCrdCidService implements AbstractService< ApplicantSisCrdCid, Integer> {
    private final ApplicantSisCrdCidDao applicantSisCrdCidDao;

    @Autowired
    public ApplicantSisCrdCidService(ApplicantSisCrdCidDao applicantSisCrdCidDao) {

        this.applicantSisCrdCidDao = applicantSisCrdCidDao;
    }

    @Cacheable
    public List<ApplicantSisCrdCid> findAll() {
        return applicantSisCrdCidDao.findAll();
    }

    @Cacheable
    public ApplicantSisCrdCid findById(Integer id) {
        return applicantSisCrdCidDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "applicantSisCrdCid", allEntries = true)},
            put = {@CachePut(value = "applicantSisCrdCid", key = "#applicantSisCrdCid.id")})
    @Transactional
    public ApplicantSisCrdCid persist(ApplicantSisCrdCid applicantSisCrdCid) {
        return applicantSisCrdCidDao.save(applicantSisCrdCid);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        applicantSisCrdCidDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<ApplicantSisCrdCid> search(ApplicantSisCrdCid applicantSisCrdCid) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ApplicantSisCrdCid> districtExample = Example.of(applicantSisCrdCid, matcher);
        return applicantSisCrdCidDao.findAll(districtExample);
    }


}
