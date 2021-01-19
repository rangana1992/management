package lk.recruitment.management.asset.applicant_interview.dao;


import lk.recruitment.management.asset.applicant_interview.entity.ApplicantInterview;
import lk.recruitment.management.asset.common_asset.model.Enum.Province;
import lk.recruitment.management.asset.district.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantInterviewDao extends JpaRepository< ApplicantInterview, Integer> {


}
