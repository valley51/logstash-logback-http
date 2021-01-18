package de.idealo.whitelabels.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import java.nio.charset.StandardCharsets;

public class HttpAppender extends AppenderBase<ILoggingEvent> {

    private Encoder<ILoggingEvent> encoder;
    private HttpClient httpClient;

    @Override
    public synchronized void start() {
        if (isStarted()) {
            return;
        }
        verifyConfigurationParameters();
        addInfo("HttpAppender started");
        super.start();
    }

    @Override
    public void append(ILoggingEvent event) {
        if (!isStarted()) {
            return;
        }
        byte[] bytes = encoder.encode(event);
        String json = new String(bytes, StandardCharsets.UTF_8);
        httpClient.put(json);
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private void verifyConfigurationParameters() {
        if (encoder == null) {
            addError("No encoder was configured. Use <encoder> to specify the class name");
        }
        if (httpClient == null) {
            addError("No destination was configured. Use <httpClient> to specify HttpClient");
        }
    }

}
