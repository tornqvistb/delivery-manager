package se.lanteam.model.comparator;

import java.util.Comparator;

import se.lanteam.model.DeliveryReportLine;

public class SortByEQStealingTag implements Comparator<DeliveryReportLine> 
{ 
    public int compare(DeliveryReportLine a, DeliveryReportLine b) 
    { 
    	return a.getEqStealingTag().compareTo(b.getEqStealingTag()); 
    } 
}