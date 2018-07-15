package com.okhttpjsonstub.helpers;

import com.okhttpjsonstub.providers.StreamProvider;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ResourcesHelper {
    /**
     * Reads a file.
     *
     * @param streamProvider the {@link StreamProvider}
     * @param name           the name of file to be read
     * @return the string of a file
     */
    @NotNull
    public static String readFile(@NotNull final StreamProvider streamProvider, @NotNull final String name) {
        final InputStream inputStream = streamProvider.provide(name);

        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            final StringBuilder stringBuilder = new StringBuilder();

            String line;
            boolean isFirst = true;
            while ((line = bufferedReader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    stringBuilder.append('\n');
                }
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (final IOException errror) {
            throw new IllegalArgumentException(errror);
        }
    }
}
