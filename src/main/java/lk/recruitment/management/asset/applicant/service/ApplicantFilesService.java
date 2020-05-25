package lk.recruitment.management.asset.applicant.service;


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
@CacheConfig( cacheNames = "applicantFile" )
public class ApplicantFilesService {
    private final ApplicantFilesDao applicantFileDao;

    @Autowired
    public ApplicantFilesService(ApplicantFilesDao applicantFileDao) {
        this.applicantFileDao = applicantFileDao;
    }

    public ApplicantFiles findByName(String filename) {
        return applicantFileDao.findByName(filename);
    }

    public void persist(ApplicantFiles storedFile) {
        applicantFileDao.save(storedFile);
    }


    public List< ApplicantFiles > search(ApplicantFiles applicantFile) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< ApplicantFiles > applicantFileExample = Example.of(applicantFile, matcher);
        return applicantFileDao.findAll(applicantFileExample);
    }

    public ApplicantFiles findById(Integer id) {
        return applicantFileDao.getOne(id);
    }

    public ApplicantFiles findByNewID(String filename) {
        return applicantFileDao.findByNewId(filename);
    }

    @Cacheable
    public List<FileInfo> applicantFileDownloadLinks(Applicant applicant) {
        return applicantFileDao.findByApplicantOrderByIdDesc(applicant)
                .stream()
                .map(applicantFile -> {
                    String filename = applicantFile.getName();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(EmployeeController.class, "downloadFile", applicantFile.getNewId())
                            .build()
                            .toString();
                    return new FileInfo(filename, applicantFile.getCreatedAt(), url);
                })
                .collect(Collectors.toList());
    }
}
