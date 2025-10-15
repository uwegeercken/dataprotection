package com.datamelt.dataprotection.utility;

import java.util.EnumMap;
import java.util.stream.Stream;

public class ArgumentParser
{
    private final EnumMap<ProgramArgument, String> argumentMap;

    public ArgumentParser(String[] args)
    {
        argumentMap = new EnumMap<>(ProgramArgument.class);
        initializeDefaults();
        parseArguments(args);
        validateMandatoryArguments();
    }

    private void initializeDefaults()
    {
        for (ProgramArgument arg : ProgramArgument.values()) {
            argumentMap.put(arg, arg.getDefaultValue());
        }
    }

    private void parseArguments(String[] args)
    {
        Stream.of(args).forEach(arg -> {
            String[] keyValue = arg.split("[=:]", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().toLowerCase();
                ProgramArgument programArg = ProgramArgument.fromKey(key);

                if (programArg != null) {
                    argumentMap.put(programArg, keyValue[1].trim());
                } else {
                    System.err.println("Invalid argument: " + key);
                }
            } else {
                System.err.println("Invalid argument format: " + arg);
            }
        });
    }

    private void validateMandatoryArguments() {
        Stream.of(ProgramArgument.values())
                .filter(ProgramArgument::isMandatory) // Filter for mandatory arguments
                .filter(arg -> argumentMap.get(arg).equals(arg.getDefaultValue()))
                .findFirst()
                .ifPresent(arg -> {
                    System.err.println("Error: Missing mandatory argument: " + arg.getKey());
                    System.exit(1);
                });
    }

    public String getArgument(ProgramArgument arg) {
        return argumentMap.get(arg);
    }
}
