# [UNRELEASED VERSION]

## Add:

1. **Primeiro commit**: Adicionando a primeira versão da aplicação.
2. **Refatoração do projeto**: Adicionando alguns pacotes e mudando paths. Além disso, adicionado o sistema de cadastro usando o Firebase. Por enquanto, apenas email + senha disponíveis.
3. **Melhorias nos tratamentos de login e email**: Validação de CPF/CNPJ pendentes.
4. **Adição de strings para agrupar mensagens de erro**: Uso de log para identificar melhor os erros.
5. **Atualização da função de login**: Correção do erro do usuário ao tentar logar.
6. **Configuração inicial para login Facebook**: Captação do email do usuário caso login ao Facebook seja feito com sucesso.
    - Necessário analisar e validar três situações com Firebase + login Facebook:
        1. Clicar e fazer login Facebook e esse email não estiver cadastrado.
        2. Após capturar email do Facebook, autenticar o usuário imediatamente, e identificar o usuário pelo Firebase.
        3. Credenciais do Facebook: Esconder e conseguir subir este projeto ao GitHub público sem afetar API etc.
7. **Modificação no layout de login**: Design mais moderno.
8. **Criação da subtela para validar usuário anteriormente conectado**.
9. **Adição de telefone na tela de cadastro**: Validação de máscara pendente. Envio dos dados de cadastro do usuário ao modelo de banco não relacional Cloud Firestore. Melhorias de layout entre outros.
10. **Recuperação de dados do usuário**: Forma manual e geral.
11. **Adição de máscaras e validações para telefone, CPF e CNPJ**: Melhorias nas mensagens das snackbars.
12. **Modificação na tela de entrada do app**: Agora mostra uma cor intermediária do login, já que o splashscreen não tem suporte a gradient. Atenção aos temas (padrão e darkmode).
13. **Mudança na arquitetura do projeto**: Implementação de todos os módulos dentro do projeto principal (Retrofit, Firebase). Posterior separação poderá ser considerada.
14. **Alteração de LiveData para StateFlow para login e registro**: Validação de campos pendente (TODO).
15. **Criação de documento para transcrever códigos de erro HTTP para mensagens amigáveis ao usuário**.
16. **Criação do fragmento do termo de aceite**.
17. **Layout e fragmento da tela de login criados**: Recepção de dados de login, simulação de uma tela de welcome.
18. **Correção do problema de loading para login via Google**.
19. **Reestruturação do projeto para 1 nível anterior, antes estavamos dentro de uma pasta, isso possibilitou
      a migração do release_notes para o readme, que agora está sendo feito, para facilitar geração de versões entre outros.**
20. 
## TODO + BUGS (POR ORDEM DE PRIORIDADE!):

(Níveis I, II, III e IV, sendo a prioridade máxima o nível I. Conforme aumenta o número, a prioridade diminui.)

### I

---
### II
- Foco na questão de erros da API: Tratar e traduzir erros para a linguagem definida no projeto. Atualmente está hardcoded, o que é muito ruim.
- Melhorias nos erros e fluxo para login/cadastro social: Determinar se deve fazer login ou cadastrar o usuário.
---
### III
- Validação do botão de continuar com Google: Atualmente só faz registro e dá erro se já estiver cadastrado. Validar na API se já existe e logar automaticamente se o token e outras informações estiverem salvas.
- Validação do botão de Google na ponta: Verificar se o usuário já tem conta para fazer login ou ir para a tela de registro (pendente). A tela de registro será diferente da tradicional, sem senha, podendo ser opcional e permitir atualização posterior. Termo de aceite necessário.
---
### IV
- Termo de aceite obrigatório no cadastro: Considerar como apresentar o termo de uso para login com rede social.
---
