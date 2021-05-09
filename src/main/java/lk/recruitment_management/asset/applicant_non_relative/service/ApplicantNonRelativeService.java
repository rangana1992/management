package lk.recruitment_management.asset.applicant_non_relative.service;


import lk.recruitment_management.asset.applicant_non_relative.dao.ApplicantNonRelativeDao;
import lk.recruitment_management.asset.applicant_non_relative.entity.ApplicantNonRelative;
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
@CacheConfig( cacheNames = "nonRelative" )
public class ApplicantNonRelativeService implements AbstractService< ApplicantNonRelative, Integer > {

    private final ApplicantNonRelativeDao applicantNonRelativeDao;

    @Autowired
    public ApplicantNonRelativeService(ApplicantNonRelativeDao applicantNonRelativeDao) {
        this.applicantNonRelativeDao = applicantNonRelativeDao;
    }

    @Cacheable
    public List< ApplicantNonRelative > findAll() {
        return applicantNonRelativeDao.findAll();
    }

    @Cacheable
    public ApplicantNonRelative findById(Integer id) {
        return applicantNonRelativeDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "nonRelative", allEntries = true )},
            put = {@CachePut( value = "nonRelative", key = "#applicantNonRelative.id" )} )
    @Transactional
    public ApplicantNonRelative persist(ApplicantNonRelative applicantNonRelative) {
        return applicantNonRelativeDao.save(applicantNonRelative);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        applicantNonRelativeDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< ApplicantNonRelative > search(ApplicantNonRelative applicantNonRelative) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantNonRelative > nonRelativeExample = Example.of(applicantNonRelative, matcher);
        return applicantNonRelativeDao.findAll(nonRelativeExample);
    }

    public boolean isNonRelativePresent(ApplicantNonRelative applicantNonRelative) {
        return applicantNonRelativeDao.equals(applicantNonRelative);
    }

}
