- Tracking Service -
Este projeto é um sistema de rastreamento de pacotes desenvolvido com Java 21 e Spring Boot 3.4.2. Ele processa eventos logísticos de pacotes, interage com o banco de dados MySQL e utiliza RabbitMQ para mensageria.

- Tecnologias Utilizadas - 
Java 21
Spring Boot 3.4.2
Maven 3.9.9 (Versões inferiores podem funcionar, mas a recomendação é 3.8.0 ou superior)
Docker & Docker Compose
MySQL 8.0
RabbitMQ
WireMock (para mocks de APIs externas)

- Como Rodar o Projeto Localmente -
no terminal: git clone https://github.com/FabioLettieri/tracking-service.git

Acesse o repositorio em que voce baixou o projeto. 
 - Suba o Docker Desktop.
 - Rode o script: docker-compose up -d

Obs.: ao rodar o script voce subira alguns containers.

voce pode subir a aplicação pelo Boot Dashboard do STS.

Após subir voce poderá: 
 - Acessar o Swagger UI para visualizar e fazer testes dos endpoints expostos: http://localhost:8080/swagger-ui.html
 - Acessar o RabbitMQ Dashboard para conferir as filas que consomem enviando mensagens para criação e tambem para atualizar pedidos a muito tempo parados.
