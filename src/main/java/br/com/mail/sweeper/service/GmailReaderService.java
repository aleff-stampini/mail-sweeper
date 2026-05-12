package br.com.mail.sweeper.service;

import br.com.mail.sweeper.model.EmailData;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailReaderService {

  private final Gmail gmail;

  public List<EmailData> getPromotionEmails() throws Exception {
    ListMessagesResponse response =
        gmail.users().messages().list("me").setQ("category:promotions older_than:7d").execute();

    if (response.getMessages() == null) {
      return List.of();
    }

    List<EmailData> emails = new ArrayList<>();
    for (Message message : response.getMessages()) {
      Message fullMessage = gmail.users().messages().get("me", message.getId()).execute();

      emails.add(mapToEmailData(fullMessage));
    }

    return emails;
  }

  private EmailData mapToEmailData(Message message) {
    String subject = "";
    String from = "";

    for (MessagePartHeader header : message.getPayload().getHeaders()) {

      if ("Subject".equalsIgnoreCase(header.getName())) subject = header.getValue();

      if ("From".equalsIgnoreCase(header.getName())) from = header.getValue();
    }

    return EmailData.builder()
        .id(message.getId())
        .subject(subject)
        .from(from)
        .snippet(message.getSnippet())
        .labels(message.getLabelIds())
        .build();
  }
}
