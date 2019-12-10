package se.lanteam.model.comparator;

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class SortByEQCustomLabel1 implements Comparator<DeliveryReportLine> 
{ 
    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
        return a.getEqCustomLabel1().compareTo(b.getEqCustomLabel1()); 
    } 
}