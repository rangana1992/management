package lk.recruitment.management.asset.gramaNiladhari.dao;



import lk.recruitment.management.asset.gramaNiladhari.entity.GramaNiladhari;
import lk.recruitment.management.asset.policeStation.Entity.PoliceStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GramaNiladhariDao extends JpaRepository<GramaNiladhari, Integer> {
//select * from district where province = ?1
    List<GramaNiladhari> findByPoliceStation(PoliceStation policeStation);

}
