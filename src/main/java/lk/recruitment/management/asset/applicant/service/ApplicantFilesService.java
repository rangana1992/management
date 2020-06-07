package lk.recruitment.management.asset.applicant.service;


import lk.recruitment.management.asset.applicant.controller.ApplicantController;
import lk.recruitment.management.asset.applicant.dao.ApplicantFilesDao;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.applicant.entity.ApplicantFiles;
import lk.recruitment.management.asset.commonAsset.model.FileInfo;
import lk.recruitment.management.asset.employee.controller.EmployeeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "applicantFiles")
public class ApplicantFilesService {
    private final ApplicantFilesDao applicantFilesDao;

    @Autowired
    public ApplicantFilesService(ApplicantFilesDao applicantFilesDao) {
        this.applicantFilesDao = applicantFilesDao;
    }

    public ApplicantFiles findByName(String filename) {
        return applicantFilesDao.findByName(filename);
    }

    public void persist(ApplicantFiles storedFile) {
        applicantFilesDao.save(storedFile);
    }


    public List<ApplicantFiles> search(ApplicantFiles applicantFile) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ApplicantFiles> applicantFileExample = Example.of(applicantFile, matcher);
        return applicantFilesDao.findAll(applicantFileExample);
    }

    public ApplicantFiles findById(Integer id) {
        return applicantFilesDao.getOne(id);
    }

    public ApplicantFiles findByNewID(String filename) {
        return applicantFilesDao.findByNewId(filename);
    }

    @Cacheable
    public FileInfo applicantFileDownloadLinks(Applicant applicant) {
        FileInfo fileInfo = new FileInfo();
        ApplicantFiles applicantFiles = applicantFilesDao.findByApplicant(applicant);
        fileInfo.setFilename(applicantFiles.getName());
        fileInfo.setUrl(MvcUriComponentsBuilder
                .fromMethodName(ApplicantController.class, "downloadFile", applicantFiles.getNewId())
                .build()
                .toString());
        fileInfo.setCreateAt(applicantFiles.getCreatedAt());
        return fileInfo;
    }

    public ApplicantFiles findByApplicant(Applicant savedApplicant) {
        return applicantFilesDao.findByApplicant(savedApplicant);
    }
}
