package lk.recruitment.management.asset.applicant.dao;


import lk.recruitment.management.asset.applicant.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantDao extends JpaRepository<Applicant, Integer> {
    Applicant findFirstByOrderByIdDesc();

    Applicant findByNic(String nic);
}
