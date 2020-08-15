package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardsTransaction {

	private Integer customerId;
	private Integer purchaseAmt;
	private LocalDate purchaseTime;
	private String purchaseMonth;

	public RewardsTransaction() {
		// meh?
	}
	
	public RewardsTransaction(Integer customerId, Integer purchaseAmt, LocalDate purchaseTime) {
		this.customerId = customerId;
		this.purchaseAmt = purchaseAmt;
		this.purchaseTime = purchaseTime;
		this.purchaseMonth = String.valueOf(purchaseTime.getYear()) + String.valueOf(purchaseTime.getMonthValue());
		
	}

	public Integer getCustomerId() {
		return customerId;
    }

    public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getPurchaseAmt() {
		return this.purchaseAmt;
	}
	
	public void setPurchaseAmt(Integer purchaseAmt) {
		this.purchaseAmt = purchaseAmt;
	}
	
	public LocalDate getPurchaseTime() {
		return this.purchaseTime;
	}

	public void setPurchaseTime(LocalDate purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	
	public void setPurchaseTime(String purchaseTime) {
		try {
			this.purchaseTime = LocalDate.parse(purchaseTime);
			
			this.purchaseMonth = String.valueOf(this.purchaseTime.getYear()) + String.valueOf(this.purchaseTime.getMonthValue());
		} catch (DateTimeParseException dpe) {
			this.purchaseTime = null;
		}
	}
	
	public String getPurchaseMonth() {
		return this.purchaseMonth;
	}
	
	@Override
	public String toString() {
		return "RewardsTransaction{" +
				"customerId=" + customerId +
				", purchaseAmt=" + purchaseAmt +
				", purchaseTime='" + purchaseTime.toString() + "'" +
				'}';
	}
}