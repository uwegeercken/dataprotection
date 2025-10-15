package com.datamelt.dataprotection.encrypt;

import com.datamelt.dataprotection.utility.Try;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Function;

public class EncryptFunction
{
    private static final String ALGORITHM = "AES";

    private final SecretKey secretKey;

    public EncryptFunction(SecretKey secretKey)
    {
        this.secretKey = secretKey;
    }

    public EncryptFunction() throws NoSuchAlgorithmException
    {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(256);
        secretKey = keyGenerator.generateKey();
    }

    public SecretKey getSecretKey()
    {
        return secretKey;
    }

    public Function<String, Try<String>> encryptString = value -> Try.of(() -> encrypt(value));
    public Function<Integer, Try<String>> encryptInteger = value -> Try.of(() -> encrypt(String.valueOf(value)));
    public Function<Boolean, Try<String>> encryptBoolean = value -> Try.of(() -> encrypt(String.valueOf(value)));

    private String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedValue = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public Function<String, Try<String>> decryptString = value -> Try.of(() -> decrypt(value));
    public Function<String, Try<Integer>> decryptInteger = value -> Try.of(() -> Integer.parseInt(decrypt(value)));
    public Function<String, Try<Boolean>> decryptBoolean = value -> Try.of(() -> Boolean.parseBoolean(decrypt(value)));


    private String decrypt(String value) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(value));
        return new String(decryptedValue, StandardCharsets.UTF_8);
    }
}
