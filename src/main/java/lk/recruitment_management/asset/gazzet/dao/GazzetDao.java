package lk.recruitment_management.asset.gazzet.dao;


import lk.recruitment_management.asset.gazzet.entity.Gazzet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GazzetDao extends JpaRepository< Gazzet, Integer> {
}
