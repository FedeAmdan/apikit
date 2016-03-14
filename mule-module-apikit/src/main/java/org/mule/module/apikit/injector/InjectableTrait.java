/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.apikit.injector;

import java.util.Map;

import org.raml.interfaces.model.IAction;
import org.raml.interfaces.model.IResponse;

public class InjectableTrait extends InjectableRamlFeature
{

    private static final String RESOURCE = "/base";
    private static final String ACTION = "get";
    private static final String TEMPLATE_BEFORE = "#%RAML 0.8\ntitle: t\ntraits:\n - injected:\n";
    private static final String TEMPLATE_AFTER = RESOURCE + ":\n " + ACTION + ":\n  is: [injected]\n";
    private static final String INDENTATION = "    ";

    private IAction cache;

    public InjectableTrait(String name, String yaml)
    {
        super(name, yaml);
    }

    @Override
    protected String getBoilerPlateBefore()
    {
        return TEMPLATE_BEFORE;
    }

    @Override
    protected String getBoilerPlateAfter()
    {
        return TEMPLATE_AFTER;
    }

    @Override
    protected String getIndentation()
    {
        return INDENTATION;
    }

    @Override
    public void applyToAction(IAction target)
    {
        if (cache == null)
        {
            cache = parse().getResource(RESOURCE).getAction(ACTION);
        }
        IAction source = resolveParams(target);
        mergeActions(target, source);
        target.addIs(name);
    }

    private void mergeActions(IAction target, IAction source)
    {
        target.setHeaders(putAllSkipExisting(target.getHeaders(), source.getHeaders()));
        target.setQueryParameters(putAllSkipExisting(target.getQueryParameters(), source.getQueryParameters()));
        if (source.getBody() != null)
        {
            target.setBody(putAllSkipExisting(target.getBody(), source.getBody()));
        }
        for (Map.Entry<String, IResponse> response : source.getResponses().entrySet())
        {
            if (target.getResponses().containsKey(response.getKey()))
            {
                target.getResponses().get(response.getKey()).setBody(putAllSkipExisting(target.getResponses().get(response.getKey()).getBody(), response.getValue().getBody()));
                target.getResponses().get(response.getKey()).setHeaders(putAllSkipExisting(target.getResponses().get(response.getKey()).getHeaders(), response.getValue().getHeaders()));
            }
            else
            {
                target.addResponse(response.getKey(), response.getValue());
//                target.getResponses().put(response.getKey(), response.getValue());
            }
        }
    }

    private IAction resolveParams(IAction target)
    {
        //TODO replace implicit parameters
        return cache;
    }

    private <K, V> Map<K, V> putAllSkipExisting(Map<K, V> to, Map<K, V> from)
    {
        if (to != null && from != null)
        {
            for (Map.Entry<K, V> entry : from.entrySet())
            {
                if (!to.containsKey(entry.getKey()))
                {
                    to.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return to;
    }

}
