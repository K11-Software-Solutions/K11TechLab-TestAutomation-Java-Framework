package com.k11.automation.webservices.baseclasses.soapserviceutil.apiHelper;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
 
public class JaxWsLoggingHandler implements SOAPHandler<SOAPMessageContext> {
 
    @Override
    public void close(MessageContext arg0) {
    }
 
    @Override
    public boolean handleFault(SOAPMessageContext arg0) {
        SOAPMessage message = arg0.getMessage();
        try {
            message.writeTo(System.out);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
 
    @Override
    public boolean handleMessage(SOAPMessageContext arg0) {
        SOAPMessage message = arg0.getMessage();
        boolean isOutboundMessage = (Boolean) arg0.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (isOutboundMessage) {
            System.out.println("OUTBOUND MESSAGE\n");
 
        } else {
            System.out.println("INBOUND MESSAGE\n");
        }
        try {
            Source source = message.getSOAPPart().getContent();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, new StreamResult(System.out));
            transformer.transform(source,
                    new StreamResult(new FileOutputStream("./testResults/CM_CCAndBillInfo_response.xml")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
 
    @Override
    public Set<QName> getHeaders() {
        return null;
    }
 
}