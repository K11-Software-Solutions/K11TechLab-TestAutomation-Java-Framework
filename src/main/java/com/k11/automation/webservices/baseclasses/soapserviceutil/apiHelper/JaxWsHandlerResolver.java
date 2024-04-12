package com.k11.automation.webservices.baseclasses.soapserviceutil.apiHelper;

import com.k11.automation.coreframework.util.LoadProperties;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.ArrayList;
import java.util.List;
 
public class JaxWsHandlerResolver implements HandlerResolver {
 
    @SuppressWarnings("rawtypes")
    @Override
    public List<Handler> getHandlerChain(PortInfo arg0) {
        List<Handler> hchain = new ArrayList<Handler>();
        LoadProperties prop= new LoadProperties();
        String username = prop.getProperty("mot1.rmb.username");
        String password = prop.getProperty("mot1.rmb.password");
        hchain.add(new JaxWsLoggingHandler());
        hchain.add(new WSSecurityHeaderSOAPHandler(username, password));
        return hchain;
    }
 
}