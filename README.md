# Store API

## Visão Geral

Uma API responsável por controlar os estoques e produtos
de um restaurante.

## Tecnologias Usadas

- Spring Boot
- Lombok
- Mysql
- MapStruct
- OAuth2

## Passo a Passo

É necessário mudar as informações em aspas do application.yml, conforme configurado no seu Banco de Dados
```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/"database"?useSSL=false&useTimezone=true&serverTimezone=UTC
    username: "usuario"
    password: "senha"
```

### Terminal

Para instanciar as dependências necessárias
```
mvn clean install
```

Para executar
```
mvn spring-boot:run
```

## Requisições Disponíveis

- GET
- POST
- PUT
- DELETE

### Gerar TOKEN

```
http://localhost:8080/oauth/token
```
<details>
<summary>Form</summary>
username e password do usuário, podem ser mudados na classe de Configuração

```
grant_type = password
username = manager
password = 1234
```
</details>

<details>
<summary>Auth</summary>

USERNAME E PASSWORD do Auth podem ser mudados no application.yml
```
USERNAME = shop
PASSWORD = 1234
```
</details>

#### Onde Usar 
É necessário usar o access_token como um Bearer Token, para realizar as seguintes
requisições POST, DELETE e PUT.



### Produtos

Criar — POST
```
http://localhost:8080/api/v1/shop/product/create
```

Lista Todos os Relatórios — GET
```
http://localhost:8080/api/v1/shop/product
```

Relatório do Produto pelo ID = (Busca — GET | Atualiza — PUT e Deleta — Delete)
```
http://localhost:8080/api/v1/shop/product/inspect/{id}
```

Mostra se o Produto pelo ID pode ser vendido — GET
```
http://localhost:8080/api/v1/shop/product/check/{id}
```

<details>
<summary>Exemplo de Json - Produtos(PUT,POST)</summary>

```
{
    "name": "Arroz",
    "price": 40,
    "components": [
        {
            "ingredientName": "Arroz",
            "usedQuantity": 10
        },
        {
            "ingredientName": "Feijão",
            "usedQuantity": 40
        }
    ]
}
```

</details>

### Estoques

Criar — POST
```
http://localhost:8080/api/v1/shop/stock/create
```

Lista todos os ingredientes no Estoque
```
http://localhost:8080/api/v1/shop/stock
```


Ingredientes em Estoque pelo ID = (Busca — GET | Atualiza — PUT e Deleta — Delete)
```
http://localhost:8080/api/v1/shop/stock/inspect/{id}
```

<details>
<summary>Exemplo de Json - Estoques(PUT,POST)</summary>

```
{
    "ingredients": {
        "name": "Arroz2",
        "unit": "Kg",
        "price": 10.52
    },
    "quantity": 50
}
```

</details>