package lk.recruitment_management.asset.applicant_gazette_interview.service;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette_interview.dao.ApplicantGazetteInterviewDao;
import lk.recruitment_management.asset.applicant_gazette_interview.entity.ApplicantGazetteInterview;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service

public class ApplicantGazetteInterviewService implements AbstractService< ApplicantGazetteInterview, Integer> {
    private final ApplicantGazetteInterviewDao applicantGazetteInterviewDao;

    @Autowired
    public ApplicantGazetteInterviewService(ApplicantGazetteInterviewDao applicantGazetteInterviewDao) {

        this.applicantGazetteInterviewDao = applicantGazetteInterviewDao;
    }

    public List< ApplicantGazetteInterview > findAll() {
        return applicantGazetteInterviewDao.findAll();
    }


    public ApplicantGazetteInterview findById(Integer id) {
        return applicantGazetteInterviewDao.getOne(id);
    }


    public ApplicantGazetteInterview persist(ApplicantGazetteInterview applicantGazetteInterview) {
        return applicantGazetteInterviewDao.save(applicantGazetteInterview);
    }

    public boolean delete(Integer id) {
        applicantGazetteInterviewDao.deleteById(id);
        return false;
    }


    public List< ApplicantGazetteInterview > search(ApplicantGazetteInterview applicantGazetteInterview) {
        ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantGazetteInterview > districtExample = Example.of(applicantGazetteInterview, matcher);
        return applicantGazetteInterviewDao.findAll(districtExample);
    }

    public List< ApplicantGazetteInterview > findByApplicantGazette(ApplicantGazette applicantGazette){
        return applicantGazetteInterviewDao.findByApplicantGazette(applicantGazette);
    }

}
