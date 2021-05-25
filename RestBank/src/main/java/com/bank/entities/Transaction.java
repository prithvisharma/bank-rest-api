package com.bank.entities;

import java.util.HashSet;
import java.util.Set;

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
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String details;
	private double amount;
	private long senderId;
	private long receiverId;
	
	@ManyToMany(mappedBy = "transactions", fetch = FetchType.LAZY)
	private Set<User> users = new HashSet<>();
	
	public Transaction(String details, double amount, long senderId, long receiverId) {
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

