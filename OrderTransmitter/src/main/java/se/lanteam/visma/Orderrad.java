package se.lanteam.visma;

import java.util.List;

public class Orderrad {
	
	private int Orderrad;
	private String Artikelnummer;
	private List<String> Serienummer;
	public int getOrderrad() {
		return Orderrad;
	}
	public void setOrderrad(int orderrad) {
		Orderrad = orderrad;
	}
	public String getArtikelnummer() {
		return Artikelnummer;
	}
	public void setArtikelnummer(String artikelnummer) {
		Artikelnummer = artikelnummer;
	}
	public List<String> getSerienummer() {
		return Serienummer;
	}
	public void setSerienummer(List<String> serienummer) {
		Serienummer = serienummer;
	}
	
}
