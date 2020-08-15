package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RewardsPoints {

	private int customerId;
	private String rewardsMonth;
	private int rewardsPoints;
	private int total;

	public RewardsPoints() {
		// meh?
	}

	public int getCustomerId() {
		return customerId;
    }

    public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getRewardsMonth() {
		return this.rewardsMonth;
	}
	
	public void setRewardsMonth(String rewardsMonth) {
		this.rewardsMonth = rewardsMonth;
	}
	
	public int getRewardsPoints() {
		return this.rewardsPoints;
	}

	public void setRewardsPoints(int rewardsPoints) {
		this.rewardsPoints = rewardsPoints;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	@Override
	public String toString() {
		return "RewardsPoints{" +
				"customerId=" + customerId +
				", rewardsMonth=" + rewardsMonth +
				", rewardsPoints=" + rewardsPoints + 
				", total=" + total +
				'}';
	}
}