package se.lanteam.visma;

import java.util.List;

public class Order {
	
	private Integer Ordernummer;
	private List<Orderrad> Orderrader;
	public Integer getOrdernummer() {
		return Ordernummer;
	}
	public void setOrdernummer(int ordernummer) {
		Ordernummer = ordernummer;
	}
	public List<Orderrad> getOrderrader() {
		return Orderrader;
	}
	public void setOrderrader(List<Orderrad> orderrader) {
		Orderrader = orderrader;
	}
}
