package lk.recruitment.management.asset.grama_niladhari.service;


import lk.recruitment.management.asset.grama_niladhari.dao.GramaNiladhariDao;
import lk.recruitment.management.asset.grama_niladhari.entity.GramaNiladhari;
import lk.recruitment.management.asset.police_station.entity.PoliceStation;
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
@CacheConfig(cacheNames = "policeStation")
public class GramaNiladhariService implements AbstractService<GramaNiladhari, Integer> {
    private final GramaNiladhariDao policeStationDao;

    @Autowired
    public GramaNiladhariService(GramaNiladhariDao policeStationDao) {

        this.policeStationDao = policeStationDao;
    }

    @Cacheable
    public List<GramaNiladhari> findAll() {
        return policeStationDao.findAll();
    }

    @Cacheable
    public GramaNiladhari findById(Integer id) {
        return policeStationDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "policeStation", allEntries = true)},
            put = {@CachePut(value = "policeStation", key = "#policeStation.id")})
    @Transactional
    public GramaNiladhari persist(GramaNiladhari policeStation) {
        return policeStationDao.save(policeStation);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        policeStationDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<GramaNiladhari> search(GramaNiladhari policeStation) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<GramaNiladhari> policeStationExample = Example.of(policeStation, matcher);
        return policeStationDao.findAll(policeStationExample);
    }

    public boolean isPoliceStationPresent(GramaNiladhari policeStation) {
        return policeStationDao.equals(policeStation);
    }


    public List<GramaNiladhari> findByPoliceStation(PoliceStation policeStation) {
        return policeStationDao.findByPoliceStation(policeStation);
    }

}
