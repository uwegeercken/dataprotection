package com.datamelt.dataprotection.utility;

import java.util.stream.Stream;

public enum ProgramArgument
{
    KEYFILE_NAME( "keyfilename", "secretKey", true),
    KEYFILE_PATH( "keyfilepath","/tmp", true);

    private final String key;
    private final String defaultValue;
    private final boolean mandatory;

    ProgramArgument(String key, String defaultValue, boolean mandatory)
    {
        this.key = key;
        this.defaultValue = defaultValue;
        this.mandatory = mandatory;
    }

    public String getKey()
    {
        return key;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public static ProgramArgument fromKey(String key) {
        return Stream.of(values())
                .filter(arg -> arg.getKey().equalsIgnoreCase(key))
                .findFirst()
                .orElse(null);
    }
}
