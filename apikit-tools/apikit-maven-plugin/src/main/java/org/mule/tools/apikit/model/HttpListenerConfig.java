package org.mule.tools.apikit.model;

import org.apache.commons.lang.StringUtils;

public class HttpListenerConfig
{
    public static final String ELEMENT_NAME = "config";
    public static final String NAME_ATTRIBUTE = "name";
    public static final String RAML_ATTRIBUTE = "raml";
    public static final String CONSOLE_ENABLED_ATTRIBUTE = "consoleEnabled";
    public static final String CONSOLE_PATH_ATTRIBUTE = "consolePath";
    public static final String DEFAULT_CONSOLE_PATH = "console";
    public static final String DEFAULT_CONFIG_NAME = "config";


    private String name;
    private String host;
    private String port;

    public static class Builder {
        private String name;
        private String host;
        private String port;

        //public Builder(final String raml) {
        //    if(StringUtils.isEmpty(raml)) {
        //        throw new IllegalArgumentException("Raml attribute cannot be null or empty");
        //    }
        //
        //}

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

    private HttpListenerConfig(final String name,
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
