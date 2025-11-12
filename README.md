Projeto Teste de Software — Cadastro de Produtos e Usuários

API em Spring Boot 3.5 (Java 21) para cadastro de Produtos e Usuários, usando JPA/Hibernate, H2 por padrão e Testcontainers (PostgreSQL) nos testes.
Artefato: cadastro-0.0.1-SNAPSHOT.jar • Porta: 8080 • H2 Console: /h2-console

1) Requisitos

JDK 21

Docker (recomendado para os testes)

Git

Maven Wrapper (mvnw / mvnw.cmd) — não preciso instalar Maven

2) Instalação / Setup
git clone https://github.com/Am4r00/Projeto-Teste-Software.git
cd Projeto-Teste-Software
./mvnw -q -DskipTests package   # macOS/Linux
# ou
mvnw.cmd -q -DskipTests package # Windows

3) Execução
H2 (padrão)
./mvnw spring-boot:run
# ou
java -jar target/cadastro-0.0.1-SNAPSHOT.jar


API: http://localhost:8080

H2: http://localhost:8080/h2-console

JDBC: jdbc:h2:mem:sistemacadastro • Usuário: sa • Senha: (vazio)

PostgreSQL (local)
docker compose -f src/test/docker-compose.yml up -d
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb \
SPRING_DATASOURCE_USERNAME=test \
SPRING_DATASOURCE_PASSWORD=test \
./mvnw spring-boot:run

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

# Postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb
SPRING_DATASOURCE_USERNAME=test
SPRING_DATASOURCE_PASSWORD=test

# Serviço de CEP
EXTERNAL_CEP_API_URL=https://viacep.com.br/ws

5) Testes
./mvnw test                       # suíte completa
./mvnw -Dtest=ClasseTest test     # classe específica
./mvnw -Dtest=Classe#metodo test  # método específico


CI/Docker local:

docker compose -f src/test/docker-compose.ci.yml up --build --abort-on-container-exit --exit-code-from tests
docker compose -f src/test/docker-compose.ci.yml down -v


Windows (atalho):

.\scripts\run-tests-in-docker.ps1

6) Endpoints
Produtos — /api/produtos

POST /api/produtos

{ "nome": "Caderno", "descricao": "200 folhas", "quantidade": 10, "valor": 12.5 }


201 criado • 400 validação

GET /api/produtos — lista

PUT /api/produtos/{id} — atualiza

DELETE /api/produtos/{id} — remove

Validações: nome obrigatório • quantidade ≥ 1 • valor ≥ 0

Usuários — /api/usuarios

GET /api/usuarios • GET /api/usuarios/{id}

POST /api/usuarios

{ "nome": "Ana Maria", "email": "ana@example.com", "cpf": "12345678901", "cep": "01001-000" }


Preenche logradouro, cidade, estado via CEP válido

PUT /api/usuarios/{id} • DELETE /api/usuarios/{id}

Restrições: nome 2–100 • email válido/único • cpf 11 dígitos/único • cep ≤ 9 • estado 2 • logradouro ≤ 200 • cidade ≤ 100

7) Build / Empacotamento
./mvnw clean verify                 # build + testes
./mvnw clean package -DskipTests    # build rápido
java -jar target/cadastro-0.0.1-SNAPSHOT.jar

8) Estrutura
src/
├─ main/
│  ├─ java/com/sistema/cadastro/    # controllers, services, entidades, mappers
│  └─ resources/                    # configs (H2/JPA)
└─ test/
   ├─ java/com/sistema/cadastro/    # testes
   ├─ docker-compose.yml            # Postgres local dev/test
   └─ docker-compose.ci.yml         # pipeline testes em Docker

9) Tecnologias

Spring Boot • Spring Web • WebClient • Spring Data JPA • Bean Validation • MapStruct • Lombok • H2/PostgreSQL • JUnit 5 • Testcontainers.

10) Dicas

Unix: chmod +x mvnw

Dependências: ./mvnw dependency:tree

Logs: --debug (Maven) ou logging.level.root=DEBUG

11) Contribuição

git checkout -b feat/minha-feature

git commit -m "feat: descrição"

git push origin feat/minha-feature

Abrir PR

12) Licença

Definir (ex.: MIT) e incluir LICENSE na raiz.
