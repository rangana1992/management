package lk.recruitment_management.asset.gazette.dao;


import lk.recruitment_management.asset.gazette.entity.Gazette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GazetteDao extends JpaRepository< Gazette, Integer> {
}
