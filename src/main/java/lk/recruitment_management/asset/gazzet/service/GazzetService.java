package lk.recruitment_management.asset.gazzet.service;


import lk.recruitment_management.asset.gazzet.dao.GazzetDao;
import lk.recruitment_management.asset.gazzet.entity.Gazzet;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GazzetService implements AbstractService< Gazzet, Integer> {

    private final GazzetDao gazzetDao;
    @Autowired
    public GazzetService(GazzetDao gazzetDao){
        this.gazzetDao = gazzetDao;
    }



    public List< Gazzet > findAll() {
        return gazzetDao.findAll();
    }


    public Gazzet findById(Integer id) {
        return gazzetDao.getOne(id);
    }


    public Gazzet persist(Gazzet gazzet) {
        return gazzetDao.save(gazzet);
    }


    public boolean delete(Integer id) {
        gazzetDao.deleteById(id);
        return false;
    }


    public List< Gazzet > search(Gazzet gazzet) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Gazzet > sampleCollectingTubeExample = Example.of(gazzet, matcher);
        return gazzetDao.findAll(sampleCollectingTubeExample);
    }
}
