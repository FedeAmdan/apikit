package org.mule.tools.apikit.output.scopes;

import static org.mule.tools.apikit.output.MuleConfigGenerator.HTTP_NAMESPACE;

import org.mule.tools.apikit.model.API;

import org.jdom2.Element;

public class HttpListenerScope implements Scope
{
    private final Element httpListener;

    @Override
    public Element generate()
    {
        return httpListener;
    }

    public HttpListenerScope( API api,)
    {
        httpListener = new Element("listener", HTTP_NAMESPACE.getNamespace());
        httpListener.setAttribute("host", api.getHost());
        httpListener.setAttribute("port", api.getPort());
    }
}

//main = new Element("flow", XMLNS_NAMESPACE.getNamespace());
//
//        main.setAttribute("name", api.getId() + "-" + "main");
//
//        Element httpListener = new Element("listener", HTTP_NAMESPACE.getNamespace());
//        httpListener.setAttribute("config-ref", listenerConfigRef);
//        httpListener.setAttribute("path", api.getPath());
//        main.addContent(httpListener);
//
//        Element restProcessor = new Element("router", APIKitTools.API_KIT_NAMESPACE.getNamespace());
//        if(!StringUtils.isEmpty(configRef)) {
//        restProcessor.setAttribute("config-ref", configRef);
//        }
//        main.addContent(restProcessor);
//
//        Element exceptionStrategy = new Element("exception-strategy", XMLNS_NAMESPACE.getNamespace());
//        exceptionStrategy.setAttribute("ref", exceptionStrategyRef);
//
//        main.addContent(exceptionStrategy);
//
//        mule.addContent(main);