package ru.dlabs.sas.example.jsso.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.codec.Hex;
import ru.dlabs.sas.example.jsso.exception.CryptoException;

@UtilityClass
public class CryptoUtils {

    /**
     * Сформировать криптографический ключ на основе пароля.
     */
    public byte[] pbkdf(String password, byte[] salt, int derivedKeyLength, int iterations) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);
        SecretKeyFactory keyFactory;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }

        try {
            return keyFactory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }
    }

    /**
     * Получить hash указанной строки.
     */
    public String hash(String input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA3-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }
        byte[] result = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(result));
    }
}
