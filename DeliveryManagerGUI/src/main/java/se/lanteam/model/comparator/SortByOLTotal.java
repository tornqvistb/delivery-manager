package se.lanteam.model.comparator;

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class SortByOLTotal implements Comparator<DeliveryReportLine> 
{ 
    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
        return a.getOlTotal() - b.getOlTotal(); 
    } 
}