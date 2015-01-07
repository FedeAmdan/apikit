/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.input.parsers;

import org.mule.tools.apikit.input.APIKitFlow;
import org.mule.tools.apikit.misc.APIKitTools;
import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIFactory;
import org.mule.tools.apikit.model.APIKitConfig;
import org.mule.tools.apikit.model.HttpListenerConfig;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class APIKitRoutersParser implements MuleConfigFileParser {

    private final Map<String, APIKitConfig> apikitConfigs;
    private final Set<File> yamlPaths;
    private final File file;
    private final APIFactory apiFactory;

    public APIKitRoutersParser(final Map<String, APIKitConfig> apikitConfigs,
                               final Set<File> yamlPaths,
                               final File file,
                               final APIFactory apiFactory) {
        this.apikitConfigs = apikitConfigs;
        this.yamlPaths = yamlPaths;
        this.file = file;
        this.apiFactory = apiFactory;
    }

    @Override
    public Map<String, API> parse(Document document)
    {
        Map<String, API> includedApis = new HashMap<String, API>();

        XPathExpression<Element> xp = XPathFactory.instance().compile("//*/*[local-name()='router']",
                                                                      Filters.element(APIKitTools.API_KIT_NAMESPACE.getNamespace()));
        List<Element> elements = xp.evaluate(document);
        for (Element element : elements) {
            Attribute configRef = element.getAttribute("config-ref");
            String configId = configRef != null ? configRef.getValue() : APIKitFlow.UNNAMED_CONFIG_NAME;

            APIKitConfig config = apikitConfigs.get(configId);
            if(config == null) {
                throw new IllegalStateException("An Apikit configuration is mandatory.");
            }

            //Attribute configRef = element.getAttribute("config-ref");
            //String configId = configRef != null ? configRef.getValue() : APIKitFlow.UNNAMED_CONFIG_NAME;
            //
            //APIKitConfig config = apikitConfigs.get(configId);
            //if(config == null) {
            //    throw new IllegalStateException("An Apikit configuration is mandatory.");
            //}


            for (File yamlPath : yamlPaths) {
                if (yamlPath.getName().equals(config.getRaml())) {
                    Element listener = element.getParentElement().getChildren().get(0);
                    Element listenerConfig = element.getParentElement().getParentElement().getChildren().get(0);

                    // TODO Unhack, it is assuming that the router will always be in a flow
                    // where the first element is going to be an http listener
                    if (!"listener".equals(listener.getName())) {
                        throw new IllegalStateException("The first element of the main flow must be a " +
                                                        "listener");
                    }

                    String path = listener.getAttributeValue("path");

                    // Case the user is specifying baseURI using address attribute
                    if (path == null) {
                        path = "";
                        //String address = listener.getAttributeValue("address");
                        //
                        //if (address == null) {
                        //    throw new IllegalStateException("Neither 'path' nor 'address' attribute was used. " +
                        //                                    "Cannot retrieve base URI.");
                        //}

                        //path = address;
                    } else  if (!path.startsWith("/")) {
                        path = "/" + path;
                    }

                    String host = listenerConfig.getAttributeValue("host");
                    if (host == null)
                    {
                        throw new IllegalStateException("Cannot retrieve host.");
                    }
                    String port = listenerConfig.getAttributeValue("port");
                    if (port == null)
                    {
                        port = HttpListenerConfig.DEFAULT_PORT;
                    }
                    else if (port.startsWith("${") && port.endsWith("}"))
                    {
                        port = System.getProperty(port.substring(2,port.length()-1));
                    }

                    String basePath = listenerConfig.getAttributeValue("basePath");
                    if (basePath == null)
                    {
                        basePath = "";
                    }
                    else  if (!basePath.startsWith("/")) {
                        basePath = "/" + basePath;
                    }

                    String address = "http://" + host + ":" + port + basePath + path;



                includedApis.put(configId, apiFactory.createAPIBinding(yamlPath, file, address, config));
                }
            }
        }

        return includedApis;
    }
}
