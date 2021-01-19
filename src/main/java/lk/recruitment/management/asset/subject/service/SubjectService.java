package lk.recruitment.management.asset.subject.service;


import lk.recruitment.management.asset.subject.dao.SubjectDao;
import lk.recruitment.management.asset.subject.entity.Subject;
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
@CacheConfig(cacheNames = "subject")
public class SubjectService implements AbstractService<Subject, Integer> {

    private final SubjectDao subjectDao;

    @Autowired
    public SubjectService(SubjectDao subjectDao) {

        this.subjectDao = subjectDao;
    }

    @Cacheable
    public List<Subject> findAll() {

        return subjectDao.findAll();
    }

    @Cacheable
    public Subject findById(Integer id) {
        return subjectDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "subject", allEntries = true)},
            put = {@CachePut(value = "subject", key = "#subject.id")})
    @Transactional
    public Subject persist(Subject subject) {
        return subjectDao.save(subject);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        subjectDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<Subject> search(Subject subject) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Subject> subjectExample = Example.of(subject, matcher);
        return subjectDao.findAll(subjectExample);
    }

    public boolean isSubjectPresent(Subject subject) {
        return subjectDao.equals(subject);
    }


/*    public List<Subject> findByProvince(Province province) {
        return subjectDao.findByProvince(province);
    }*/
}
