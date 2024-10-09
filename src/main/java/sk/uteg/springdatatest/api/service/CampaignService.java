package sk.uteg.springdatatest.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.model.OptionSummary;
import sk.uteg.springdatatest.api.model.QuestionSummary;
import sk.uteg.springdatatest.api.repository.AnswerRepository;
import sk.uteg.springdatatest.api.repository.CampaignRepository;
import sk.uteg.springdatatest.api.repository.FeedbackRepository;
import sk.uteg.springdatatest.db.model.Answer;
import sk.uteg.springdatatest.db.model.Campaign;
import sk.uteg.springdatatest.db.model.Option;
import sk.uteg.springdatatest.db.model.Question;

import java.math.BigDecimal;
import java.util.*;

import static sk.uteg.springdatatest.db.model.QuestionType.CHOICE;
import static sk.uteg.springdatatest.db.model.QuestionType.RATING;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final FeedbackRepository feedbackRepository;
    private final AnswerRepository answerRepository;

    public CampaignSummary getCampaignSummary(UUID campaignId) {
        Optional<Campaign> campaignOptional = this.campaignRepository.findById(campaignId);
        if (campaignOptional.isPresent()) {
            Campaign campaign = campaignOptional.get();
            int totalFeedbacks = this.feedbackRepository.countByCampaign(campaign);

            List<QuestionSummary> questionSummaries = new ArrayList<>();

            int counter = 0;
            for (Question question : campaign.getQuestions()) {
                QuestionSummary questionSummary = new QuestionSummary();
                questionSummary.setType(question.getType());
                questionSummary.setName("Question " + ++counter);

                if (question.getType().equals(RATING)) {
                    questionSummary.setRatingAverage(findAverageRating(question));
                    questionSummary.setOptionSummaries(new ArrayList<>()); // Оставляем пустым для RATING
                } else if (question.getType().equals(CHOICE)) {
                    questionSummary.setRatingAverage(BigDecimal.ZERO); // Для CHOICE всегда ноль
                    questionSummary.setOptionSummaries(createOptionSummaries(question));

                }
                questionSummaries.add(questionSummary);

            }
            CampaignSummary campaignSummary = new CampaignSummary();
            campaignSummary.setTotalFeedbacks(totalFeedbacks);
            campaignSummary.setQuestionSummaries(questionSummaries);

            return campaignSummary;
        } else {
            throw new RuntimeException("Campaign not found!");
        }
    }

    private List<OptionSummary> createOptionSummaries(Question question) {
        List<OptionSummary> optionSummaries = new ArrayList<>();

        Map<String, Integer> optionSelectedCount =  new HashMap<>();
        question.getOptions().forEach(option -> optionSelectedCount.put(option.getText(), 0));

        List<Answer> answers = this.answerRepository.findByFeedbackCampaign(question.getCampaign());
        answers.forEach(answer -> {
            if (answer.getSelectedOptions() != null) {
                answer.getSelectedOptions().forEach(option ->
                        optionSelectedCount.put(option.getText(), optionSelectedCount.get(option.getText()) + 1));
            }
        });

        for (Option option : question.getOptions()) {
            OptionSummary optionSummary = new OptionSummary();
            optionSummary.setText(option.getText());
            optionSummary.setOccurrences(optionSelectedCount.get(option.getText()));
            optionSummaries.add(optionSummary);
        }

        return optionSummaries;
    }

    private BigDecimal findAverageRating(Question question) {
        List<Answer> answers = this.answerRepository.findByFeedbackCampaign(question.getCampaign());

        OptionalDouble averageOptional = answers.stream()
                .filter(answer -> answer.getRatingValue() != 0)
                .mapToInt(Answer::getRatingValue)
                .average();

        return averageOptional.isPresent()
                ? BigDecimal.valueOf(averageOptional.getAsDouble()) : BigDecimal.ZERO;
    }
}
