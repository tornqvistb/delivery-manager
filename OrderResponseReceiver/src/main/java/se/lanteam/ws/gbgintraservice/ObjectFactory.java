//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.02 at 04:10:15 PM CET 
//


package se.lanteam.ws.gbgintraservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the se.lanteam.ws.gbgintraservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ErrorResponseErrorResponse_QNAME = new QName("http://lanteam.se/ws/GbgIntraservice", "errorResponse");
    private final static QName _ErrorResponseResponseErrorResponseResult_QNAME = new QName("http://lanteam.se/ws/GbgIntraservice", "ErrorResponseResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: se.lanteam.ws.gbgintraservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ErrorResponseResponse }
     * 
     */
    public ErrorResponseResponse createErrorResponseResponse() {
        return new ErrorResponseResponse();
    }

    /**
     * Create an instance of {@link ErrorResponse }
     * 
     */
    public ErrorResponse createErrorResponse() {
        return new ErrorResponse();
    }

    /**
     * Create an instance of {@link GBCA003AErrorResponse }
     * 
     */
    public GBCA003AErrorResponse createGBCA003AErrorResponse() {
        return new GBCA003AErrorResponse();
    }

    /**
     * Create an instance of {@link GBCA003AErrorResponseLine }
     * 
     */
    public GBCA003AErrorResponseLine createGBCA003AErrorResponseLine() {
        return new GBCA003AErrorResponseLine();
    }

    /**
     * Create an instance of {@link ArrayOfGBCA003AErrorResponseLine }
     * 
     */
    public ArrayOfGBCA003AErrorResponseLine createArrayOfGBCA003AErrorResponseLine() {
        return new ArrayOfGBCA003AErrorResponseLine();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GBCA003AErrorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lanteam.se/ws/GbgIntraservice", name = "errorResponse", scope = ErrorResponse.class)
    public JAXBElement<GBCA003AErrorResponse> createErrorResponseErrorResponse(GBCA003AErrorResponse value) {
        return new JAXBElement<GBCA003AErrorResponse>(_ErrorResponseErrorResponse_QNAME, GBCA003AErrorResponse.class, ErrorResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://lanteam.se/ws/GbgIntraservice", name = "ErrorResponseResult", scope = ErrorResponseResponse.class)
    public JAXBElement<String> createErrorResponseResponseErrorResponseResult(String value) {
        return new JAXBElement<String>(_ErrorResponseResponseErrorResponseResult_QNAME, String.class, ErrorResponseResponse.class, value);
    }

}
