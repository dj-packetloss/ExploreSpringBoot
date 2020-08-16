package com.example.domain;

import java.util.Comparator;

public class RewardsOutputComparator implements Comparator<RewardsOutput> {
	// Allow Collections.sort() to sort a Collection<RewardsOutput> by customerid, month
	@Override
	public int compare(RewardsOutput o1, RewardsOutput o2) {
		int idCompare = o1.getCustomerId().compareTo(o2.getCustomerId());
		
		return (0 == idCompare ? o1.getPurchaseMonth().compareTo(o2.getPurchaseMonth()) : idCompare);
	}
}
