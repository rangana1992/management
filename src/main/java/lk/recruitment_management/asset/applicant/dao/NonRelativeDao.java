package lk.recruitment_management.asset.applicant.dao;


import lk.recruitment_management.asset.applicant.entity.NonRelative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonRelativeDao extends JpaRepository<NonRelative, Integer> {

}
