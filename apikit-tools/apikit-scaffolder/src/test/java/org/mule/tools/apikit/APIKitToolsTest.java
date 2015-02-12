/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.mule.tools.apikit.misc.APIKitTools;
import org.mule.tools.apikit.model.API;

import org.junit.Assert;
import org.junit.Test;

public class APIKitToolsTest
{

    @Test
    public void getPartsFromUris()
    {
        String uri = "http://localhost";
        Assert.assertEquals("localhost", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals(Integer.toString(API.DEFAULT_PORT), APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/*", APIKitTools.getPathFromUri(uri,true));
        Assert.assertEquals("/", APIKitTools.getPathFromUri(uri,false));

        uri = "http://localhost.com:333/path/path2/";
        Assert.assertEquals("localhost.com", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals("333", APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/path/path2/*", APIKitTools.getPathFromUri(uri,true));
        Assert.assertEquals("/path/path2/", APIKitTools.getPathFromUri(uri,false));

        uri = "http://localhost.com:${port}/path/path2/";
        Assert.assertEquals("localhost.com", APIKitTools.getHostFromUri(uri));
        Assert.assertEquals("${port}", APIKitTools.getPortFromUri(uri));
        Assert.assertEquals("/path/path2/*", APIKitTools.getPathFromUri(uri,true));
        Assert.assertEquals("/path/path2/", APIKitTools.getPathFromUri(uri,false));

    }

    @Test
    public void isForcedToCreateInboundEndpointsTest(){
        assertTrue(APIKitTools.isForcedToCreateInboundEndpoints("3.5.0"));
        assertFalse(APIKitTools.isForcedToCreateInboundEndpoints("3.6.0"));
        assertTrue(APIKitTools.isForcedToCreateInboundEndpoints("3.5.1-SNAPSHOT"));
        assertFalse(APIKitTools.isForcedToCreateInboundEndpoints("3.6.0-SNAPSHOT"));
        assertFalse(APIKitTools.isForcedToCreateInboundEndpoints("3.6.1"));
        assertTrue(APIKitTools.isForcedToCreateInboundEndpoints("3.5.11"));
        assertFalse(APIKitTools.isForcedToCreateInboundEndpoints(null));
    }
}
