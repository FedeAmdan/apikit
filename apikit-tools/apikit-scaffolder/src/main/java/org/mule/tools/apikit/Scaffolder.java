/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit;

import org.mule.tools.apikit.misc.APIKitTools;
import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIFactory;
import org.mule.tools.apikit.output.GenerationModel;
import org.mule.tools.apikit.output.GenerationStrategy;
import org.mule.tools.apikit.output.MuleConfigGenerator;
import org.mule.tools.apikit.misc.FileListUtils;
import org.mule.tools.apikit.input.MuleConfigParser;
import org.mule.tools.apikit.input.RAMLFilesParser;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class Scaffolder {
    private final MuleConfigGenerator muleConfigGenerator;

    public static Scaffolder createScaffolder(ApikitLogger log, File muleXmlOutputDirectory,
                                   List<String> specFiles, List<String> muleXmlFiles, String muleVersion) {
        FileListUtils fileUtils = new FileListUtils(log);

        Map<File, InputStream> fileInputStreamMap = fileUtils.toStreamsOrFail(specFiles);
        Map<File, InputStream> streams = fileUtils.toStreamsOrFail(muleXmlFiles);

        return new Scaffolder(log, muleXmlOutputDirectory, fileInputStreamMap, streams, muleVersion);
    }

    public Scaffolder(ApikitLogger log, File muleXmlOutputDirectory,  Map<File, InputStream> ramls,
                      Map<File, InputStream> xmls, String muleVersion)  {
        APIFactory apiFactory = new APIFactory();
        RAMLFilesParser RAMLFilesParser = new RAMLFilesParser(log, ramls, apiFactory);
        if (APIKitTools.isForcedToCreateInboundEndpoints(muleVersion)){
            xmls = new HashMap<File, InputStream>();
        }
        MuleConfigParser muleConfigParser = new MuleConfigParser(log, ramls.keySet(), xmls, apiFactory);

        List<GenerationModel> generationModels = new GenerationStrategy(log).generate(RAMLFilesParser, muleConfigParser);
        muleConfigGenerator = new MuleConfigGenerator(log, muleXmlOutputDirectory, generationModels);
    }

    public void run() {
        muleConfigGenerator.generate();
    }

}
