package br.com.infnet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.infnet.client.NotificacaoClient;

@SpringBootTest
@ActiveProfiles("test")
class InfnetApplicationTests {

	@MockBean
	private NotificacaoClient notificacaoClient;

	@Test
	void contextLoads() {
	}

}
