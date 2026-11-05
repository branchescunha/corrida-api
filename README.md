# Corrida API

API REST desenvolvida em Java com Spring Boot para gerenciamento das inscrições da 5ª Corrida Solidária Vila Esperança.

O projeto implementa cadastro, consulta, edição e cancelamento de inscrições, aplicação das regras de negócio da corrida, persistência em banco de dados e tratamento padronizado de erros.

## Tecnologias

- Java 21
- Spring Boot 4.1.2
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Supabase
- H2 Database
- Maven
- JUnit 5

## Funcionalidades

- cadastrar inscrição
- listar inscrições
- consultar inscrição por e-mail
- atualizar inscrição
- cancelar inscrição
- consultar resumo das inscrições
- validação dos dados de entrada
- cálculo da idade do participante
- cálculo da categoria
- validação da idade mínima
- restrição do percurso de 10 km
- normalização de nome e e-mail
- tratamento global de erros

## Regras de negócio

A data oficial da prova é **14/03/2027**.

### Idade mínima

O participante deve possuir pelo menos 14 anos na data da prova.

### Percursos

- 5 km: permitido a partir de 14 anos
- 10 km: permitido somente a partir de 18 anos

### Categorias

- Juvenil: 14 a 17 anos
- Adulto: 18 a 39 anos
- Master: 40 a 59 anos
- Senior: 60 anos ou mais

### E-mail

O e-mail funciona como identificador da inscrição e não pode ser alterado durante a edição.

Também é permitida somente uma inscrição ativa por e-mail.

## Endpoints

Base URL local:

```text
http://localhost:8080/api/inscricoes
```

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/inscricoes` | Cadastra uma inscrição |
| GET | `/api/inscricoes` | Lista todas as inscrições |
| GET | `/api/inscricoes/{email}` | Consulta uma inscrição |
| PUT | `/api/inscricoes/{email}` | Atualiza uma inscrição |
| DELETE | `/api/inscricoes/{email}` | Cancela uma inscrição |
| GET | `/api/inscricoes/resumo` | Retorna o resumo das inscrições |

## Exemplo de cadastro

```json
{
  "nome": "André Vinícius",
  "email": "andre@example.com",
  "dataNascimento": "2000-05-10",
  "percurso": "KM_10"
}
```

Resposta:

```json
{
  "nome": "André Vinícius",
  "email": "andre@example.com",
  "dataNascimento": "2000-05-10",
  "percurso": "KM_10",
  "idadeNaProva": 26,
  "categoria": "ADULTO"
}
```

## Tratamento de erros

A API utiliza tratamento global de exceções.

Principais códigos HTTP:

- `200 OK` — operação realizada com sucesso
- `201 Created` — inscrição criada
- `204 No Content` — inscrição cancelada
- `400 Bad Request` — dados ou regra de negócio inválidos
- `404 Not Found` — inscrição não encontrada
- `409 Conflict` — e-mail já cadastrado

Exemplo:

```json
{
  "mensagem": "Já existe uma inscrição ativa para este e-mail."
}
```

## Banco de dados

### Desenvolvimento local

Por padrão, o projeto utiliza H2 persistente.

O console pode ser acessado em:

```text
http://localhost:8080/h2-console
```

### Supabase

O projeto também possui um profile para conexão com PostgreSQL hospedado no Supabase.

As credenciais são fornecidas por variáveis de ambiente:

```text
SPRING_PROFILES_ACTIVE=supabase
DB_URL
DB_USERNAME
DB_PASSWORD
```

Nenhuma credencial do banco é armazenada no repositório.

## Como executar

### Requisitos

- Java 21

### Executar os testes

Windows:

```powershell
.\mvnw.cmd test
```

Linux/macOS:

```bash
./mvnw test
```

### Executar a API

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## Testes automatizados

A suíte automatizada cobre:

- inicialização do contexto Spring
- cálculo da idade antes, no dia e depois do aniversário na data da prova
- categorias nas bordas 17/18, 39/40 e 59/60
- idade mínima de 14 anos
- precedência da idade mínima sobre a regra dos 10 km
- restrição dos 10 km para menores de 18 anos
- permissão dos 10 km a partir dos 18 anos
- permissão do percurso de 5 km
- normalização de e-mail
- normalização de nome
- validação do nome após normalização
- validação do e-mail após normalização

Resultado atual:

```text
Tests run: 18
Failures: 0
Errors: 0
Skipped: 0
```

## Estrutura

```text
src/
├── main/
│   ├── java/com/github/branchescunha/corridaapi/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── enums/
│   │   ├── exception/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── application.properties
│       └── application-supabase.properties
└── test/
    └── java/com/github/branchescunha/corridaapi/
        └── service/
```

## Autor

André Vinícius Branches Cunha