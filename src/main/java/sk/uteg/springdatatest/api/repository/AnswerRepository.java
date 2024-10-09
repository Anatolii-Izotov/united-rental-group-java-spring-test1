package sk.uteg.springdatatest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.uteg.springdatatest.db.model.Answer;
import sk.uteg.springdatatest.db.model.Campaign;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Campaign> {
    List<Answer> findByFeedbackCampaign(Campaign campaign);
}
