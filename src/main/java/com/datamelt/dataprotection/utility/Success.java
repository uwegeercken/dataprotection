package com.datamelt.dataprotection.utility;

public record Success<T>(T result) implements Try<T>
{
    @Override
    public T get()
    {
        return result;
    }

    @Override
    public Throwable getError()
    {
        throw new RuntimeException("invalid method invocation");
    }
}
