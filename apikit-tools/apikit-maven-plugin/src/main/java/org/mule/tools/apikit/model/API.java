/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.model;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

public class API {

    public static final int DEFAULT_PORT = 8081;
    public static final String DEFAULT_BASE_URI = "http://localhost:" + DEFAULT_PORT + "/api";

    private String baseUri;
    private APIKitConfig config;
    private HttpListenerConfig listenerConfig;
    private File xmlFile;
    private File yamlFile;
    private String id;

    public API(File yamlFile, File xmlFile, String baseUri, HttpListenerConfig listenerConfig) {
        this.baseUri = baseUri;
        this.yamlFile = yamlFile;
        this.xmlFile = xmlFile;
        this.listenerConfig = listenerConfig;
        id = FilenameUtils.removeExtension(yamlFile.getName()).trim();
    }

    public API(File yamlFile, File xmlFile, String baseUri, APIKitConfig config, HttpListenerConfig listenerConfig) {
        this(yamlFile, xmlFile, baseUri,listenerConfig);
        this.config = config;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    public File getXmlFile(File rootDirectory) {
        // Case we need to create the file
        if (xmlFile == null) {
            xmlFile = new File(rootDirectory,
                    FilenameUtils.getBaseName(
                            yamlFile.getAbsolutePath()) + ".xml");
        }
        return xmlFile;
    }

    public File getYamlFile() {
        return yamlFile;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public String getPath() {
        return "api"; //TODO DO NOT HARDCODE THIS - FEDE
    }

    public HttpListenerConfig getListenerConfig() {
        return listenerConfig;
    }

    public APIKitConfig getConfig() {
        return config;
    }

    public void setConfig(APIKitConfig config) {
        this.config = config;
    }

    public void setDefaultConfig() {
        config = new APIKitConfig.Builder(yamlFile.getName()).setName(id + "-" + APIKitConfig.DEFAULT_CONFIG_NAME).build();
    }

    public void setDefaultHttpListenerConfig() {
        listenerConfig = new HttpListenerConfig.Builder(HttpListenerConfig.DEFAULT_CONFIG_NAME, this.getHost(), this.getPort()).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        API api = (API) o;

        if (!yamlFile.equals(api.yamlFile)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return yamlFile.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getHost()
    {
        return "localhost";//TODO DO NOT HARDCODE THIS - FEDE
    }

    public String getPort()
    {
        return String.valueOf(DEFAULT_PORT);//TODO DO NOT HARDCODE THIS - FEDE
    }
}
