package com.spring.dto;

public class QueryResponse {
	public class PurchasedCount {
	    private int productId;
	    private double percentage;
		public Integer getProductId() {
			return productId;
		}
		public void setProductId(Integer productId) {
			this.productId = productId;
		}
		public double getPercentage() {
			return percentage;
		}
		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}
		

	    // Constructors, getters, and setters

	    // ...
	}
}
