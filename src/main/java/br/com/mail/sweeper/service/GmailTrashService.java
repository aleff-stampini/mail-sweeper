package br.com.mail.sweeper.service;

import com.google.api.services.gmail.Gmail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GmailTrashService {

  private final Gmail gmail;

  public void moveToTrash(String messageId) throws Exception {
    gmail.users().messages().trash("me", messageId).execute();
  }
}
