## 🧠 Vaga Fácil

Plataforma de gerenciamento de vagas e candidatos, com autenticação via JWT, separando perfis de empresa e candidato. Desenvolvido com **Spring Boot**, **Spring Security**, **JWT** e **MongoDB**.

---

### 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- MongoDB
- JWT (Auth0)
- Swagger / OpenAPI
- JUnit + Mockito (testes unitários e de controller)

---

### 📚 Funcionalidades

#### ✅ Autenticação & Autorização

- Autenticação via JWT para candidatos e empresas
- Proteção de rotas com base em perfis (role: `CANDIDATE` ou `COMPANY`)
- Login e cadastro separados para cada tipo de usuário

#### 🧝 Candidato

- Cadastro de candidatos
- Autenticação de candidato (`/candidate/auth`)
- Atualização de dados pessoais
- Validação de duplicidade de usuário

#### 🏢 Empresa

- Cadastro de empresas
- Autenticação de empresa (`/company/auth`)
- Validação de duplicidade de e-mail ou username

---

### 📂 Estrutura do Projeto

```
src/main/java/com/example/demo/
├── config/
│   └── SwaggerConfig.java
├── modules/
│   ├── candidate/
│   │   ├── controllers/
│   │   ├── dtos/
│   │   ├── entity/
│   │   ├── exceptions/
│   │   ├── repository/
│   │   └── useCases/
│   ├── company/
│   │   ├── controllers/
│   │   ├── dto/
│   │   ├── entity/
│   │   ├── exceptions/
│   │   ├── repository/
│   │   └── useCases/
├── providers/
├── security/
└── GestaoVagasApplication.java
```

---

### 🥪 Testes

O projeto contém testes unitários e de controller para garantir o bom funcionamento das funcionalidades principais.

Exemplos:

- ✅ Criação de empresas
- ✅ Autenticação de candidatos e empresas
- ✅ Validação de erros (usuário não encontrado, senha incorreta, etc.)

Execute os testes com:

```bash
./mvnw test
```

---

### 🛠️ Como rodar o projeto

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/javagas.git
cd javagas
```

2. Configure o `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/javagas
security.token.secret=seuSegredoJWT
```

3. Execute o projeto:

```bash
./mvnw spring-boot:run
```

---

### 📄 Documentação da API

Após iniciar o projeto, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

---

### 🧑‍💻 Autor

Desenvolvido por **Lucas Bomfim do Nascimento**

- 🌐 [LinkedIn](https://www.linkedin.com/in/lucas-bomfim15/)
- 📧 [lucas.email@exemplo.com](mailto\:bmfmlucas@gmail.com)

