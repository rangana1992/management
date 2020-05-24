package lk.recruitment.management.asset.policeStation.Dao;


import lk.recruitment.management.asset.agOffice.entity.AgOffice;
import lk.recruitment.management.asset.policeStation.Entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoliceStationDao extends JpaRepository<PoliceStation, Integer> {
    List<PoliceStation> findByAgOffice(AgOffice agOffice);

}
