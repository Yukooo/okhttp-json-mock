package com.okhttpjsonstub.providers;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DefaultStreamProvider implements StreamProvider {
    @Override
    @NotNull
    public InputStream provide(@NotNull final String path) {
        try {
            return new FileInputStream(new File(path));
        } catch (final Exception error) {
            throw new IllegalArgumentException(error);
        }
    }
}
