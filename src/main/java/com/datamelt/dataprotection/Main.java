package com.datamelt.dataprotection;

import com.datamelt.dataprotection.encrypt.EncryptFunction;
import com.datamelt.dataprotection.key.KeyStorageUtility;
import com.datamelt.dataprotection.rest.PostHandler;
import com.datamelt.dataprotection.utility.ArgumentParser;
import com.datamelt.dataprotection.utility.ProgramArgument;
import com.datamelt.dataprotection.utility.Try;
import com.sun.net.httpserver.HttpServer;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        ArgumentParser parser = new ArgumentParser(args);
        Stream.of(ProgramArgument.values())
                .forEach(arg -> System.out.println("argument: " + arg.getKey() + " -> " + parser.getArgument(arg)));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/process", new PostHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");

        try
        {
                String originalString = "Hello, World!";
                int originalInt = 42;
                boolean originalBoolean = true;

                EncryptFunction encryptFunction;
                Try<SecretKey> secretKey = Try.of(() -> KeyStorageUtility.loadKey(parser.getArgument(ProgramArgument.KEYFILE_PATH), parser.getArgument(ProgramArgument.KEYFILE_NAME)));

                if(secretKey.isSuccess())
                {
                    encryptFunction = new EncryptFunction(secretKey.get());
                }
                else
                {
                    encryptFunction = new EncryptFunction();
                    KeyStorageUtility.saveKey(parser.getArgument(ProgramArgument.KEYFILE_PATH), parser.getArgument(ProgramArgument.KEYFILE_NAME), encryptFunction.getSecretKey());
                }

                // Encrypting values
                Try<String> encryptedString = encryptFunction.encryptString.apply(originalString);
                Try<String> encryptedInt = encryptFunction.encryptInteger.apply(originalInt);
                Try<String> encryptedBoolean = encryptFunction.encryptBoolean.apply(originalBoolean);

                // Output encrypted values
                System.out.println("Encrypted String: " + encryptedString.get());
                System.out.println("Encrypted Integer: " + encryptedInt.get());
                System.out.println("Encrypted Boolean: " + encryptedBoolean.get());

                // Decrypting values
                Try<String> decryptedString = encryptFunction.decryptString.apply(encryptedString.get());
                Try<Integer> decryptedInt = encryptFunction.decryptInteger.apply(encryptedInt.get());
                Try<Boolean> decryptedBoolean = encryptFunction.decryptBoolean.apply(encryptedBoolean.get());

                // Output decrypted values
                System.out.println("Decrypted String: " + decryptedString.get());
                System.out.println("Decrypted Integer: " + decryptedInt.get());
                System.out.println("Decrypted Boolean: " + decryptedBoolean.get());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
