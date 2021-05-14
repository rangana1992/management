package lk.recruitment_management.asset.applicant_gazette.service;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant_gazette.dao.ApplicantGazetteDao;
import lk.recruitment_management.asset.applicant_gazette.entity.ApplicantGazette;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplicantGazetteStatus;
import lk.recruitment_management.asset.applicant_gazette.entity.enums.ApplyingRank;
import lk.recruitment_management.asset.gazette.entity.Gazette;
import lk.recruitment_management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicantGazetteService implements AbstractService< ApplicantGazette, Integer > {

  private final ApplicantGazetteDao applicantGazetteDao;

  @Autowired
  public ApplicantGazetteService(ApplicantGazetteDao applicantGazetteDao) {
    this.applicantGazetteDao = applicantGazetteDao;
  }

  public List< ApplicantGazette > findAll() {
    return applicantGazetteDao.findAll();
  }

  public ApplicantGazette findById(Integer id) {
    return applicantGazetteDao.getOne(id);
  }

  public ApplicantGazette persist(ApplicantGazette applicantGazette) {
    return applicantGazetteDao.save(applicantGazette);
  }

  public boolean delete(Integer id) {
    applicantGazetteDao.deleteById(id);
    return false;
  }

  public List< ApplicantGazette > search(ApplicantGazette applicantGazette) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< ApplicantGazette > sampleCollectingTubeExample = Example.of(applicantGazette, matcher);
    return applicantGazetteDao.findAll(sampleCollectingTubeExample);
  }

  public List< ApplicantGazette > findByApplicant(Applicant applicant) {
    return applicantGazetteDao.findByApplicant(applicant);
  }

  public List< ApplicantGazette > findByCreatedAtIsBetweenAndApplicantGazetteStatusAndApplyingRank(LocalDateTime dateTimeToLocalDateStartInDay, LocalDateTime dateTimeToLocalDateEndInDay, ApplicantGazetteStatus applicantGazetteStatus, ApplyingRank applyingRank) {
    return applicantGazetteDao.findByCreatedAtIsBetweenAndApplicantGazetteStatusAndApplyingRank(dateTimeToLocalDateEndInDay, dateTimeToLocalDateEndInDay, applicantGazetteStatus, applyingRank);
  }

  public int countByApplicantGazetteStatus(ApplicantGazetteStatus applicantGazetteStatus) {
    return applicantGazetteDao.countByApplicantGazetteStatus(applicantGazetteStatus);
  }

  public int countByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus applicantGazetteStatus, Gazette gazette) {
    return applicantGazetteDao.countByApplicantGazetteStatusAndGazette(applicantGazetteStatus, gazette);
  }

  public List< ApplicantGazette > findByApplicantGazetteStatusAndGazette(ApplicantGazetteStatus applicantGazetteStatus, Gazette gazette) {
    return applicantGazetteDao.findByApplicantGazetteStatusAndGazette(applicantGazetteStatus, gazette);
  }

  public ApplicantGazette findByGazetteAndApplicant(Gazette gazette, Applicant applicant) {
    return applicantGazetteDao.findByGazetteAndApplicant(gazette, applicant);
  }

  public List< ApplicantGazette > findByGazettes(List< Gazette > gazettes) {
    List< ApplicantGazette > applicantGazettes = new ArrayList<>();
    gazettes.forEach(x -> applicantGazettes.addAll(applicantGazetteDao.findByGazette(x)));
    return applicantGazettes;
  }

  public ApplicantGazette lastApplicantGazette() {
    return applicantGazetteDao.findFirstByOrderByIdDesc();
  }
}
