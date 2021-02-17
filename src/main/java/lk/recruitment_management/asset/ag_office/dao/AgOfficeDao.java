package lk.recruitment_management.asset.ag_office.dao;


import lk.recruitment_management.asset.ag_office.entity.AgOffice;
import lk.recruitment_management.asset.district.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgOfficeDao extends JpaRepository<AgOffice, Integer> {

    List<AgOffice> findByDistrict(District district);
}
