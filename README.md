# Automação de Testes de API — ServeRest

Projeto de automação de testes da API REST [ServeRest](https://serverest.dev/), desenvolvido em Java, com foco na validação de endpoints, regras de negócio e cenários positivos e negativos.

## Tecnologias utilizadas

- Java 21
- JUnit 5
- REST Assured
- Maven
- DataFaker
- JSON Schema Validation

## Cobertura de testes

O projeto contempla 29 execuções de testes automatizados, distribuídas entre:

- **Usuários:** cadastro, consulta, atualização, exclusão e validações de dados.
- **Login:** autenticação e validação de credenciais inválidas.
- **Produtos:** cadastro, consulta, atualização, exclusão e controle de autorização.
- **Carrinhos:** criação, consulta, validação de estoque, cancelamento e conclusão de compras.

## Estrutura do projeto

O código está organizado em pacotes de testes, serviços, modelos, geração de dados e configurações, utilizando conceitos como Service Layer, Builder Pattern, Data Factory e testes parametrizados.

## Como executar os testes

### Pré-requisitos

- JDK 21 instalado
- Apache Maven instalado
- Git instalado
- Conexão com a internet

### 1. Clonar o repositório

No terminal, execute:

```bash
git clone https://github.com/gabrielhoffmannhub/Automacao_ServeRest_Parte3.git
```

Em seguida, acesse a pasta do projeto:

```bash
cd Automacao_ServeRest_Parte3
```

### 2. Executar os testes

Na raiz do projeto, execute:

```bash
mvn clean test
```

O Maven fará o download das dependências necessárias e executará os testes automatizados.

### 3. Consultar os resultados

Os resultados serão exibidos no terminal após a execução.

Os relatórios individuais também estarão disponíveis em:

```text
target/surefire-reports/
```

### Execução pelo IntelliJ IDEA

Como alternativa, o projeto pode ser aberto no IntelliJ IDEA como um projeto Maven.

Após o carregamento das dependências, basta executar as classes de teste pelo próprio IntelliJ.

## Ambiente de testes

Por padrão, a automação utiliza a API pública da ServeRest:

https://serverest.dev

Também é possível executar os testes utilizando uma instância local da API, configurando a URL base:

```bash
mvn test -DbaseUrl=http://localhost:3000
```
