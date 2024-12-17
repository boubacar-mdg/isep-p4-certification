package com.isep.certification.commons.digest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.DigestScheme;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import com.isep.certification.system.services.SystemParameterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Slf4j
public class HttpComponentsClientHttpRequestFactoryDigestAuth extends HttpComponentsClientHttpRequestFactory {
    HttpHost host;
    SystemParameterService systemParameterService;

    public HttpComponentsClientHttpRequestFactoryDigestAuth(final HttpHost host, final HttpClient httpClient,
            final SystemParameterService systemParameterService) {
        super(httpClient);
        this.host = host;
        this.systemParameterService = systemParameterService;
    }

    private static SecureRandom secureRandom = new SecureRandom();

    private static String generateNonce() {
        byte[] nonceBytes = new byte[16];
        secureRandom.nextBytes(nonceBytes);
        return DigestUtils.md5Hex(nonceBytes);
    }

    //
    @Override
    protected HttpContext createHttpContext(final HttpMethod httpMethod, final URI uri) {
        return createHttpContext();
    }

    private HttpContext createHttpContext() {
        // Create AuthCache instance
        final AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local auth
        // cache
        final DigestScheme digestAuth = new DigestScheme();
        // If we already know the realm name
        digestAuth.initPreemptive(
                new UsernamePasswordCredentials(
                        systemParameterService.getParameterValueByCode("INTOUCH_DIGEST_USERNAME",
                                ""),
                        systemParameterService
                                .getParameterValueByCode("INTOUCH_DIGEST_PASS",
                                        "")
                                .toCharArray()),
                generateNonce(), "CERTFICATION");

        log.info("Digest authentication: {}", digestAuth.toString());

        authCache.put(host, digestAuth);

        // Add AuthCache to the execution context
        final BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
        return localcontext;
    }

}