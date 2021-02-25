package lk.recruitment_management.asset.ag_office.service;


import lk.recruitment_management.asset.ag_office.dao.AgOfficeDao;
import lk.recruitment_management.asset.ag_office.entity.AgOffice;
import lk.recruitment_management.asset.district.entity.District;
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
@CacheConfig(cacheNames = "agOffice")
public class AgOfficeService implements AbstractService<AgOffice, Integer> {
    private final AgOfficeDao agOfficeDao;

    @Autowired
    public AgOfficeService(AgOfficeDao agOfficeDao) {

        this.agOfficeDao = agOfficeDao;
    }

    @Cacheable
    public List<AgOffice> findAll() {
        return agOfficeDao.findAll();
    }

    @Cacheable
    public AgOffice findById(Integer id) {
        return agOfficeDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "agOffice", allEntries = true)},
            put = {@CachePut(value = "agOffice", key = "#agOffice.id")})
    @Transactional
    public AgOffice persist(AgOffice agOffice) {
        return agOfficeDao.save(agOffice);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        agOfficeDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<AgOffice> search(AgOffice agOffice) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<AgOffice> agOfficeExample = Example.of(agOffice, matcher);
        return agOfficeDao.findAll(agOfficeExample);
    }

    public boolean isAgOfficePresent(AgOffice agOffice) {
        return agOfficeDao.equals(agOffice);
    }

    public List<AgOffice> findByDistrict(District district) {
        return agOfficeDao.findByDistrict(district);
    }
}
