package br.com.mail.sweeper.scheduler;

import br.com.mail.sweeper.classifier.EmailAIClassifier;
import br.com.mail.sweeper.model.EmailData;
import br.com.mail.sweeper.model.response.AIClassificationResponse;
import br.com.mail.sweeper.service.GmailReaderService;
import br.com.mail.sweeper.service.GmailTrashService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxCleanerScheduler {

  private final GmailReaderService readerService;
  private final GmailTrashService trashService;

  private final EmailAIClassifier aiClassifier;

  @Scheduled(fixedDelay = 30000)
  public void cleanInbox() {
    try {
      List<EmailData> emails = readerService.getPromotionEmails();

      for (EmailData email : emails) {
        AIClassificationResponse result = aiClassifier.classify(email);

        log.info(
            "EMAIL ANALYZED \nSUBJECT: {} \nDELETE: {} \nCONFIDENCE: {} \nREASON: {} ",
            email.getSubject(),
            result.isShouldDelete(),
            result.getConfidence(),
            result.getReason());

        if (result.isShouldDelete() && result.getConfidence() > 0.90) {
          trashService.moveToTrash(email.getId());
          log.info("EMAIL DELETED {}", email.getSubject());
        }
      }
    } catch (Exception e) {
      log.error("Erro limpando inbox", e);
    }
  }
}
