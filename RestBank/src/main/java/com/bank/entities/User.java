package com.bank.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private double balance;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@OrderBy(value = "id asc")
	@JoinTable(name = "user_transaction",
	          joinColumns = @JoinColumn(name = "user_id"),
	          inverseJoinColumns = @JoinColumn(name = "transaction_id"))
	private Set <Transaction> transactions = new HashSet<>();

	public User(String name, double balance, Set<Transaction> transactions) {
		super();
		this.name = name;
		this.balance = balance;
		this.transactions = transactions;
	}
	
	public void addTransaction(Transaction transaction) {
		this.transactions.add(transaction);
		transaction.getUsers().add(this);
	}
	
	public void removeAllTransactions() {
		this.transactions.clear();
	}
	
}
