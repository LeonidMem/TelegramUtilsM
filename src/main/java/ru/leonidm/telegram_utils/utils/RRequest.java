package ru.leonidm.telegram_utils.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class RRequest implements Cloneable {

    private final String url;
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    private InputStream responseInputStream = null;
    private int responseCode = 0;

    public RRequest(String url) {
        this.url = url;
    }

    public RRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * @param keysAndValues Keys and values, like "Content-type", "application/json"
     * @throws IllegalArgumentException If input array has odd amount of values
     * (there isn't a value for last key)
     */
    public RRequest addHeaders(String... keysAndValues) {
        if(keysAndValues.length % 2 == 1) throw new IllegalArgumentException();
        for(int i = 0; i < keysAndValues.length; i += 2) {
            headers.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return this;
    }

    public RRequest addParameter(String key, String value) {
        parameters.put(key, value);
        return this;
    }

    public RRequest addParameters(String... keysAndValues) {
        if(keysAndValues.length % 2 == 1) throw new IllegalArgumentException();
        for(int i = 0; i < keysAndValues.length; i += 2) {
            parameters.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return this;
    }

    public byte[] getResponse(boolean printException) {
        try {
            byte[] out = responseInputStream.readAllBytes();
            responseInputStream.close();
            return out;
        } catch(Exception e) {
            if(printException) e.printStackTrace();
        }

        return null;
    }

    public String getResponseString(boolean printException) {
        try {
            StringBuilder out = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(responseInputStream));
            String line;
            while((line = reader.readLine()) != null) {
                out.append(line).append('\n');
            }

            return out.toString();
        } catch(Exception e) {
            if(printException) e.printStackTrace();
        }

        return null;
    }

    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public RRequest clone() {
        try {
            return (RRequest) super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RRequest send(Method method, boolean printException) {
        return send(method, null, printException);
    }

    public RRequest send(Method method, byte[] bytes, boolean printException) {
        try {

            StringBuilder urlBuilder = new StringBuilder(url);

            if(parameters.size() != 0) {
                urlBuilder.append('?');
                for(Map.Entry<String, String> parameter : parameters.entrySet()) {
                    urlBuilder.append(parameter.getKey()).append('=').append(parameter.getValue()).append('&');
                }

                urlBuilder.replace(urlBuilder.length() - 1, urlBuilder.length(), "");
            }

            URL url = new URL(urlBuilder.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method.toString());
            connection.setDoOutput(true);

            method.consumer.accept(connection, bytes);

            for(Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getValue(), header.getKey());
            }

            connection.setRequestProperty("keepAlive", "false");

            responseCode = connection.getResponseCode();
            responseInputStream = connection.getInputStream();

        } catch(Exception e) {
            if(printException) e.printStackTrace();
        }

        return this;
    }

    public enum Method {
        GET, POST((connection, bytes) -> {
            try {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(bytes);
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        private final BiConsumer<HttpURLConnection, byte[]> consumer;

        Method() {
            this.consumer = (connection, bytes) -> {};
        }

        Method(BiConsumer<HttpURLConnection, byte[]> consumer) {
            this.consumer = consumer;
        }
    }
}
