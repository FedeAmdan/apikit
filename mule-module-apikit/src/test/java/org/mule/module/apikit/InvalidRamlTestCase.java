/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit;

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.mule.module.apikit.exception.ApikitRuntimeException;

import org.junit.Test;
import org.raml.interfaces.IRamlDocumentBuilder;
import org.raml.interfaces.RamlFactory;

public class InvalidRamlTestCase
{

    @Test
    public void invalidRaml() throws Exception
    {

        Router router = new Router();
        router.setConfig(new Configuration());
        router.getConfig().setRaml("org/mule/module/apikit/invalid-config.yaml");
        try
        {
            IRamlDocumentBuilder ramlDocumentBuilder = RamlFactory.createRamlDocumentBuilder();
//            router.getConfig().validateRaml(new DefaultResourceLoader());
            router.getConfig().validateRaml(ramlDocumentBuilder);
            fail();
        }
        catch (ApikitRuntimeException e)
        {
            assertThat(e.getMessage(), containsString("errors found: 2"));
        }
    }

    @Test
    public void invalidRamlLocation() throws Exception
    {

        Router router = new Router();
        router.setConfig(new Configuration());
        router.getConfig().setRaml("invalidRamlLocation.raml");
        try
        {
            IRamlDocumentBuilder ramlDocumentBuilder = RamlFactory.createRamlDocumentBuilder();
            router.getConfig().validateRaml(ramlDocumentBuilder);
//            router.getConfig().validateRaml(new DefaultResourceLoader());
            fail();
        }
        catch (ApikitRuntimeException e)
        {
            assertThat(e.getMessage(), containsString("errors found: 1"));
            assertThat(e.getMessage(), containsString("Raml resource not found  --  file: invalidRamlLocation.raml"));
        }
    }
}
