package com.bzh.dytt.data.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * A generic class that holds a value with its loading status.
 *
 * @param <T>
 */

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> success(@NonNull T data, @Nullable String message) {
        return new Resource<>(Status.SUCCESS, data, message);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public static <T> Resource<T> loading(@Nullable T data, @Nullable String message) {
        return new Resource<>(Status.LOADING, data, message);
    }

    @Override
    public String toString() {
        return "Resource{ address=" + Integer.toHexString(hashCode()) +
                ", status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}