# 🔐 Java Security Demo — GitHub Security Alerts Lab

> **Projeto educacional** com vulnerabilidades **intencionais** para demonstrar os três pilares do GitHub Security.  
> ⚠️ **NÃO USE EM PRODUÇÃO** ⚠️

---

## 🎯 Objetivo

Este projeto foi criado para **acionar alertas reais** no painel do **GitHub Security**, cobrindo:

| Tipo de Alerta | Ferramenta GitHub | O que detecta |
|---|---|---|
| 🔴 **Dependabot Alerts** | Dependabot | Dependências com CVEs conhecidos |
| 🟠 **Code Scanning** | CodeQL | Vulnerabilidades no código-fonte |
| 🟡 **Secret Scanning** | Secret Scanning | Credenciais e tokens no código |

---

## 🚨 Vulnerabilidades Incluídas

### 1. Dependabot Alerts — `pom.xml`

| Dependência | Versão | CVE | Severidade |
|---|---|---|---|
| `log4j-core` | 2.14.1 | CVE-2021-44228 (Log4Shell) | 🔴 Critical (10.0) |
| `spring-webmvc` | 5.3.0 | CVE-2022-22965 (Spring4Shell) | 🔴 Critical (9.8) |
| `jackson-databind` | 2.9.8 | CVE-2019-14379 | 🟠 High (9.8) |
| `commons-collections` | 3.2.1 | CVE-2015-6420 | 🟠 High (7.5) |

### 2. Code Scanning Alerts — `App.java` / `InsecureDeserializer.java`

| Vulnerabilidade | CWE | Query CodeQL | Arquivo |
|---|---|---|---|
| SQL Injection | CWE-89 | `java/sql-injection` | `App.java` |
| Command Injection | CWE-78 | `java/command-line-injection` | `App.java` |
| Path Traversal | CWE-22 | `java/path-injection` | `App.java` |
| Log4Shell (taint) | CWE-917 | `java/log4j-injection` | `App.java` |
| Unsafe Deserialization | CWE-502 | `java/unsafe-deserialization` | `InsecureDeserializer.java` |
| Weak Cryptography (MD5) | CWE-327 | `java/use-of-broken-crypto` | `InsecureDeserializer.java` |
| Weak Random (java.util.Random) | CWE-338 | `java/weak-cryptographic-algorithm` | `InsecureDeserializer.java` |
| XSS | CWE-79 | `java/xss` | `InsecureDeserializer.java` |

### 3. Secret Scanning Alerts — `App.java`

| Segredo | Padrão Detectado |
|---|---|
| `AKIAIOSFODNN7EXAMPLE` | Amazon AWS Access Key ID |
| `wJalrXUtnFEMI/...` | Amazon AWS Secret Access Key |
| `ghp_16C7e42F292c6...` | GitHub Personal Access Token |
| `sk-proj-abcdef...` | Generic API Key |

---

## 🚀 Como usar

### Pré-requisitos
- Conta no GitHub
- Java 11+
- Maven 3.8+

### Passo a passo

```bash
# 1. Crie um repositório PÚBLICO no GitHub (necessário para features gratuitas)
#    Settings → Security → Enable todas as opções

# 2. Clone e configure
git clone https://github.com/SEU_USUARIO/java-security-demo.git
cd java-security-demo

# 3. Faça push do projeto
git add .
git commit -m "feat: add security demo project"
git push origin main
```

### Habilitando os alertas no GitHub

1. Vá em **Settings** → **Security & analysis**
2. Ative:
   - ✅ Dependency graph
   - ✅ Dependabot alerts
   - ✅ Dependabot security updates
   - ✅ Secret scanning
   - ✅ Code scanning (via Actions)

3. O workflow `.github/workflows/codeql.yml` rodará automaticamente no push

### Onde ver os alertas

```
Repositório → Security
├── Overview          ← Painel geral
├── Dependabot        ← Alertas de dependências
├── Code scanning     ← Alertas do CodeQL
└── Secret scanning   ← Credenciais detectadas
```

---

## 🔧 Estrutura do Projeto

```
java-security-demo/
├── .github/
│   ├── workflows/
│   │   └── codeql.yml          # Workflow de análise CodeQL
│   └── dependabot.yml          # Configuração do Dependabot
├── src/main/java/com/securitydemo/
│   ├── App.java                # SQL/Command/Path Injection + Log4Shell + Secrets
│   └── InsecureDeserializer.java # Deserialization + MD5 + Weak Random + XSS
├── pom.xml                     # Dependências vulneráveis (Log4Shell, Spring4Shell...)
└── README.md
```

---

## ✅ Correções (para fins de estudo)

| Vulnerabilidade | Correção |
|---|---|
| SQL Injection | Use `PreparedStatement` com parâmetros (`?`) |
| Command Injection | Use `ProcessBuilder` com lista de strings separadas |
| Path Traversal | Canonicalize o path e valide que está dentro do diretório permitido |
| Log4Shell | Atualize para `log4j 2.17.1+` |
| Unsafe Deserialization | Use `ObjectInputFilter` para whitelist de classes |
| MD5 | Use `BCrypt`, `Argon2`, ou `SHA-256` com salt |
| Weak Random | Use `java.security.SecureRandom` |
| XSS | Use `OWASP Java Encoder` ou `HtmlUtils.htmlEscape()` |
| Secrets hardcoded | Use variáveis de ambiente ou serviços como AWS Secrets Manager |

---

## 📚 Referências

- [GitHub Security Features](https://docs.github.com/en/code-security)
- [CodeQL Java Queries](https://codeql.github.com/codeql-query-help/java/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [CVE-2021-44228 (Log4Shell)](https://nvd.nist.gov/vuln/detail/CVE-2021-44228)

---

> **Disclaimer:** Este projeto é estritamente educacional. As vulnerabilidades são simuladas e os segredos são fictícios/inválidos.
