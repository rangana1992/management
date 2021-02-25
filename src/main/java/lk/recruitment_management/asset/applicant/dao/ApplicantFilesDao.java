package lk.recruitment_management.asset.applicant.dao;


import lk.recruitment_management.asset.applicant.entity.Applicant;
import lk.recruitment_management.asset.applicant.entity.ApplicantFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantFilesDao extends JpaRepository<ApplicantFiles, Integer > {
    List< ApplicantFiles > findByApplicantOrderByIdDesc(Applicant applicant);

    ApplicantFiles findByName(String filename);

    ApplicantFiles findByNewName(String filename);

    ApplicantFiles findByNewId(String filename);

    ApplicantFiles findByApplicant(Applicant savedApplicant);
}
