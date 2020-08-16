package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardsOutput {
	
	private Integer customerId;
	private String purchaseMonth;
	private Integer rewardsPoints;

	public RewardsOutput() {
		
	}
	
	public RewardsOutput(Integer customerId, String purchaseMonth, Integer rewardsPoints) {
		this.customerId = customerId;
		this.purchaseMonth = purchaseMonth;
		this.rewardsPoints = rewardsPoints;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	public String getPurchaseMonth() {
		return purchaseMonth;
	}
	
	public void setPurchaseMonth(String purchaseMonth) {
		this.purchaseMonth = purchaseMonth;
	}
	
	public Integer getRewardsPoints() {
		return rewardsPoints;
	}
	
	public void setRewardsPoints(Integer rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}
	
	@Override
	public String toString() {
		return "RewardsOutput{" +
				"customerId=" + customerId +
				", purchaseMonth=" + purchaseMonth +
				", rewardsPoints=" + rewardsPoints + 
				'}';
	}

}
