package com.datamelt.dataprotection.utility;

public record Failure<T>(Throwable throwable) implements Try<T>
{
    @Override
    public T get()
    {
        throw new RuntimeException("invalid method invocation");
    }

    @Override
    public Throwable getError()
    {
        return throwable;
    }
}

