package org.mule.tools.apikit.output.scopes;

import static org.mule.tools.apikit.output.MuleConfigGenerator.HTTP_NAMESPACE;

import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIKitConfig;
import org.mule.tools.apikit.model.HttpListenerConfig;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Element;

public class HttpListenerConfigScope implements Scope
{
    //    private final Element httpListener;

    private final Element mule;
    private final Element httpListenerConfig;

    public HttpListenerConfigScope(API api, Element mule)
    {
        this.mule = mule;

        if (api.getHttpListenerConfig() != null)
        {
            httpListenerConfig = new Element(HttpListenerConfig.ELEMENT_NAME, HTTP_NAMESPACE.getNamespace());

            //httpListener = new Element("listener", HTTP_NAMESPACE.getNamespace());
            if(!StringUtils.isEmpty(httpListenerConfig.getName())) {
                httpListenerConfig.setAttribute("name", HttpListenerConfig.DEFAULT_CONFIG_NAME);
            }
            httpListenerConfig.setAttribute("host", api.getHttpListenerConfig().getHost());
            httpListenerConfig.setAttribute("port", api.getHttpListenerConfig().getPort());
            httpListenerConfig.setAttribute("basePath", api.getHttpListenerConfig().getBasePath());
            mule.addContent(httpListenerConfig);
        }
        else
            httpListenerConfig = null;
    }

    @Override
    public Element generate()
    {
        return httpListenerConfig;

    }
}