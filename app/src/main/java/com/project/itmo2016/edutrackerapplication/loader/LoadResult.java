package com.project.itmo2016.edutrackerapplication.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Aleksandr Tukallo on 01.12.16.
 */

/**
 * Result of data loading
 */

public class LoadResult<T> {

    /**
     * Result of loading (ok/no internet connection/another error)
     */
    @NonNull
    public final ResultType resultType;

    /**
     * Loaded data (if resultType is ok) or null (else)
     */
    @Nullable
    public final T data;

    public LoadResult(@NonNull ResultType resultType, @Nullable T data) {
        this.resultType = resultType;
        this.data = data;
    }
}
