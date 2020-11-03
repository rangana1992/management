package lk.recruitment.management.asset.police_station.dao;


import lk.recruitment.management.asset.ag_office.entity.AgOffice;
import lk.recruitment.management.asset.police_station.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliceStationDao extends JpaRepository<PoliceStation, Integer> {
    List<PoliceStation> findByAgOffice(AgOffice agOffice);

}
