/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.injector;

import org.raml.interfaces.RamlFactoryHelper;
import org.raml.interfaces.model.IAction;
import org.raml.interfaces.model.IRaml;

public abstract class InjectableRamlFeature
{
    protected String name;
    private String yaml;

    protected InjectableRamlFeature(String name, String yaml)
    {
        if (name == null || yaml == null)
        {
            throw new IllegalArgumentException("neither name nor yaml can be null");
        }
        this.name = name;
        this.yaml = yaml;
    }

    protected IRaml parse()
    {
        StringBuilder wholeYaml = new StringBuilder(getBoilerPlateBefore());
        String[] split = yaml.split("[\r\n]+");
        for (String s : split)
        {
            wholeYaml.append(getIndentation()).append(s).append("\n");
        }
        wholeYaml.append(getBoilerPlateAfter());
        return RamlFactoryHelper.createRamlDocumentBuilder().build(wholeYaml.toString(), "");
    }

    public abstract void applyToAction(IAction target);

    protected abstract String getBoilerPlateBefore();

    protected abstract String getBoilerPlateAfter();

    protected abstract String getIndentation();

}
