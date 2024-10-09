package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Campaign> {
    int countByCampaign(Campaign campaign);
}