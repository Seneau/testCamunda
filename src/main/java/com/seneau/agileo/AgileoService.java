package com.seneau.agileo;


import com.seneau.agileo.dtos.CongeDTO;
import com.seneau.agileo.dtos.WebhookDTO;

import java.util.List;

public interface AgileoService {

    List<CongeDTO> getAllConge();

    CongeDTO getCongeByAgent(Long id,String type);

    void sendWebhook(WebhookDTO webhookDTO);

}
