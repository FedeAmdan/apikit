package org.mule.tools.apikit.output.scopes;

import static org.mule.tools.apikit.output.MuleConfigGenerator.HTTP_NAMESPACE;

import org.mule.tools.apikit.model.API;
import org.mule.tools.apikit.model.APIKitConfig;
import org.mule.tools.apikit.model.HttpListenerConfig;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Element;

public class HttpListenerScope implements Scope
{
//    private final Element httpListener;

    private final Element mule;
    private final Element httpListenerConfig;

    public HttpListenerScope(API api, HttpListenerConfig config, Element mule)
    {
        this.mule = mule;
        //this.httpListenerConfig = config;
        if (config != null)
        {
            httpListenerConfig = new Element(HttpListenerConfig.ELEMENT_NAME, HTTP_NAMESPACE.getNamespace());

            //httpListener = new Element("listener", HTTP_NAMESPACE.getNamespace());
            if(!StringUtils.isEmpty(config.getName())) {
                httpListenerConfig.setAttribute(APIKitConfig.NAME_ATTRIBUTE, config.getName());
            }
            httpListenerConfig.setAttribute("host", api.getHost());
            httpListenerConfig.setAttribute("port", api.getPort());
            mule.addContent(httpListenerConfig);
        }
        else
            httpListenerConfig = null;
    }

    @Override
    public Element generate()
    {
        //Element config = null;
        //if (httpListenerConfig != null)
        //{
        //    config = new Element(HttpListenerConfig.ELEMENT_NAME, HTTP_NAMESPACE.getNamespace());
        //
        //    //httpListener = new Element("listener", HTTP_NAMESPACE.getNamespace());
        //    config.setAttribute("host", api.getHost());
        //    config.setAttribute("port", api.getPort());
        //
        //}

        return httpListenerConfig;

    }

}


//private final Element mule;
//private final APIKitConfig config;
//
//    public APIKitConfigScope(APIKitConfig config, Element mule) {
//        this.mule = mule;
//        this.config = config;
//    }
//
//    @Override
//    public Element generate() {
//        Element config = null;
//        if(this.config != null) {
//            config = new Element(APIKitConfig.ELEMENT_NAME,
//                                 APIKitTools.API_KIT_NAMESPACE.getNamespace());
//
//            if(!StringUtils.isEmpty(this.config.getName())) {
//                config.setAttribute(APIKitConfig.NAME_ATTRIBUTE, this.config.getName());
//            }
//
//            config.setAttribute(APIKitConfig.RAML_ATTRIBUTE, this.config.getRaml());
//            config.setAttribute(APIKitConfig.CONSOLE_ENABLED_ATTRIBUTE, String.valueOf(this.config.isConsoleEnabled()));
//            config.setAttribute(APIKitConfig.CONSOLE_PATH_ATTRIBUTE, this.config.getConsolePath());
//
//            mule.addContent(config);
//        }
//        return config;
//    }





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