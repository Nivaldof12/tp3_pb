package br.com.infnet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.infnet.client.NotificacaoClient;
import br.com.infnet.messaging.EventPublisher;

@SpringBootTest
@ActiveProfiles("test")
class InfnetApplicationTests {

	@MockBean
	private NotificacaoClient notificacaoClient;

	@MockBean
	private EventPublisher eventPublisher;

	@Test
	void contextLoads() {
	}

}
