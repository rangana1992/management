package lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.service;


import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.dao.ApplicantGazetteSisCrdCidDao;
import lk.recruitment_management.asset.applicant_gazette_sis_crd_cid_result.entity.ApplicantGazetteSisCrdCid;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service

public class ApplicantGazetteSisCrdCidService implements AbstractService< ApplicantGazetteSisCrdCid, Integer > {
  private final ApplicantGazetteSisCrdCidDao applicantGazetteSisCrdCidDao;

  @Autowired
  public ApplicantGazetteSisCrdCidService(ApplicantGazetteSisCrdCidDao applicantGazetteSisCrdCidDao) {

    this.applicantGazetteSisCrdCidDao = applicantGazetteSisCrdCidDao;
  }


  public List< ApplicantGazetteSisCrdCid > findAll() {
    return applicantGazetteSisCrdCidDao.findAll();
  }


  public ApplicantGazetteSisCrdCid findById(Integer id) {
    return applicantGazetteSisCrdCidDao.getOne(id);
  }


  public ApplicantGazetteSisCrdCid persist(ApplicantGazetteSisCrdCid applicantGazetteSisCrdCid) {
    return applicantGazetteSisCrdCidDao.save(applicantGazetteSisCrdCid);
  }

  public boolean delete(Integer id) {
    applicantGazetteSisCrdCidDao.deleteById(id);
    return false;
  }

  public List< ApplicantGazetteSisCrdCid > search(ApplicantGazetteSisCrdCid applicantGazetteSisCrdCid) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< ApplicantGazetteSisCrdCid > districtExample = Example.of(applicantGazetteSisCrdCid, matcher);
    return applicantGazetteSisCrdCidDao.findAll(districtExample);
  }


  public List< ApplicantGazetteSisCrdCid > findByApplicantGazette(ApplicantGazette applicantGazette) {
    return applicantGazetteSisCrdCidDao.findByApplicantGazette(applicantGazette);
  }
}
