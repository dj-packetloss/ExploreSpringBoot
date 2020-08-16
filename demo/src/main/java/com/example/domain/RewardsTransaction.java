package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardsTransaction {

	private static final int POINT_THRESHOLD_100 = 100;
	private static final int POINT_THRESHOLD_50  =  50;
	
	private static final int REWARD_THRESHOLD_100 = 2;
	private static final int REWARD_THRESHOLD_50  = 1;
	
	private Integer customerId;
	private Integer purchaseAmt;
	private LocalDate purchaseTime;
	private String purchaseMonth;
	private Integer rewardsPoints;

	public RewardsTransaction() {
		// meh?
	}
	
	public RewardsTransaction(Integer customerId, Integer purchaseAmt, LocalDate purchaseTime) {
		this.customerId = customerId;
		this.setPurchaseAmt(purchaseAmt);
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
		
		this.rewardsPoints = calcRewardsForPurchase(purchaseAmt);
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
	
	// Setter is hidden intentionally as this is derived
	public String getPurchaseMonth() {
		return this.purchaseMonth;
	}
	
	// Setter is hidden intentionally as this is derived
	public Integer getRewardsPoints() {
		return this.rewardsPoints;
	}
	
	public static int calcRewardsForPurchase(int purchaseAmt) {
 		if (purchaseAmt > POINT_THRESHOLD_100) {
 			// EXPLICIT    - (((purchasePoints - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + ((POINT_THRESHOLD_100 - POINT_THRESHOLD_50) * REWARD_THRESHOLD_50)
 			// PERFORMANCE - (((purchasePoints - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + 50)
 			return (((purchaseAmt - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + 50);
 		}

 		if (purchaseAmt > POINT_THRESHOLD_50) {
 			// EXPLICIT    - ((purchasePoints - POINT_THRESHOLD_50) * REWARD_THRESHOLD_50)
 			// PERFORMANCE - (purchasePoints - POINT_THRESHOLD_50)
 			return (purchaseAmt - POINT_THRESHOLD_50);
 		}
 		
 		// Not high enough to receive anything
 		return 0;
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