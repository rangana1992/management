package lk.recruitment.management.asset.employee.dao;


import lk.recruitment.management.asset.employee.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectDao extends JpaRepository<Subject, Integer> {

/*//select * from district where province = ?1
    List<Subject> findByProvince(Province province);*/

}
