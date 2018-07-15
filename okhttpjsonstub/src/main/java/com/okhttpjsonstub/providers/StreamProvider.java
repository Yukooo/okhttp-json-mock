package com.okhttpjsonstub.providers;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

/**
 * The {@link StreamProvider} provides a stream to read file resources.
 */
public interface StreamProvider {
    /**
     * Provides a stream to read file resources.
     *
     * @param path the file path
     * @return the {@link InputStream}
     */
    @NotNull
    InputStream provide(@NotNull String path);
}
