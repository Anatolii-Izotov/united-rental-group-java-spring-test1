package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.UUID;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {}
