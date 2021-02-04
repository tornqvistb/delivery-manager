package se.lanteam.model;

import java.util.ArrayList;
import java.util.List;

public class PickedOrderLine {
	String lineId;
	String articleId;
	int amount;
	List<String> serialNumbers = new ArrayList<String>();
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public List<String> getSerialNumbers() {
		return serialNumbers;
	}
	public void setSerialNumbers(List<String> serialNumbers) {
		this.serialNumbers = serialNumbers;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	@Override
	public String toString() {
		return "PickedOrderLine [lineId=" + lineId + ", articleId=" + articleId + ", amount=" + amount
				+ ", serialNumbers=" + serialNumbers + "]";
	}
	
}
