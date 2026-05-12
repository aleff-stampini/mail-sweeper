package br.com.mail.sweeper.model.response;

import lombok.Data;

@Data
public class AIClassificationResponse {
  private boolean shouldDelete;
  private double confidence;
  private String reason;
}
