Feature: Cadastro de produtos
  Como usuário da API
  Quero cadastrar um produto
  Para que ele apareça na lista de produtos

  Scenario: Criar produto com sucesso
    Given que a API está no ar
    When eu cadastro um produto válido
    Then a resposta HTTP deve ser 201
    And o produto retornado deve ter id gerado
    When eu consulto a lista de produtos
    Then deve conter um produto com nome "Caderno"