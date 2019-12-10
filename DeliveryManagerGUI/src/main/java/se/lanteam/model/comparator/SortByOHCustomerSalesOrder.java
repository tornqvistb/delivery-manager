package se.lanteam.model.comparator;

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class SortByOHCustomerSalesOrder implements Comparator<DeliveryReportLine> 
{ 
    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
        return a.getOhCustomerSalesOrder().compareTo(b.getOhCustomerSalesOrder()); 
    } 
}