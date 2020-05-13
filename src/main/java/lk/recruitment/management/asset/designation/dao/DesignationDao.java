package lk.recruitment.management.asset.designation.dao;


import lk.recruitment.management.asset.designation.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignationDao extends JpaRepository<Designation, Integer> {

/*//select * from district where province = ?1
    List<Qualification> findByProvince(Province province);*/

}
