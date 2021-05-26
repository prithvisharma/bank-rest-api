package com.bank.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	@Column(name = "details")
	private String details;
	@Column(name = "amount")
	private double amount;
	@Column(name = "sender_id")
	private UUID senderId;
	@Column(name = "receiver_id")
	private UUID receiverId;
	
	@ManyToMany(mappedBy = "transactions", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();
	
	public Transaction(String details, double amount, UUID senderId, UUID receiverId) {
		super();
		this.details = details;
		this.amount = amount;
		this.senderId = senderId;
		this.receiverId = receiverId;
	}
	
	public void removeAllUsers(Transaction transaction) {
		for(User u : this.users) {
			u.getTransactions().remove(transaction);
			transaction.getUsers().remove(this);
		}
	}
}

