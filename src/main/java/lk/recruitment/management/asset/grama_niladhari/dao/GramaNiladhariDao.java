package lk.recruitment.management.asset.grama_niladhari.dao;



import lk.recruitment.management.asset.grama_niladhari.entity.GramaNiladhari;
import lk.recruitment.management.asset.police_station.entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GramaNiladhariDao extends JpaRepository<GramaNiladhari, Integer> {
//select * from district where province = ?1
    List<GramaNiladhari> findByPoliceStation(PoliceStation policeStation);

}
