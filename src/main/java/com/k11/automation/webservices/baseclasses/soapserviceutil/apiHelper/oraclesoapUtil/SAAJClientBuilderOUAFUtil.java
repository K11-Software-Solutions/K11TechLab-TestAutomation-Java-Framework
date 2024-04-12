package com.k11.automation.webservices.baseclasses.soapserviceutil.apiHelper.oraclesoapUtil;

import com.k11.automation.coreframework.util.LoadProperties;

import javax.xml.soap.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SAAJClientBuilderOUAFUtil {

    // SAAJ - SOAP Client Testing
    public static final String WSS_10_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public final static String WSSE_Security_Elem = "Security";
    public final static String WSSE_Security_prefix = "wsse";
    public final static String WSSE_UsernameToken_Elem = "UsernameToken";
    public final static String WSSE_Username_Elem = "Username";
    public final static String WSSE_Password_Elem = "Password";
    static LoadProperties prop= new LoadProperties();
    public final static String username = prop.getProperty("uat.rmb.username");
    public final static String password = prop.getProperty("uat.rmb.password");

    public final static String namespace = "cm";
   // public final static String namespaceURI = ;


    public static SOAPEnvelope createSoapEnvelope(SOAPMessage soapMessage, String namespaceURI) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);

        // then grab for the SOAP header, and...
        SOAPHeader soapHeader = envelope.getHeader();

        // ... add the WS-Security Header Element
        Name headerElementName = envelope.createName(WSSE_Security_Elem,
                WSSE_Security_prefix, WSS_10_NAMESPACE);
        SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(headerElementName);

        // otherwise a RST without appliesTo fails
        soapHeaderElement.setMustUnderstand(true);

        // add the usernameToken to "Security" soapHeaderElement
        SOAPElement usernameTokenSOAPElement = soapHeaderElement
                .addChildElement(WSSE_UsernameToken_Elem,WSSE_Security_prefix );

        // add the username to usernameToken
        SOAPElement userNameSOAPElement = usernameTokenSOAPElement
                .addChildElement(WSSE_Username_Elem, WSSE_Security_prefix);
        userNameSOAPElement.addTextNode(username);

        // add the password to usernameToken
        SOAPElement passwordSOAPElement = usernameTokenSOAPElement
                .addChildElement(WSSE_Password_Elem, WSSE_Security_prefix);

        passwordSOAPElement.addTextNode(password);
        return envelope;
    }



    public static SOAPMessage readSoapMessage(String filename){
        try {
            SOAPMessage message = MessageFactory.newInstance().createMessage();
            SOAPPart soapPart = message.getSOAPPart();
            soapPart.setContent(new StreamSource(new FileInputStream(filename)));
            message.saveChanges();
          //  createSoapEnvelope(message);
            message.writeTo(System.out);
            return message;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void callSoapWebService(String soapEndpointUrl, String responseFilePath) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();

            System.out.println("Request SOAP Message:");
            soapMessage.writeTo(System.out);
            System.out.println("\n");

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(soapMessage, soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            generateResponseXml(soapResponse, responseFilePath);
            System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }


    private static void generateResponseXml(SOAPMessage soapResponse, String responseFilePath){
        try {
            Source source = soapResponse.getSOAPPart().getContent();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, new StreamResult(System.out));
            transformer.transform(source,
                    new StreamResult(new FileOutputStream(responseFilePath)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  static void callSoapWebService(String soapEndpointUrl, SOAPMessage message, String responseFilePath) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(message, soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            generateResponseXml(soapResponse, responseFilePath);
            System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("\nError occurred while sending SOAP Request to Server!\n Make sure you have the correct endpoint URL and SOAPAction!\n");

        }
    }


}
