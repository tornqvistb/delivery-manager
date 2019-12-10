package se.lanteam.model.comparator;

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class SortByOHCustomerOrderNumber implements Comparator<DeliveryReportLine> 
{ 
    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
        return a.getOhCustomerOrderNumber().compareTo(b.getOhCustomerOrderNumber()); 
    } 
}