package se.lanteam.visma;

import java.util.List;

public class Orderrad {
	
	private int Orderrad;
	private int Kundradnummer;
	private String Artikelnummer;
	private List<String> Serienummer;
	private String Leasingnummer;
	public String getLeasingnummer() {
		return Leasingnummer;
	}
	public void setLeasingnummer(String leasingnummer) {
		Leasingnummer = leasingnummer;
	}
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
	public int getKundradnummer() {
		return Kundradnummer;
	}
	public void setKundradnummer(int kundradnummer) {
		Kundradnummer = kundradnummer;
	}
	
}
