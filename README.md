# Aplicativo Android de Login Autenticador

Este aplicativo Android oferece uma solução completa de autenticação de usuários, permitindo cadastro, login com senha e e-mail, e login social com Google e Facebook. Ele se integra a uma API externa [auth-android-api](https://github.com/vvieira22/auth-android-api) desenvolvida em FastAPI.

## Funcionalidades

- **Cadastro de Usuário**: Permite que novos usuários criem uma conta no aplicativo, fornecendo informações como nome, e-mail e senha.
- **Login com Senha e E-mail**: Os usuários podem fazer login no aplicativo usando suas credenciais de e-mail e senha.
- **Login Social**: O aplicativo oferece suporte para login social com Google e Facebook, permitindo que os usuários façam login usando suas contas existentes nessas plataformas.

## Configuração

### API

1. Certifique-se de que a API "auth-android-api" esteja em execução e acessível.
2. Configure a URL da API no aplicativo Android para que ele possa se comunicar com o backend.

### Google

1. Crie um projeto no Firebase Console:
   - Acesse o [Firebase Console](https://console.firebase.google.com/).
   - Crie um novo projeto ou selecione um existente.

2. Registre seu aplicativo Android:
   - No painel do projeto, clique em "Android".
   - Siga as instruções para registrar seu aplicativo, fornecendo o nome do pacote e o SHA-1 do certificado de depuração e produção.
Caso não saiba como conseguir o SHA-1, por se tratar inicialmente de um ambiente dev, podemos obter dentro do projeto, preferencialmente executando dentro do android studio o comando: `/gradlew signingReport`.

3. Ative o login com o Google:
   - No painel do projeto, vá para "Autenticação" e clique em "Primeiros passos".
   - Na guia "Método de login", ative o login com o Google.
   - Obtenha as credenciais do Google para o seu aplicativo.

4. Integre o SDK do Firebase no seu aplicativo:
   - Adicione as dependências do Firebase ao seu arquivo `build.gradle`.
   - Configure o Firebase no seu aplicativo, fornecendo as credenciais obtidas anteriormente.

### Facebook

1. Crie um aplicativo no Facebook for Developers:
   - Acesse o [Facebook for Developers](https://developers.facebook.com/).
   - Crie um novo aplicativo.

2. Configure o login com o Facebook:
   - No painel do aplicativo, vá para "Configurações" e clique em "Básico".
   - Adicione o SHA-1 do seu certificado de depuração e produção.
   - Na guia "Produtos", adicione o produto "Login do Facebook".
   - Configure as URLs de redirecionamento para o seu aplicativo.

3. Integre o SDK do Facebook no seu aplicativo:
   - Adicione as dependências do Facebook ao seu arquivo `build.gradle`.
   - Configure o Facebook no seu aplicativo, fornecendo as credenciais obtidas anteriormente.

## Observações

- Você pode acompanhar releases e features novas na [branch](https://github.com/vvieira22/app-authenticator/tree/develop) principal do projeto.
