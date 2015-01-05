package org.mule.tools.apikit.model;

import org.apache.commons.lang.StringUtils;

public class HttpListenerConfig
{
    public static final String ELEMENT_NAME = "listener-config";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String DEFAULT_CONFIG_NAME = "HTTP_Listener_Configuration";


    private String name;
    private String host;
    private String port;

    public static class Builder {
        private String name;
        private String host;
        private String port;

        public Builder(final String name, final String host, final String port) {
            if(StringUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name attribute cannot be null or empty");
            }
            if(StringUtils.isEmpty(host)) {
                throw new IllegalArgumentException("Host attribute cannot be null or empty");
            }
            if(StringUtils.isEmpty(port)) {
                throw new IllegalArgumentException("Port attribute cannot be null or empty");
            }
            this.name = name;
            this.host = host;
            this.port = port;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(String port) {
            this.port = port;
            return this;
        }

        public HttpListenerConfig build() {
            return new HttpListenerConfig(this.name, this.host, this.port);
        }
    }

    public HttpListenerConfig(final String name,
                         final String host,
                         final String port) {
        this.name = name;
        this.host = host;
        this.port = port;

    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

}
