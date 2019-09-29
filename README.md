# ListOfEvents

## Organização da aplicação
O aplicativo foi organizado em 4 módulos além do módulo "app", onde cada módulo é responsável por:
- **basefeature:** Módulo que contém classes comuns para o desenvolvimento das demais features do app
- **coredata:** Para esse aplicativo ele ficou responsável basicamente para a criação do cliente de REST, mas em projetos maiores fica responsável pelo core tanto de persistencia local como de comunicação com o servidor.
- **eventsdata:** Módulo responsável por efetuar a comunicação com a API de eventos e abastecer o módulo events com os dados obtidos
- **events:** Módulo que contém a parte de UI da feature de eventos

Todas as dependêcias externas estão centralizadas no arquivo de configuração **"dependencies.gradle"**. Essa abordagem se deu pelo fato de possuirem varios módulos e em uma futura atualização de alguma dependência somente um arquivo precisará ser alterado.

## Frameworks utilizados
- **Coroutines:** Utilizado para tratamento de operações assíncronas
- **Koin:** Framework de injeção de dependência
- **Mockito:** Ferramenta de testes unitários para mockar dados
- **Picasso:** Framework utilizado para carregar imagens a partir de uma url
- **Retrofit:** Framework utilizado para realizar comunicação com serviços REST
