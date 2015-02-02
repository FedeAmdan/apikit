/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.model;

import org.mule.tools.apikit.misc.APIKitTools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;

public class API {

    public static final String DEFAULT_PATH = "/api/*";

    private APIKitConfig config;
    private HttpListenerConfig httpListenerConfig;
    private String path;

    private String baseUri;
    private File xmlFile;
    private File yamlFile;
    private String id;
    private Boolean useInboundEndpoint;


    public API(File yamlFile, File xmlFile, String baseUri) {
        this.yamlFile = yamlFile;
        this.xmlFile = xmlFile;
        this.baseUri = baseUri;
        id = FilenameUtils.removeExtension(yamlFile.getName()).trim();
    }

    public API(File yamlFile, File xmlFile, HttpListenerConfig httpListenerConfig, String path) {
        this(yamlFile,xmlFile,null);
        this.httpListenerConfig = httpListenerConfig;
        this.path = path;
    }

    public API(File yamlFile, File xmlFile, APIKitConfig config, String baseUri, Boolean useInboundEndpoint){
        this(yamlFile, xmlFile, null, baseUri);
        this.useInboundEndpoint = useInboundEndpoint;
        if (!useInboundEndpoint)
        {
            String httpListenerConfigName = id == null ? HttpListenerConfig.DEFAULT_CONFIG_NAME : id + "-" + HttpListenerConfig.DEFAULT_CONFIG_NAME;
            this.httpListenerConfig = new HttpListenerConfig.Builder(httpListenerConfigName, baseUri).build();
            this.path = APIKitTools.getPathFromUri(baseUri);
        }
        this.config = config;
    }
    //
    //public API(File yamlFile, File xmlFile, APIKitConfig config, String baseUri, String path)
    //{
    //    this(yamlFile, xmlFile,config,baseUri,path,false);
    //}

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

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public HttpListenerConfig getHttpListenerConfig() {
        return httpListenerConfig;
    }

    public APIKitConfig getConfig() {
        return config;
    }

    public void setConfig(APIKitConfig config) {
        this.config = config;
    }

    public void setHttpListenerConfig(HttpListenerConfig httpListenerConfig) {
        this.httpListenerConfig = httpListenerConfig;
        this.useInboundEndpoint = false;
    }

    public void setDefaultConfig() {
        config = new APIKitConfig.Builder(yamlFile.getName()).setName(id + "-" + APIKitConfig.DEFAULT_CONFIG_NAME).build();
    }

    public Boolean useInboundEndpoint()
    {
        return useInboundEndpoint;
    }
    public boolean setUseInboundEndpoint(Boolean useInboundEndpoint)
    {
        return this.useInboundEndpoint = useInboundEndpoint;
    }
    public String getBaseUri()
    {
        return baseUri;
    }

    public void setBaseUri(String baseUri)
    {
        this.baseUri = baseUri;
    }

    //public void setDefaultHttpListenerConfig() {
    //    httpListenerConfig = new HttpListenerConfig(HttpListenerConfig.DEFAULT_CONFIG_NAME, HttpListenerConfig.DEFAULT_HOST, HttpListenerConfig.DEFAULT_PORT, HttpListenerConfig.DEFAULT_BASE_PATH);
    //}

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

}
