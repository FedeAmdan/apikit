package org.mule.tools.apikit;

import org.mule.tools.apikit.misc.APIKitTools;
import org.mule.tools.apikit.model.HttpListenerConfig;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class APIKitToolsTest
{

    @Test
    public void getPartsFromValidUris()
    {
        String uri = "http://localhost";
        Assert.assertEquals("localhost", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals(HttpListenerConfig.DEFAULT_PORT, APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/*", APIKitTools.getPathFromUri(uri,true));

        uri = "http://localhost.com:333/path/path2/";
        Assert.assertEquals("localhost.com", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals("333", APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/path/path2/*", APIKitTools.getPathFromUri(uri,true));

        uri = "http://localhost.com:${port}/path/path2/";
        Assert.assertEquals("localhost.com", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals("${port}", APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/path/path2/*", APIKitTools.getPathFromUri(uri,true));

    }

    @Ignore
    @Test
    public void getPartsFromInvalidUris()
    {
        String uri = "http:localhost";
        Assert.assertEquals("localhost", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals(HttpListenerConfig.DEFAULT_PORT, APIKitTools.getPortFromUri(uri));
        Assert.assertEquals(HttpListenerConfig.DEFAULT_BASE_PATH, APIKitTools.getPathFromUri(uri,true));

        uri = "http//localhost.com:333/path/path2/";
        Assert.assertEquals("localhost.com", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals("333", APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/path/path2/*", APIKitTools.getPathFromUri(uri,true));
    }

}
