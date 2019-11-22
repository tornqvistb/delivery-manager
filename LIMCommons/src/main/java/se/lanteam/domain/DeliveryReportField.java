package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

@Entity
public class DeliveryReportField {

	private Long id;
	private ReportsConfig reportsConfig;
	private String fieldName;
	private String label;
	private Boolean showInReport;
	private Long columnNumber;	
	private Long originColumnNumber;
		
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne
	public ReportsConfig getReportsConfig() {
		return reportsConfig;
	}
	public void setReportsConfig(ReportsConfig reportsConfig) {
		this.reportsConfig = reportsConfig;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Boolean getShowInReport() {
		return showInReport;
	}
	public void setShowInReport(Boolean showInReport) {
		this.showInReport = showInReport;
	}
	public Long getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(Long columnNumber) {
		this.columnNumber = columnNumber;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public Long getOriginColumnNumber() {
		return originColumnNumber;
	}
	public void setOriginColumnNumber(Long originColumnNumber) {
		this.originColumnNumber = originColumnNumber;
	}

	@Transient
	public String getStyleClass() {
		String defaultClass = "ui-state-default";
		if (this.getFieldName().startsWith("oh"))
			return defaultClass + " pink-box";
		if (this.getFieldName().startsWith("ol"))
			return defaultClass + " yellow-box";
		if (this.getFieldName().startsWith("eq"))
			return defaultClass + " green-box";
		return defaultClass;
	}

}
