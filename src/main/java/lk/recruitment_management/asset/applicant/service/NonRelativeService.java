package lk.recruitment_management.asset.applicant.service;


import lk.recruitment_management.asset.applicant.dao.NonRelativeDao;
import lk.recruitment_management.asset.applicant.entity.NonRelative;
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
public class NonRelativeService implements AbstractService<NonRelative, Integer > {

    private final NonRelativeDao nonRelativeDao;

    @Autowired
    public NonRelativeService(NonRelativeDao nonRelativeDao) {
        this.nonRelativeDao = nonRelativeDao;
    }

    @Cacheable
    public List< NonRelative > findAll() {
        return nonRelativeDao.findAll();
    }

    @Cacheable
    public NonRelative findById(Integer id) {
        return nonRelativeDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "nonRelative", allEntries = true )},
            put = {@CachePut( value = "nonRelative", key = "#nonRelative.id" )} )
    @Transactional
    public NonRelative persist(NonRelative nonRelative) {
        return nonRelativeDao.save(nonRelative);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        nonRelativeDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List< NonRelative > search(NonRelative nonRelative) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< NonRelative > nonRelativeExample = Example.of(nonRelative, matcher);
        return nonRelativeDao.findAll(nonRelativeExample);
    }

    public boolean isNonRelativePresent(NonRelative nonRelative) {
        return nonRelativeDao.equals(nonRelative);
    }

}
