package br.com.mail.sweeper.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailConfig {

  private static final String APPLICATION_NAME = "MailCleanerAI";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final List<String> SCOPES = List.of(GmailScopes.GMAIL_MODIFY);

  @Bean
  public Gmail gmailService() throws Exception {
    InputStream input = GmailConfig.class.getResourceAsStream("/credentials.json");

    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(input));

    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
            .setAccessType("offline")
            .build();

    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

    return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME)
        .build();
  }
}
