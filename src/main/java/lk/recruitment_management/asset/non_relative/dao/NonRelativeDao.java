package lk.recruitment_management.asset.non_relative.dao;


import lk.recruitment_management.asset.non_relative.entity.NonRelative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonRelativeDao extends JpaRepository<NonRelative, Integer> {

}
