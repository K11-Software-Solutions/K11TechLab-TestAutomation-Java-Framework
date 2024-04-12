package com.k11.automation.webservices.baseclasses.soapserviceutil;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.*;

import javax.xml.soap.*;

import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;


import org.apache.log4j.Logger;


public class CommonServiceUtil {

    private static final Logger logger = Logger.getLogger(CommonServiceUtil.class);
    /**
     * Method for calling soap webservice with endpoint & soapmessage and returning responseString
     * @param soapEndpointUrl
     * @param message
     * @return
     * @throws Exception
     */
    public String callSoapWebService(String soapEndpointUrl, SOAPMessage message) throws Exception{
        String response=null;
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(message, soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            //soapResponse.writeTo(System.out);
            response= generateResponseXml(soapResponse);
            soapConnection.close();
        } catch (Exception e) {
            String errormessage = "\nError occurred while sending SOAP Request to Server!\n Make sure you have the correct endpoint URL and SOAPAction!\n";
            logger.error(errormessage + e.getMessage());
            throw new Exception(e);
        }
        return response;
    }
    /**
     * Method to convert the xmlString into Document
     * @param xmlString
     * @return Document
     */
    private Document parseXmlFile(String xmlString) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            String message = "\nError occurred while parsing XML file!\n";
            logger.error(message + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    /**
     * Method to convert SOAPResponse Message object into response string
     * @param soapResponse
     * @return response string
     */
    private String generateResponseXml(SOAPMessage soapResponse) {
        try {
            Source source = soapResponse.getSOAPPart().getContent();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, new StreamResult(System.out));
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *  Method for getting integer number from Node Text
     * @param xmlString
     * @param nodeName
     * @return
     */
    public int getNodeTextContentByNodeName(String xmlString, String nodeName) {
        int nodeValue=0;
        List<String> listNodeText= getNodeTextContentByNodeName(xmlString, nodeName,null);
        if(listNodeText!=null&&listNodeText.size()>0) {
            nodeValue= Integer.parseInt(listNodeText.get(0));
        }
        return nodeValue;
    }

    /**Method to parse xmlstring into document then based on nodename and childnode name returning the text content
     *
     * @param xmlString
     * @param nodeName
     * @param childNodeName
     * @return
     */


    public List<String> getNodeTextContentByNodeName(String xmlString, String nodeName, String childNodeName) {
        List<String>  list =  new ArrayList<String>();
        Document document = parseXmlFile(xmlString);
        NodeList nodeLst = document.getElementsByTagName("*");
        for (int i = 0; i < nodeLst.getLength(); i++) {
            Node n = nodeLst.item(i);
            if (n.getNodeName() != null&& n.getNodeName().contains(nodeName)) {
                if (n.hasChildNodes()&&childNodeName!=null) {
                    NodeList nnm = n.getChildNodes();
                    if (nnm != null) {
                        for (int j = 0; j < nnm.getLength(); j++) {
                            Node node = nnm.item(j);
                            if (node.getNodeName() != null & node.getNodeName().contains(childNodeName)) {
                                list.add(node.getTextContent());
                            }
                        }
                        return list;
                    }
                }
                list.add(n.getTextContent());
                return list;
            }
        }
        return null;
    }



    static public void doTrustToCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        return;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        return;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private static class TrustAllHosts implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * Sends SOAP request and saves it in a queue.
     *
     * @param request SOAP Message request object
     * @return SOAP Message response object
     */
    public String sendSoapRequest(String endpointUrl, SOAPMessage request) throws Exception{
        String response=null;
        try {
            final boolean isHttps = endpointUrl.toLowerCase().startsWith("https");
            HttpsURLConnection httpsConnection = null;
            // Open HTTPS connection
            if (isHttps) {
                // Create SSL context and trust all certificates
                doTrustToCertificates();
                // Open HTTPS connection
                URL url = new URL(endpointUrl);
                httpsConnection = (HttpsURLConnection) url.openConnection();
                // Trust all hosts
                httpsConnection.setHostnameVerifier(new TrustAllHosts());
                // Connect
                httpsConnection.connect();
            }
            // Send HTTP SOAP request and get response
            response= callSoapWebService(endpointUrl, request);
            if (isHttps) {
                httpsConnection.disconnect();
            }
            return response;
        } catch (SOAPException | IOException
                 | NoSuchAlgorithmException | KeyManagementException ex) {
            throw new Exception(ex);
        }
    }

}

