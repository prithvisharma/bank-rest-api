package com.bank.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bank.entities.Transaction;
import com.bank.entities.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;

@RestController
public class MainController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@PostMapping("/api/add-user")
	public String addUser(@RequestBody User apiuser) {
		userRepository.save(apiuser);
		return "User Added";
	}
		
	@PostMapping("/api/process-transaction")
	public String processTransaction(@RequestBody Transaction transaction) {
		long senderId = transaction.getSenderId();
		long receiverId = transaction.getReceiverId();
		double amount = transaction.getAmount();
		
		User sender = userRepository.getById(senderId);
		User receiver = userRepository.getById(receiverId);
				
		if(senderId == receiverId) {
			transactionRepository.save(transaction);
			
			receiver.setBalance(receiver.getBalance()+amount);
			receiver.addTransaction(transaction);
			userRepository.save(receiver);
			
			return "Transaction Success";
		}
		else {
			if(sender.getBalance()>=amount) {
				transactionRepository.save(transaction);
				
				sender.setBalance(sender.getBalance()-amount);
				sender.addTransaction(transaction);
				userRepository.save(sender);
				
				receiver.setBalance(amount+receiver.getBalance());
				receiver.addTransaction(transaction);
				userRepository.save(receiver);
				
				return "Transaction Success";
			}
			else {
				return "Transaction Failed : Insufficient Balance";
			}
		}
	}
	
	@PostMapping("/api/reverse-transaction")
	public String reverseTransaction(@RequestBody long id) {
		Transaction transaction = transactionRepository.getById(id);
		User sender = userRepository.getById(transaction.getSenderId());
		User receiver = userRepository.getById(transaction.getReceiverId());
		
		sender.setBalance(sender.getBalance()+transaction.getAmount());
		userRepository.save(sender);
		
		receiver.setBalance(receiver.getBalance()-transaction.getAmount());
		userRepository.save(receiver);
		
		transaction.removeAllUsers(transaction);
		transactionRepository.delete(transaction);
		
		return "Transaction Reversed";
	}
	
	@GetMapping("/api/transaction/{id}")
	public String displayTransactionById(@PathVariable("id") long id) {
		Transaction transaction = transactionRepository.getById(id);
		return "ID: "+transaction.getId()+"\nDetails: "+transaction.getDetails()
				+"\nSender ID: "+transaction.getSenderId()+"\nReceiver ID: "+transaction.getReceiverId()
				+"\nAmount: "+transaction.getAmount();
	}
	
	@GetMapping("/api/all/transaction")
	public List<String> displayAllTransactions(){
		 List<Transaction> transactions = transactionRepository.findAll();
		 List<String> list = new ArrayList<>();
		 for(Transaction transaction : transactions)
			 list.add("ID: "+transaction.getId()+"  Details: "+transaction.getDetails()
				+"  Sender ID: "+transaction.getSenderId()+"  Receiver ID: "+transaction.getReceiverId()
				+"  Amount: "+transaction.getAmount());
		 return list;
	}
	
	@GetMapping("/api/user-transaction/{id}")
	public List<String> displayTransactionByUser(@PathVariable("id") long id){
		User user = userRepository.getById(id);
		Set<Transaction> transactions = user.getTransactions();
		List<String> list = new ArrayList<>();
		for(Transaction transaction : transactions)
			 list.add("ID: "+transaction.getId()+"  Details: "+transaction.getDetails()
				+"  Sender ID: "+transaction.getSenderId()+"  Receiver ID: "+transaction.getReceiverId()
				+"  Amount: "+transaction.getAmount());
		return list;
	}
}
