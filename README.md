# Projeto Teste de Software — Cadastro de Produtos e Usuários

Sistema **Spring Boot 3.5** (Java **21**) para cadastro de **Produtos** e **Usuários**, com persistência via **JPA/Hibernate**, banco **H2 em memória** por padrão e testes com **Testcontainers** (PostgreSQL). O projeto usa **Maven Wrapper**, **MapStruct** e **Bean Validation**.

> Artefato: `cadastro-0.0.1-SNAPSHOT.jar`  
> Porta padrão: **8080**  
> Console do H2: **/h2-console**

---

## 🔧 Requisitos

- **JDK 21** (compatível com Java 21) — verifique com `java -version`  
- **Docker** (**recomendado** para rodar os testes com Testcontainers)  
- **Git** para clonar o repositório  
- **Maven NÃO é obrigatório** (use `mvnw` / `mvnw.cmd`)

---

## 🚀 Setup rápido

```bash
# 1) Clone
git clone https://github.com/Am4r00/Projeto-Teste-Software.git
cd Projeto-Teste-Software

# 2) Build rápido (baixa dependências, empacota)
./mvnw -q -DskipTests package        # macOS/Linux
# ou
mvnw.cmd -q -DskipTests package      # Windows

# 3) Rodar em modo dev (Spring Boot)
./mvnw spring-boot:run

# 4) Acesse (após subir):
#   API:         http://localhost:8080
#   H2 Console:  http://localhost:8080/h2-console


H2 Console

JDBC URL: jdbc:h2:mem:sistemacadastro

Usuário: sa

Senha: (vazio)

As tabelas são criadas/atualizadas automaticamente (spring.jpa.hibernate.ddl-auto=update).

🧱 Build / Empacotamento
Sempre exibir os detalhes
# Build completo (com testes)
./mvnw clean verify

# Build sem rodar testes
./mvnw clean package -DskipTests

# JAR final (executável)
java -jar target/cadastro-0.0.1-SNAPSHOT.jar
# (ou java -jar target/*.jar)

▶️ Execução (prod/dev)

Padrão (H2 em memória):

Sempre exibir os detalhes
./mvnw spring-boot:run
# ou
java -jar target/cadastro-0.0.1-SNAPSHOT.jar


Com PostgreSQL local (docker-compose de exemplo incluso):

Sempre exibir os detalhes
# sobe um Postgres local de teste (porta 5432)
docker compose -f src/test/docker-compose.yml up -d

# execute a aplicação apontando para o Postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb \
SPRING_DATASOURCE_USERNAME=test \
SPRING_DATASOURCE_PASSWORD=test \
./mvnw spring-boot:run


No Windows (PowerShell), ajuste a exportação de variáveis conforme seu shell.

Base de CEP externa (ViaCEP): por padrão, usa https://viacep.com.br/ws via WebClient. Para trocar a base (ou mockar em ambientes de teste), configure:

Sempre exibir os detalhes
EXTERNAL_CEP_API_URL=https://viacep.com.br/ws  # padrão

🧪 Testes

Os testes de integração usam Testcontainers (PostgreSQL). Com Docker rodando, basta:

Sempre exibir os detalhes
./mvnw test                    # roda a suíte
./mvnw -Dtest=ClasseTest test  # roda somente uma classe
./mvnw -Dtest=Classe#metodo test  # roda somente um método

Rodar testes em Docker (ambiente CI local)

O repositório traz um compose prontinho para CI (usa imagem maven:3.9.9-eclipse-temurin-21):

Sempre exibir os detalhes
docker compose -f src/test/docker-compose.ci.yml up --build --abort-on-container-exit --exit-code-from tests
# derruba tudo ao final
docker compose -f src/test/docker-compose.ci.yml down -v


No Windows, você pode usar o script:

Sempre exibir os detalhes
.\scripts\run-tests-in-docker.ps1

📚 Tecnologias e libs

Spring Boot 3.5.7: Web (MVC), WebFlux (WebClient), Data JPA, Validation, DevTools

Banco: H2 (memória, dev) • Suporte a PostgreSQL (teste/produção)

MapStruct 1.6 (mapeamento DTO ↔ entidade)

Lombok (getters/setters/constructors)

Testcontainers + JUnit 5 (integração)

WireMock (disponível para mocks HTTP em testes, se necessário)

🗺️ Endpoints principais
Produtos (/api/produtos)

POST /api/produtos — cria produto
Body (JSON):

Sempre exibir os detalhes
{
  "nome": "Caderno",
  "descricao": "200 folhas",
  "quantidade": 10,
  "valor": 12.5
}


Respostas: 201 Created (objeto criado) • 400 Bad Request (validação)

GET /api/produtos — lista todos
Resposta: 200 OK com []

PUT /api/produtos/{id} — atualiza produto
Body (JSON): mesmo formato do POST
Respostas: 200 OK • 404 Not Found

DELETE /api/produtos/{id} — remove produto
Respostas: 204 No Content • 404 Not Found

Regras de validação (DTO):

nome: obrigatório

quantidade: >= 1

valor: >= 0

Usuários (/api/usuarios)

GET /api/usuarios — lista todos (200 OK)

GET /api/usuarios/{id} — busca por id (200 OK • 404 Not Found)

POST /api/usuarios — cria usuário
Body (JSON):

Sempre exibir os detalhes
{
  "nome": "Ana Maria",
  "email": "ana@example.com",
  "cpf": "12345678901",
  "cep": "01001-000"
}


Campos opcionais preenchidos automaticamente quando cep é informado e válido: logradouro, cidade, estado (via ViaCEP).
Respostas: 201 Created • 400 Bad Request (validação ou CEP inválido)

PUT /api/usuarios/{id} — atualiza usuário (200 OK • 404 Not Found)

DELETE /api/usuarios/{id} — remove (204 No Content • 404 Not Found)

Restrições e regras (entidade):

nome: obrigatório, 2–100 chars

email: obrigatório, válido, único

cpf: obrigatório, 11 dígitos, único

cep: máx 9 chars (aceita com/sem máscara)

estado: máx 2 chars

logradouro (máx 200), cidade (máx 100)

🧩 Estrutura do projeto
Sempre exibir os detalhes
src/
├─ main/
│  ├─ java/com/sistema/cadastro/
│  │  ├─ CadastroApplication.java        # classe main (Spring Boot)
│  │  ├─ controller/                     # REST controllers (Produto, Usuario)
│  │  ├─ dto/                            # DTOs de Produto
│  │  ├─ external/                       # CepClient (WebClient) + CepResponse
│  │  ├─ mapper/                         # MapStruct (ProdutoMapper)
│  │  ├─ produto/                        # entidades: Produto, Usuario
│  │  └─ repository/ + service/          # camadas de dados e regras
│  └─ resources/application.properties   # H2 e JPA
└─ test/
   ├─ java/com/sistema/cadastro/
   │  ├─ CadastroApplicationTests.java
   │  ├─ TestCadastroApplication.java
   │  └─ TestcontainersConfiguration.java
   ├─ docker-compose.yml                 # Postgres local p/ dev/test
   └─ docker-compose.ci.yml              # pipeline de testes em Docker

⚙️ Configurações úteis
Banco (dev)

src/main/resources/application.properties

Sempre exibir os detalhes
spring.datasource.url=jdbc:h2:mem:sistemacadastro;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

Banco (PostgreSQL)

Variáveis de ambiente típicas:

Sempre exibir os detalhes
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testdb
SPRING_DATASOURCE_USERNAME=test
SPRING_DATASOURCE_PASSWORD=test
# opcional:
SPRING_JPA_HIBERNATE_DDL_AUTO=update

Serviço de CEP
Sempre exibir os detalhes
EXTERNAL_CEP_API_URL=https://viacep.com.br/ws   # default

💡 Dicas

Se mvnw der permissão negada (Linux/macOS): chmod +x mvnw

Para ver dependências: ./mvnw dependency:tree

Logs mais verbosos: --debug no Maven ou logging.level.root=DEBUG no application.properties

🤝 Contribuição

Crie um branch: git checkout -b feat/minha-feature

Commits padronizados: feat: ..., fix: ..., test: ...

Push: git push origin feat/minha-feature

Abra um Pull Request

📄 Licença

Defina a licença do projeto (ex.: MIT). Se necessário, adicione o arquivo LICENSE na raiz.
"""

Save file

with open('/mnt/data/README.md', 'w', encoding='utf-8') as f:
f.write(readme)

print("README.md gerado em /mnt/data/README.md")
