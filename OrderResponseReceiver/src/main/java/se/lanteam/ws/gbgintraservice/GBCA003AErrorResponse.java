//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.26 at 09:25:24 AM CET 
//


package se.lanteam.ws.gbgintraservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GBCA003A_ErrorResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GBCA003A_ErrorResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Line" type="{http://lanteam.se/ws/GbgIntraservice}ArrayOfGBCA003A_ErrorResponseLine"/>
 *         &lt;element name="NumberOfLines" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="YourPurchaseOrder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="YourSalesOrder" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GBCA003A_ErrorResponse", propOrder = {
    "line",
    "numberOfLines",
    "yourPurchaseOrder",
    "yourSalesOrder"
})
public class GBCA003AErrorResponse {

    @XmlElement(name = "Line", required = true, nillable = true)
    protected ArrayOfGBCA003AErrorResponseLine line;
    @XmlElement(name = "NumberOfLines", required = true, nillable = true)
    protected String numberOfLines;
    @XmlElement(name = "YourPurchaseOrder", required = true, nillable = true)
    protected String yourPurchaseOrder;
    @XmlElement(name = "YourSalesOrder", required = true, nillable = true)
    protected String yourSalesOrder;

    /**
     * Gets the value of the line property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfGBCA003AErrorResponseLine }
     *     
     */
    public ArrayOfGBCA003AErrorResponseLine getLine() {
        return line;
    }

    /**
     * Sets the value of the line property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfGBCA003AErrorResponseLine }
     *     
     */
    public void setLine(ArrayOfGBCA003AErrorResponseLine value) {
        this.line = value;
    }

    /**
     * Gets the value of the numberOfLines property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * Sets the value of the numberOfLines property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfLines(String value) {
        this.numberOfLines = value;
    }

    /**
     * Gets the value of the yourPurchaseOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYourPurchaseOrder() {
        return yourPurchaseOrder;
    }

    /**
     * Sets the value of the yourPurchaseOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYourPurchaseOrder(String value) {
        this.yourPurchaseOrder = value;
    }

    /**
     * Gets the value of the yourSalesOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYourSalesOrder() {
        return yourSalesOrder;
    }

    /**
     * Sets the value of the yourSalesOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYourSalesOrder(String value) {
        this.yourSalesOrder = value;
    }

}
