package lk.recruitment_management.asset.applicant_gazzet.service;



import lk.recruitment_management.asset.applicant_gazzet.dao.ApplicantGazzetDao;
import lk.recruitment_management.asset.applicant_gazzet.entity.ApplicantGazzet;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantGazzetService implements AbstractService< ApplicantGazzet, Integer> {

    private final ApplicantGazzetDao applicantGazzetDao;
    @Autowired
    public ApplicantGazzetService(ApplicantGazzetDao applicantGazzetDao){
        this.applicantGazzetDao = applicantGazzetDao;
    }



    public List< ApplicantGazzet > findAll() {
        return applicantGazzetDao.findAll();
    }


    public ApplicantGazzet findById(Integer id) {
        return applicantGazzetDao.getOne(id);
    }


    public ApplicantGazzet persist(ApplicantGazzet applicantGazzet) {
        return applicantGazzetDao.save(applicantGazzet);
    }


    public boolean delete(Integer id) {
        applicantGazzetDao.deleteById(id);
        return false;
    }


    public List< ApplicantGazzet > search(ApplicantGazzet applicantGazzet) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantGazzet > sampleCollectingTubeExample = Example.of(applicantGazzet, matcher);
        return applicantGazzetDao.findAll(sampleCollectingTubeExample);
    }
}
