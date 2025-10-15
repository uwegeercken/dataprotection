package com.datamelt.dataprotection.utility;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Try<T> permits Success, Failure
{
    T get();
    Throwable getError();

    static <T> Try<T> of(Callable<T> code)
    {
        try
        {
            return new Success<T>(code.call());
        }
        catch (Throwable throwable)
        {
            return new Failure<>(throwable);
        }
    }

    default boolean isSuccess()
    {
        return this instanceof Success<T>;
    }

    default boolean isFailure()
    {
        return this instanceof Failure<T>;
    }

    default <R> Try<R> map(Function<T,R> mapper)
    {
        if(isSuccess())
        {
            return of(() -> mapper.apply(get()));
        }
        else
        {
            return new Failure<>(getError());
        }
    }

    default <R> Try<R> mapUsingTry(Function<T,Try<R>> mapper)
    {
        if(isSuccess())
        {
            return mapper.apply(get());
        }
        else
        {
            return new Failure<>(getError());
        }
    }

    default Try<T> orElse(Supplier<T> alternative)
    {
        if(isFailure())
        {
            return of(alternative::get);
        }
        else
        {
            return this;
        }
    }

    default Try<T> orElse(T alternative)
    {
        if(isFailure())
        {
            return new Success<>(alternative);
        }
        else
        {
            return this;
        }
    }

    default <R> Try<R> recover(Function<Throwable,R> mapper)
    {
        if(isFailure())
        {
            return of(() -> mapper.apply(getError()));
        }
        else
        {
            return new Failure<>(new RuntimeException("recover called on success object"));
        }
    }

    default void ifSuccess(Consumer<T> consumer)
    {
        if(isSuccess())
        {
            consumer.accept(get());
        }
    }

    default void ifFailure(Consumer<Throwable> consumer)
    {
        if(isFailure())
        {
            consumer.accept(getError());
        }
    }
}
