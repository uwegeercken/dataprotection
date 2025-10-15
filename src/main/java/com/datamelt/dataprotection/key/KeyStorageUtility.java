package com.datamelt.dataprotection.key;

import com.datamelt.dataprotection.utility.FileUtility;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KeyStorageUtility
{
    private static final String ALGORITHM = "AES";
    // Method to save the secret key to a file
    public static void saveKey(String filePath, String fileName, SecretKey key) throws Exception
    {
        byte[] keyData = key.getEncoded();
        Files.write(Paths.get(FileUtility.getFilePathAndName( filePath, fileName)), keyData);
    }

    // Method to load the secret key from a file
    public static SecretKey loadKey(String filePath, String fileName) throws Exception
    {
        byte[] keyData = Files.readAllBytes(Paths.get(FileUtility.getFilePathAndName(filePath, fileName)));
        return new SecretKeySpec(keyData, ALGORITHM);
    }
}
