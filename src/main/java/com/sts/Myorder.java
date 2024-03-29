package com.sts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Myorder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long myOrderId;
	private String orderId;
	private String amount;
	private String receipt;
	private String status;
	private String paymentId;
	
	@ManyToOne
	private User user;

	public Myorder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Myorder(Long myOrderId, String orderId, String amount, String receipt, String status, String paymentId,
			User user) {
		super();
		this.myOrderId = myOrderId;
		this.orderId = orderId;
		this.amount = amount;
		this.receipt = receipt;
		this.status = status;
		this.paymentId = paymentId;
		this.user = user;
	}

	@Override
	public String toString() {
		return "Myorder [myOrderId=" + myOrderId + ", orderId=" + orderId + ", amount=" + amount + ", receipt="
				+ receipt + ", status=" + status + ", paymentId=" + paymentId + ", user=" + user + "]";
	}

	public Long getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(Long myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
