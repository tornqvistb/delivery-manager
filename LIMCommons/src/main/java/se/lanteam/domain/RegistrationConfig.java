package se.lanteam.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class RegistrationConfig {

	private Long id;
	private CustomerGroup customerGroup;
	private Boolean useAttribute1;
	private String labelAttribute1;
	private Boolean mandatoryAttribute1;
	private Boolean useAttribute2;
	private String labelAttribute2;
	private Boolean mandatoryAttribute2;
	private Boolean useAttribute3;
	private String labelAttribute3;
	private Boolean mandatoryAttribute3;
	private Boolean useAttribute4;
	private String labelAttribute4;
	private Boolean mandatoryAttribute4;
	private Boolean useAttribute5;
	private String labelAttribute5;
	private Boolean mandatoryAttribute5;
	private Boolean useAttribute6;
	private String labelAttribute6;
	private Boolean mandatoryAttribute6;
	private Boolean useAttribute7;
	private String labelAttribute7;
	private Boolean mandatoryAttribute7;
	private Boolean useAttribute8;
	private String labelAttribute8;
	private Boolean mandatoryAttribute8;
	

	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne
	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}
	public Boolean getUseAttribute1() {
		return useAttribute1;
	}
	public void setUseAttribute1(Boolean useAttribute1) {
		this.useAttribute1 = useAttribute1;
	}
	public String getLabelAttribute1() {
		return labelAttribute1;
	}
	public void setLabelAttribute1(String labelAttribute1) {
		this.labelAttribute1 = labelAttribute1;
	}
	public Boolean getUseAttribute2() {
		return useAttribute2;
	}
	public void setUseAttribute2(Boolean useAttribute2) {
		this.useAttribute2 = useAttribute2;
	}
	public String getLabelAttribute2() {
		return labelAttribute2;
	}
	public void setLabelAttribute2(String labelAttribute2) {
		this.labelAttribute2 = labelAttribute2;
	}
	public Boolean getUseAttribute3() {
		return useAttribute3;
	}
	public void setUseAttribute3(Boolean useAttribute3) {
		this.useAttribute3 = useAttribute3;
	}
	public String getLabelAttribute3() {
		return labelAttribute3;
	}
	public void setLabelAttribute3(String labelAttribute3) {
		this.labelAttribute3 = labelAttribute3;
	}
	public Boolean getUseAttribute4() {
		return useAttribute4;
	}
	public void setUseAttribute4(Boolean useAttribute4) {
		this.useAttribute4 = useAttribute4;
	}
	public String getLabelAttribute4() {
		return labelAttribute4;
	}
	public void setLabelAttribute4(String labelAttribute4) {
		this.labelAttribute4 = labelAttribute4;
	}
	public Boolean getUseAttribute5() {
		return useAttribute5;
	}
	public void setUseAttribute5(Boolean useAttribute5) {
		this.useAttribute5 = useAttribute5;
	}
	public String getLabelAttribute5() {
		return labelAttribute5;
	}
	public void setLabelAttribute5(String labelAttribute5) {
		this.labelAttribute5 = labelAttribute5;
	}
	public Boolean getUseAttribute6() {
		return useAttribute6;
	}
	public void setUseAttribute6(Boolean useAttribute6) {
		this.useAttribute6 = useAttribute6;
	}
	public String getLabelAttribute6() {
		return labelAttribute6;
	}
	public void setLabelAttribute6(String labelAttribute6) {
		this.labelAttribute6 = labelAttribute6;
	}
	public Boolean getUseAttribute7() {
		return useAttribute7;
	}
	public void setUseAttribute7(Boolean useAttribute7) {
		this.useAttribute7 = useAttribute7;
	}
	public String getLabelAttribute7() {
		return labelAttribute7;
	}
	public void setLabelAttribute7(String labelAttribute7) {
		this.labelAttribute7 = labelAttribute7;
	}
	public Boolean getUseAttribute8() {
		return useAttribute8;
	}
	public void setUseAttribute8(Boolean useAttribute8) {
		this.useAttribute8 = useAttribute8;
	}
	public String getLabelAttribute8() {
		return labelAttribute8;
	}
	public void setLabelAttribute8(String labelAttribute8) {
		this.labelAttribute8 = labelAttribute8;
	}
	public Boolean getMandatoryAttribute1() {
		return mandatoryAttribute1;
	}
	public void setMandatoryAttribute1(Boolean mandatoryAttribute1) {
		this.mandatoryAttribute1 = mandatoryAttribute1;
	}
	public Boolean getMandatoryAttribute2() {
		return mandatoryAttribute2;
	}
	public void setMandatoryAttribute2(Boolean mandatoryAttribute2) {
		this.mandatoryAttribute2 = mandatoryAttribute2;
	}
	public Boolean getMandatoryAttribute3() {
		return mandatoryAttribute3;
	}
	public void setMandatoryAttribute3(Boolean mandatoryAttribute3) {
		this.mandatoryAttribute3 = mandatoryAttribute3;
	}
	public Boolean getMandatoryAttribute4() {
		return mandatoryAttribute4;
	}
	public void setMandatoryAttribute4(Boolean mandatoryAttribute4) {
		this.mandatoryAttribute4 = mandatoryAttribute4;
	}
	public Boolean getMandatoryAttribute5() {
		return mandatoryAttribute5;
	}
	public void setMandatoryAttribute5(Boolean mandatoryAttribute5) {
		this.mandatoryAttribute5 = mandatoryAttribute5;
	}
	public Boolean getMandatoryAttribute6() {
		return mandatoryAttribute6;
	}
	public void setMandatoryAttribute6(Boolean mandatoryAttribute6) {
		this.mandatoryAttribute6 = mandatoryAttribute6;
	}
	public Boolean getMandatoryAttribute7() {
		return mandatoryAttribute7;
	}
	public void setMandatoryAttribute7(Boolean mandatoryAttribute7) {
		this.mandatoryAttribute7 = mandatoryAttribute7;
	}
	public Boolean getMandatoryAttribute8() {
		return mandatoryAttribute8;
	}
	public void setMandatoryAttribute8(Boolean mandatoryAttribute8) {
		this.mandatoryAttribute8 = mandatoryAttribute8;
	}

}
