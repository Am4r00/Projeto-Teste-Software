Projeto Teste de Software — Cadastro de Produtos e Usuários

API em Spring Boot 3.5 (Java 21) para cadastro de Produtos e Usuários. Persistência com JPA/Hibernate, H2 em memória por padrão, testes com JUnit 5 + Testcontainers (PostgreSQL). Uso Maven Wrapper, MapStruct e Bean Validation.

Artefato: cadastro-0.0.1-SNAPSHOT.jar

Porta: 8080

H2 Console: /h2-console

1) Requisitos

JDK 21

Docker (recomendado para testes com Testcontainers)

Git

Maven não é necessário (uso mvnw / mvnw.cmd)

2) Instalação e Setup
git clone https://github.com/Am4r00/Projeto-Teste-Software.git
cd Projeto-Teste-Software
./mvnw -q -DskipTests package   # macOS/Linux
# ou
mvnw.cmd -q -DskipTests package # Windows

3) Execução
3.1 H2 (padrão)
./mvnw spring-boot:run
# ou
java -jar target/cadastro-0.0.1-SNAPSHOT.jar


API: http://localhost:8080

H2 Console: http://localhost:8080/h2-console

JDBC: jdbc:h2:mem:sistemacadastro

Usuário: sa | Senha: (vazio)

3.2 PostgreSQL (local)
docker compose -f src/test/docker-compose.yml up -d
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb \
SPRING_DATASOURCE_USERNAME=test \
SPRING_DATASOURCE_PASSWORD=test \
./mvnw spring-boot:run


No Windows, exporte as variáveis conforme seu shell.

4) Configuração

src/main/resources/application.properties (dev/H2):

spring.datasource.url=jdbc:h2:mem:sistemacadastro;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


Variáveis úteis:

# PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb
SPRING_DATASOURCE_USERNAME=test
SPRING_DATASOURCE_PASSWORD=test

# Serviço de CEP (ViaCEP por padrão)
EXTERNAL_CEP_API_URL=https://viacep.com.br/ws

5) Testes
./mvnw test                      # suíte completa
./mvnw -Dtest=ClasseTest test    # classe específica
./mvnw -Dtest=Classe#metodo test # método específico


Rodar testes em Docker (CI local):

docker compose -f src/test/docker-compose.ci.yml up --build --abort-on-container-exit --exit-code-from tests
docker compose -f src/test/docker-compose.ci.yml down -v


Windows (atalho):

.\scripts\run-tests-in-docker.ps1

6) Endpoints
6.1 Produtos — /api/produtos

POST /api/produtos

{ "nome": "Caderno", "descricao": "200 folhas", "quantidade": 10, "valor": 12.5 }


201 criado | 400 validação

GET /api/produtos → lista

PUT /api/produtos/{id} → atualiza

DELETE /api/produtos/{id} → remove

Validações (DTO): nome obrigatório • quantidade >= 1 • valor >= 0

6.2 Usuários — /api/usuarios

GET /api/usuarios | GET /api/usuarios/{id}

POST /api/usuarios

{ "nome": "Ana Maria", "email": "ana@example.com", "cpf": "12345678901", "cep": "01001-000" }


Preenche logradouro, cidade, estado via CEP válido.

PUT /api/usuarios/{id} | DELETE /api/usuarios/{id}

Restrições:
nome 2–100 • email válido/único • cpf 11 dígitos/único • cep até 9 • estado 2 • logradouro ≤ 200 • cidade ≤ 100

7) Build e Empacotamento
./mvnw clean verify                 # build + testes
./mvnw clean package -DskipTests    # build rápido
java -jar target/cadastro-0.0.1-SNAPSHOT.jar

8) Estrutura
src/
├─ main/
│  ├─ java/com/sistema/cadastro/    # controllers, serviços, entidades, mappers
│  └─ resources/                    # configs (H2, JPA)
└─ test/
   ├─ java/com/sistema/cadastro/    # testes
   ├─ docker-compose.yml            # Postgres local p/ dev/test
   └─ docker-compose.ci.yml         # pipeline de testes em Docker

9) Tecnologias

Spring Boot, Spring Web, WebClient, Spring Data JPA, Bean Validation, MapStruct, Lombok, H2/PostgreSQL, JUnit 5, Testcontainers.

10) Dicas

Permissão do wrapper (Unix): chmod +x mvnw

Dependências: ./mvnw dependency:tree

Logs: --debug no Maven ou logging.level.root=DEBUG

11) Contribuição

git checkout -b feat/minha-feature

git commit -m "feat: descrição"

git push origin feat/minha-feature

Abra um PR

12) Licença

Defina a licença (ex.: MIT) e adicione o arquivo LICENSE na raiz.
