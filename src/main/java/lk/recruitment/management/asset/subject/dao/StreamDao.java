package lk.recruitment.management.asset.subject.dao;


import lk.recruitment.management.asset.subject.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamDao extends JpaRepository<Stream, Integer> {

/*//select * from district where province = ?1
    List<Subject> findByProvince(Province province);*/

}
