package se.lanteam.ws;

public class Header {

	private String kod;
	private String text;
	public Header(String kod, String text) {
		this.kod = kod;
		this.text = text;
	}
	public String getKod() {
		return kod;
	}
	public String getText() {
		return text;
	}	
	
}
