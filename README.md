# Aplicativo Android de Login Autenticador
<details>
   <summary><img src="https://github.com/vvieira22/primeiro-codigo/assets/49120447/73ede6c7-ead7-49c4-80c4-88cff49efc21" alt="image"> English Version</summary>

   This Android application offers a complete user authentication solution, allowing registration, login with email and password, and social login with Google and Facebook. 
   It integrates with an external API [auth-android-api](https://github.com/vvieira22/auth-android-api) developed in FastAPI.

## Features

- **User Registration**: Allows new users to create an account in the app by providing information such as name, email, and password.
- **Email and Password Login**: Users can log in to the app using their email and password credentials.
- **Social Login**: The app supports social login with Google and Facebook, allowing users to log in using their existing accounts on those platforms.

## Configuration

### API

1. Ensure that the "auth-android-api" is running and accessible.
2. Configure the API URL in the Android application so it can communicate with the backend.

### Google

1. Create a project in the Firebase Console:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project or select an existing one.

3. Enable Google login:
   - In the project panel, go to "Authentication" and click "Get Started".
   - In the "Sign-in method" tab, enable Google login.
   - Obtain the Google credentials for your application.

4. Integrate the Firebase SDK into your application:
   - Add Firebase dependencies to your `build.gradle` file.
   - Configure Firebase in your application by providing the credentials obtained earlier.

### Facebook

1. Create an app on Facebook for Developers:
   - Go to [Facebook for Developers](https://developers.facebook.com/).
   - Create a new app.

2. Configure Facebook login:
   - In the app panel, go to "Settings" and click "Basic".
   - Add the SHA-1 of your debug and production certificate.
   - In the "Products" tab, add the "Facebook Login" product.
   - Configure the redirect URLs for your application.

3. Integrate the Facebook SDK into your application:
   - Add Facebook dependencies to your `build.gradle` file.
   - Configure Facebook in your application by providing the credentials obtained
  
   ## Observations
- You can follow releases and features at main [branch](https://github.com/vvieira22/app-authenticator/tree/develop) of project.
</details>

Este aplicativo Android oferece uma solução completa de autenticação de usuários, permitindo cadastro, login com senha e e-mail, e login social com Google e Facebook. 
Ele se integra a uma API externa [auth-android-api](https://github.com/vvieira22/auth-android-api) desenvolvida em FastAPI.

&nbsp;
&nbsp;
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
