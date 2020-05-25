package lk.recruitment.management.asset.agOffice.dao;


import lk.recruitment.management.asset.agOffice.entity.AgOffice;
import lk.recruitment.management.asset.applicant.entity.Applicant;
import lk.recruitment.management.asset.district.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgOfficeDao extends JpaRepository<AgOffice, Integer> {

    List<AgOffice> findByDistrict(District district);
}
