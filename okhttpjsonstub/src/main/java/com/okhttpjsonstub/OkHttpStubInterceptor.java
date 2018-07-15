package com.okhttpjsonstub;

import com.okhttpjsonstub.helpers.ResourcesHelper;
import com.okhttpjsonstub.providers.DefaultStreamProvider;
import com.okhttpjsonstub.providers.StreamProvider;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * The {@link OkHttpStubInterceptor} is an implementation of {@link Interceptor} to provide a stub data.
 */
public class OkHttpStubInterceptor implements Interceptor {
    private final static String DEFAULT_BASE_PATH = "";
    private final static int DELAY_DEFAULT_MIN = 500;
    private final static int DELAY_DEFAULT_MAX = 1500;

    private final int failurePercentage;
    private final String basePath;
    private final StreamProvider streamProvider;
    private final int minDelayMilliseconds;
    private final int maxDelayMilliseconds;

    public OkHttpStubInterceptor(final int failurePercentage) {
        this(new DefaultStreamProvider(), failurePercentage, DEFAULT_BASE_PATH, DELAY_DEFAULT_MIN, DELAY_DEFAULT_MAX);
    }

    public OkHttpStubInterceptor(@NotNull final StreamProvider streamProvider,
                                 final int failurePercentage) {
        this(streamProvider, failurePercentage, DEFAULT_BASE_PATH, DELAY_DEFAULT_MIN, DELAY_DEFAULT_MAX);
    }

    public OkHttpStubInterceptor(@NotNull final StreamProvider streamProvider,
                                 final int failurePercentage,
                                 final int minDelayMilliseconds,
                                 final int maxDelayMilliseconds) {
        this(streamProvider, failurePercentage, DEFAULT_BASE_PATH, minDelayMilliseconds, maxDelayMilliseconds);
    }

    public OkHttpStubInterceptor(@NotNull final StreamProvider streamProvider,
                                 final int failurePercentage,
                                 @NotNull final String basePath,
                                 final int minDelayMilliseconds,
                                 final int maxDelayMilliseconds) {
        this.streamProvider = streamProvider;
        this.failurePercentage = failurePercentage;
        this.basePath = basePath;
        this.minDelayMilliseconds = minDelayMilliseconds;
        this.maxDelayMilliseconds = maxDelayMilliseconds;
    }

    @Override
    public Response intercept(@NotNull final Chain chain) {
        final HttpUrl url = chain.request().url();

        final String query = url.encodedQuery();
        final String path = url.encodedPath().substring(1) + ((query == null || query.length() == 0) ? "" : ("/" + query));

        final boolean isRandomFail = Math.abs(new Random().nextInt() % 100) < failurePercentage;

        final int statusCode = isRandomFail ? 504 : 200;
        final String response = ResourcesHelper.readFile(streamProvider, basePath + path + ".json");

        try {
            final long timeDelay = Math.abs(new Random().nextInt() % (maxDelayMilliseconds - minDelayMilliseconds)) + minDelayMilliseconds;
            Thread.sleep(timeDelay);
        } catch (final InterruptedException ignored) {
            //nothing here
        }

        return new Response.Builder()
                .code(statusCode)
                .message(response)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create(MediaType.parse("application/json"), response))
                .addHeader("content-type", "application/json")
                .build();
    }
}