/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;
import org.mule.tools.apikit.misc.APIKitTools;
import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIFactory;
import org.mule.tools.apikit.model.HttpListenerConfig;
import org.mule.tools.apikit.output.GenerationModel;
import org.mule.tools.apikit.model.ResourceActionMimeTypeTriplet;

import org.raml.model.Action;
import org.raml.model.MimeType;
import org.raml.model.Raml;
import org.raml.model.Resource;
import org.raml.parser.loader.CompositeResourceLoader;
import org.raml.parser.loader.DefaultResourceLoader;
import org.raml.parser.loader.FileResourceLoader;
import org.raml.parser.loader.ResourceLoader;
import org.raml.parser.rule.ValidationResult;
import org.raml.parser.visitor.RamlDocumentBuilder;
import org.raml.parser.visitor.RamlValidationService;

public class RAMLFilesParser
{
    private Map<ResourceActionMimeTypeTriplet, GenerationModel> entries = new HashMap<ResourceActionMimeTypeTriplet, GenerationModel>();
    private final APIFactory apiFactory;
    private final Log log;

    public RAMLFilesParser(Log log, Map<File, InputStream> fileStreams, APIFactory apiFactory)
    {
        this.log = log;
        this.apiFactory = apiFactory;
        List<File> processedFiles = new ArrayList<File>();
        for (Map.Entry<File, InputStream> fileInputStreamEntry : fileStreams.entrySet())
        {
            String content;
            File ramlFile = fileInputStreamEntry.getKey();
            try
            {
                content = IOUtils.toString(fileInputStreamEntry.getValue());
            }
            catch (IOException ioe)
            {
                this.log.info("Error loading file " + ramlFile.getName());
                break;

            }
            ResourceLoader resourceLoader = new CompositeResourceLoader(new DefaultResourceLoader(), new FileResourceLoader(ramlFile.getParentFile()));

            if (isValidYaml(ramlFile.getName(), content, resourceLoader))
            {
                RamlDocumentBuilder builderNodeHandler = new RamlDocumentBuilder(resourceLoader);
                try
                {
                    Raml raml = builderNodeHandler.build(content, ramlFile.getName());
                    String host = APIKitTools.getHostFromUri(raml.getBaseUri());
                    if (host == "")
                    {
                        host = HttpListenerConfig.DEFAULT_HOST;
                    }
                    String port = APIKitTools.getPortFromUri(raml.getBaseUri());
                    if (port == "")
                    {
                        port = HttpListenerConfig.DEFAULT_PORT;
                    }
                    String basePath = APIKitTools.getPathFromUri(raml.getBaseUri());
                    if (basePath == "")
                    {
                        port = HttpListenerConfig.DEFAULT_BASE_PATH;
                    }
                    collectResources(ramlFile, raml.getResources(), host, port, basePath, "/");
                    processedFiles.add(ramlFile);
                }
                catch (Exception e)
                {
                    log.info("Could not parse [" + ramlFile + "] as root RAML file. Reason: " + e.getMessage());
                    log.debug(e);
                }
            }

        }
        if (processedFiles.size() > 0)
        {
            this.log.info("The following RAML files were parsed correctly: " +
                     processedFiles);
        }
        else
        {
            this.log.error("RAML Root not found. None of the files were recognized as valid root RAML files.");
        }
    }

    private boolean isValidYaml(String fileName, String content, ResourceLoader resourceLoader)
    {
        List<ValidationResult> validationResults = RamlValidationService.createDefault(resourceLoader).validate(content, fileName);
        if (validationResults != null && !validationResults.isEmpty())
        {
            log.info("File '" + fileName + "' is not a valid root RAML file. See following error(s): ");
            int errorCount = 1;
            for (ValidationResult validationResult : validationResults)
            {
                log.info("Error " + errorCount + ": " + validationResult.toString());
                errorCount++;
            }
            return false;
        }
        return true;
    }

    void collectResources(File filename, Map<String, Resource> resourceMap, String host, String port, String basePath, String path)
    {
        for (Resource resource : resourceMap.values())
        {
            for (Action action : resource.getActions().values())
            {
                API api = apiFactory.createAPIBinding(filename, null, null, new HttpListenerConfig.Builder(HttpListenerConfig.DEFAULT_CONFIG_NAME, host, port, basePath).build(), path);

                Map<String, MimeType> mimeTypes = action.getBody();
                boolean addGenericAction = false;
                if (mimeTypes != null)
                {
                    for (MimeType mimeType : mimeTypes.values())
                    {
                        if (mimeType.getSchema() != null || mimeType.getFormParameters() != null)
                        {
                            addResource(api, resource, action, basePath, path, mimeType.getType());
                        }
                        else { addGenericAction = true; }
                    }
                }
                else { addGenericAction = true; }

                if (addGenericAction) {
                    addResource(api, resource, action, basePath, path, null);
                }
            }

            collectResources(filename, resource.getResources(), host, port, basePath, path);
        }
    }

    void addResource(API api, Resource resource, Action action,String basePath, String path, String mimeType) {
        String completePath = basePath + path;
        if (completePath.endsWith("/"))
        {
            completePath = completePath.substring(0,completePath.length() -1);
        }

        if (completePath.contains("//"))
        {
            completePath = completePath.replace("//","/");
        }
        ResourceActionMimeTypeTriplet resourceActionTriplet = new ResourceActionMimeTypeTriplet(api, completePath + resource.getUri(),
                action.getType().toString(), mimeType);
        entries.put(resourceActionTriplet, new GenerationModel(api, resource, action, mimeType));
    }

    public Map<ResourceActionMimeTypeTriplet, GenerationModel> getEntries()
    {
        return entries;
    }
}
