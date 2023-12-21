package com.spring.dto;

import org.springframework.beans.factory.annotation.Value;

public class CheckoutItemDto {

    private String productName;
    private int  quantity;
    private double price;
    private int productId;
    @Value("year")
   private String period;
   private int branch;

	public CheckoutItemDto() {}

    public CheckoutItemDto(String productName, int quantity, double price, int productId) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        
    }

   
	public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice(){return price;}

    public int getProductId() {
        return productId;
    }

    public void setProductId(int id) {
        this.productId = id;
    }

	

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getBranch() {
		return branch;
	}

	public void setBranch(int branch) {
		this.branch = branch;
	}


}
