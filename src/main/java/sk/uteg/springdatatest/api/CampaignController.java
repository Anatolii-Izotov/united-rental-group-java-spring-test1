package sk.uteg.springdatatest.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sk.uteg.springdatatest.api.model.CampaignSummary;
import sk.uteg.springdatatest.api.repository.CampaignRepository;
import sk.uteg.springdatatest.api.service.CampaignService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController("campaign")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping("/summary/{uuid}")
    public ResponseEntity<CampaignSummary> getSummary(@PathVariable UUID uuid) {
        return ResponseEntity.ok(campaignService.getCampaignSummary(uuid));
    }
}
