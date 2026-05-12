package br.com.mail.sweeper.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailData {
  private String id;
  private String subject;
  private String from;
  private String snippet;
  private List<String> labels;
}
