package br.com.mail.sweeper.classifier;

import br.com.mail.sweeper.model.EmailData;
import br.com.mail.sweeper.model.response.AIClassificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailAIClassifier {

  private final ChatClient chatClient;

  public AIClassificationResponse classify(EmailData email) {
    String prompt =
        """
                Você é um classificador de emails.

                Sua tarefa:
                decidir se um email deve ser deletado.
  
                Regras:

                DELETE:
                - promoções
                - marketing
                - newsletters
                - spam
                - publicidade
                - notificações irrelevantes

                KEEP:
                - trabalho
                - recrutadores
                - entrevistas
                - autenticação
                - segurança
                - banco
                - financeiro
                - clientes

                Retorne JSON:
                {
                  "shouldDelete": true/false,
                  "confidence": 0-1,
                  "reason": "motivo"
                }

                EMAIL:

                FROM:
                %s

                SUBJECT:
                %s

                SNIPPET:
                %s
                """
            .formatted(email.getFrom(), email.getSubject(), email.getSnippet());

    return chatClient.prompt().user(prompt).call().entity(AIClassificationResponse.class);
  }
}
