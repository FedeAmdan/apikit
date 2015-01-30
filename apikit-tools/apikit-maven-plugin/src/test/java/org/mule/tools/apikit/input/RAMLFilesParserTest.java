package org.mule.tools.apikit.input;


import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIFactory;
import org.mule.tools.apikit.model.ResourceActionMimeTypeTriplet;
import org.mule.tools.apikit.output.GenerationModel;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;

public class RAMLFilesParserTest
{

    @Test
    public void testCreation()
    {
        final InputStream resourceAsStream =
                RAMLFilesParserTest.class.getClassLoader().getResourceAsStream(
                        "create-mojo/simple.yaml");
        Log log = mock(Log.class);

        HashSet<File> yamlPaths = new HashSet<File>();
        yamlPaths.add(new File("leagues.yaml"));

        HashMap<File, InputStream> streams = new HashMap<File, InputStream>();
        streams.put(new File(""), resourceAsStream);

        RAMLFilesParser ramlFilesParser = new RAMLFilesParser(log, streams, new APIFactory());

        Map<ResourceActionMimeTypeTriplet, GenerationModel> entries = ramlFilesParser.getEntries();
        assertNotNull(entries);
        assertEquals(1, entries.size());
        Set<ResourceActionMimeTypeTriplet> yamlEntries = entries.keySet();
        ResourceActionMimeTypeTriplet triplet = yamlEntries.iterator().next();
        Assert.assertEquals("/api/pet", triplet.getUri());
        Assert.assertEquals("GET", triplet.getVerb());
        Assert.assertEquals("/api/*",triplet.getApi().getPath());
        Assert.assertNotNull(triplet.getApi().getHttpListenerConfig());
        Assert.assertEquals("localhost", triplet.getApi().getHttpListenerConfig().getHost());
        Assert.assertEquals("8090", triplet.getApi().getHttpListenerConfig().getPort());
        Assert.assertEquals("/", triplet.getApi().getHttpListenerConfig().getBasePath());
        Assert.assertEquals(null,triplet.getApi().getHttpListenerConfig().getName());
    }
}