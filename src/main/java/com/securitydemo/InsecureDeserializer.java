package com.securitydemo;

import java.io.*;
import java.util.Base64;

/**
 * ⚠️ VULNERABILIDADE: Deserialization Insegura
 * CWE-502: Deserialization of Untrusted Data
 * CodeQL query: java/unsafe-deserialization
 *
 * Combinada com commons-collections 3.2.1 (CVE-2015-6420),
 * permite Remote Code Execution.
 */
public class InsecureDeserializer {

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - Unsafe Deserialization
    // CWE-502 + commons-collections 3.2.1 → RCE
    // =========================================================
    public Object deserializeFromBase64(String base64Data) {
        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);

            // VULNERÁVEL: ObjectInputStream sem validação de classes
            ObjectInputStream ois = new ObjectInputStream(bis); // ← CodeQL detecta aqui
            Object obj = ois.readObject();
            ois.close();

            return obj;

        } catch (Exception e) {
            System.out.println("Erro na deserialização: " + e.getMessage());
            return null;
        }
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - Weak Cryptography
    // CWE-327: Use of a Broken or Risky Cryptographic Algorithm
    // CodeQL query: java/use-of-broken-or-risky-cryptographic-algorithm
    // =========================================================
    public String hashPasswordInsecure(String password) {
        try {
            // VULNERÁVEL: MD5 é considerado quebrado para uso criptográfico
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5"); // ← CodeQL detecta
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            return null;
        }
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - Weak Random
    // CWE-338: Use of Cryptographically Weak PRNG
    // =========================================================
    public String generateToken() {
        // VULNERÁVEL: java.util.Random não é criptograficamente seguro
        java.util.Random random = new java.util.Random(); // ← CodeQL detecta
        return String.valueOf(random.nextLong());
        // ✓ Correção: usar java.security.SecureRandom
    }

    // =========================================================
    // ⚠️ ALERTA: CODE SCANNING - XSS (Cross-Site Scripting)
    // CWE-79: Improper Neutralization of Input in Web Page
    // =========================================================
    public String renderHtml(String userContent) {
        // VULNERÁVEL: input do usuário inserido diretamente no HTML sem escape
        return "<html><body><h1>" + userContent + "</h1></body></html>"; // ← CodeQL detecta
        // Payload: <script>alert('XSS')</script>
        // ✓ Correção: usar OWASP Java Encoder ou similar
    }
}
