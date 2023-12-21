package com.spring.dto;

import java.util.List;

public class CheckoutDto {
	private double totalPrice;
private List<CheckoutItemDto> checkoutItemList;


public double getTotalPrice() {
	return totalPrice;
}

public void setTotalPrice(double totalPrice) {
	this.totalPrice = totalPrice;
}

public List<CheckoutItemDto> getCheckoutItemList() {
	return checkoutItemList;
}

public void setCheckoutItemList(List<CheckoutItemDto> checkoutItemList) {
	this.checkoutItemList = checkoutItemList;
}

}
