package br.com.infnet.notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NotificacaoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificacaoServiceApplication.class, args);
	}
}
