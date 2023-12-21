package com.spring.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="orders")
public class Order implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	@Column
	private Date createdDate;

	@Column
	private Double totalPrice;

	@Column
	private String sessionId;
	@Column
	private String paymentId;





	public Refund getRefund() {
		return refund;
	}


	public void setRefund(Refund refund) {
		this.refund = refund;
	}


	public String getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}


	public List<OrderItem> getOrderitems() {
		return orderitems;
	}


	public void setOrderitems(List<OrderItem> orderitems) {
		this.orderitems = orderitems;
	}


	@OneToMany(fetch = FetchType.EAGER,mappedBy="orders",cascade = CascadeType.ALL) 
	private List<OrderItem> orderitems;

	@OneToOne(fetch = FetchType.EAGER,mappedBy="orders",cascade = CascadeType.ALL) 
	private Refund refund;
	@Column
	private int userId;
	@Column
	private boolean isActive;

	public boolean isActive() {
		return isActive;
	}


	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public Order() {
	}


	public List<OrderItem> getOrderItems() {
		return orderitems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderitems = orderItems;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


}
