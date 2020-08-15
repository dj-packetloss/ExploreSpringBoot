
package com.example.demo;
  
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
  


import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.ws.util.StringUtils;
  
  
@SpringBootApplication
@RestController
public class DemoApplication {
    
	private static final int POINT_THRESHOLD_100 = 100;
	private static final int POINT_THRESHOLD_50  =  50;
	
	private static final int REWARD_THRESHOLD_100 = 2;
	private static final int REWARD_THRESHOLD_50  = 1;
	  
    public static void main(String[] args) {
    	SpringApplication.run(DemoApplication.class, args);
    	
    	// Stupid eclipse hackery to ensure spring-boot's tomcat dies
    	System.out.println("Press 'Enter' to terminate");
        new Scanner(System.in).nextLine();
        System.out.println("Terminating");
        System.exit(0);
    }
    
    // Keeping the hello world test from spring.io/quickstart  , because why not?
    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
    	return String.format("Hello %s!", name);
    }
      
    /* PROBLEM - 
 	 * A customer receives 2 points for every dollar spent over $100 in each transaction, plus 1 point for every dollar spent over $50 in each transaction
 	 * (e.g. a $120 purchase = 2x$20 + 1x$50 = 90 points).
 	 *
 	 * SOLUTION V1 - Integer inputs only
 	 * check purchase points > 100 ?   Take (purchase points - 100) * 2  + 50  (because if we're over 100, we get the 1x points for 50-100)
 	 * check purchase points > 50  ?   Take (purchase points - 50) * 1         (nix the *1 in math)
 	 * else 0
 	*/
    @GetMapping("/calcRewardsForPurchase")
 	public int calcRewardsForPurchase(@RequestParam("purchaseAmt") int purchaseAmt) {
 		if (purchaseAmt > POINT_THRESHOLD_100) {
 			// EXPLICIT    - (((purchasePoints - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + ((POINT_THRESHOLD_100 - POINT_THRESHOLD_50) * REWARD_THRESHOLD_50)
 			// PERFORMANCE - (((purchasePoints - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + 50)
 			return (((purchaseAmt - POINT_THRESHOLD_100) * REWARD_THRESHOLD_100) + 50);
 		}

 		if (purchaseAmt > POINT_THRESHOLD_50) {
 			// EXPLICIT    - ((purchasePoints - POINT_THRESHOLD_50) * REWARD_THRESHOLD_50)
 			// PERFORMANCE - (purchasePoints - POINT_THRESHOLD_50)
 			return (purchaseAmt - POINT_THRESHOLD_50);
 		}
 		
 		// Not high enough to receive anything
 		return 0;
 	}
      
    /*  
     * Toy Example - All this really does is ensure the Jackson jars are included and our object(s) are set up correctly.
     *  
     * Accept input of JSON, output JSON of 1 record
     * JSON input  {"customerId":"1", "purchaseAmt":"101", "purchaseTime":"2020-08-13"}
     * JSON output {"customerId":"1", "rewardsMonth":"8", "rewardsAmt":"52", "totalRewards:52"}
     * 
     * Example Encoded URL:  http://localhost:8080/calcRewardsForATransaction?json=%7B%22customerId%22%3A%221%22%2C%20%22purchaseAmt%22%3A%22101%22%2C%20%22purchaseTime%22%3A%222020-08-13%22%7D
     */
    // Toy example to verify we can read & post json. 
    @GetMapping("/calcRewardsForATransaction")
    public ResponseEntity<RewardsPoints> calcRewardsForTransaction(@RequestParam("json") String json) {
    	
    	try {
    		System.out.println("JSON=" + json);
    		
    		// This is all or nothing and we'll get a generic error if/when it fails.
	    	RewardsTransaction transaction = new ObjectMapper().readValue(json, RewardsTransaction.class);
	    	
	    	RewardsPoints temp = new RewardsPoints();
	    	temp.setCustomerId(transaction.getCustomerId());
	    	temp.setRewardsMonth(String.valueOf(transaction.getPurchaseTime().getMonthValue()));
	    	temp.setRewardsPoints(calcRewardsForPurchase(transaction.getPurchaseAmt()));
	    	// Because this is a toy example
	    	//temp.setTotal(temp.getRewardsPoints());
	    	
	    	
	    	return new ResponseEntity<RewardsPoints>(temp, new HttpHeaders(), HttpStatus.OK);
    	} catch (Exception e) {
    		// Not handled for toy application when attempting to convert String -> JSON -> RewardsTransaction
    		// JsonMappingException
    		// JsonParseException
    		// IOException
    		System.out.println("Oops!  Encountered an error attempting to read json & calculate rewards.");
    		e.printStackTrace();
    	}
    	
    	// Plug bad request return statement here for compiler.
    	return new ResponseEntity<RewardsPoints>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    
    /* 
     * PROBLEM -
     * Given a record of every transaction during a three month period, calculate the reward points earned for each customer per month and total.
	 *
     *  Solution V1 - Let's post an arbitrarily large json array and have jackson translate it for us because jackson is that neat
     *                
     *                If we were connecting to a db, SQL might look like this  
     *                		"select trs.customerid, trs.month, sum(rewardspoints) from transactions trs where trs.month >= (now() - 3 months)) group by trs.customerid, trs.month;"
     *                      Then perhaps we let front end calc rollup totals.  Or if we're a cool dbms, rollup function(s) already exist o.~
     *
     *                Java 8 introduced some cool new options for collections
     *                Java 8 introduced Streams which allow for some even cooler options for collections
     *                
     *  
     *  JSON input  [ {"customerId":"1", "purchaseAmt":"101", "purchaseTime":"2020-08-13"}    # should be 52 rewards points for customer 1
     *               ,{"customerId":"2", "purchaseAmt":"65", "purchaseTime":"2020-07-13"}     # should be 15 rewards points for customer 2
     *               ,{"customerId":"1", "purchaseAmt":"55", "purchaseTime":"2020-06-15"}     # should be 5 rewards points for customer 1 (Total 57 so far)
     *               ,{"customerId":"1", "purchaseAmt":"275", "purchaseTime":"2020-04-01"}    # this should be too old and excluded, else we'll see lots of points for customer 1
     *               ,{"customerId":"3", "purchaseAmt":"15", "purchaseTime":"2020-08-01"}     # should be 0 rewards points for customer 3
     *               ,{"customerId":"1", "purchaseAmt":"51", "purchaseTime":"2020-08-01"}     # should be 1 rewards point for customer 1  (Total 58)  (Total for month 8 = 53)
     *              ]
     *              
     *  JSON output [ {"customerId":"1", "rewardsMonth":"202008", "rewardsAmt":"52"}
     *               ,{"customerId":"1", "rewardsMonth":"202006", "rewardsAmt":"5"}
     *               ,{"customerId":"1", "totalRewards":"57"}
     *               ,{"customerId":"2", "rewardsMonth":"202007", "rewardsAmt":"15"}
     *               ,{"customerId":"2", "totalRewards":"15"}
     *               ,{"customerId":"3", "rewardsMonth":"202008", "rewardsAmt":"0"}                # arguably could be ignored
     *               ,{"customerId":"3", "totalRewards":"0"}
     *              ]
     */
    @PostMapping("/calcBulkRewards")
    public ResponseEntity<RewardsPoints[]> calcBulkRewards(@RequestParam("json") String json) {
    	
    	try {
    		System.out.println("JSON=" + json);
    		
	    	RewardsTransaction[] transactions = new ObjectMapper().readValue(json, RewardsTransaction[].class);

	    	List<RewardsPoints> rewards = new ArrayList<RewardsPoints>(transactions.length);
	    	
	    	// Show off java.time + stream filtering
	    	/*
	    		Arrays.stream(transactions)
	    				.filter(trans -> trans.getPurchaseTime().compareTo(LocalDate.now().minusMonths(3)) >= 0)
	    				.forEach(trans -> rewards.add(setUpRewardsFromTransaction(trans)));
	    	*/			

	    	// TODO
	    	// Sum each month for each customer + print a total
	    	
	    	// Add grouping to the filter we tried above  (customerid, month, [data]) 
	    	Map<Integer, Map<String, List<RewardsTransaction>>> mapOfDoom = Arrays.stream(transactions)
					.filter(trans -> trans.getPurchaseTime().compareTo(LocalDate.now().minusMonths(3)) >= 0)
					.collect(Collectors.groupingBy(RewardsTransaction::getCustomerId
							,Collectors.groupingBy(RewardsTransaction::getPurchaseMonth))
					        )
					        ;
	    	
	    	// TODO return a RewardsPoints[] and let jackson take care of the json mapping
	    	return new ResponseEntity<RewardsPoints[]>(null, new HttpHeaders(), HttpStatus.OK);
    	} catch (Exception e) {
    		// Not handled for toy application when attempting to convert String -> JSON -> RewardsTransaction
    		// JsonMappingException
    		// JsonParseException
    		// IOException
    		System.out.println("Oops!  Encountered an error attempting to read json & calculate rewards.");
    		e.printStackTrace();
    	}
    	
    	// Plug bad request return statement here for compiler.
    	return new ResponseEntity<RewardsPoints[]>(null, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    
    // A helper function for use in Stream.forEach
    private RewardsPoints setUpRewardsFromTransaction(RewardsTransaction transaction) {

    	System.out.println(transaction);
    	
    	RewardsPoints temp = new RewardsPoints();
    	temp.setCustomerId(transaction.getCustomerId());
    	temp.setRewardsMonth(String.valueOf(transaction.getPurchaseTime().getYear()) + String.valueOf(transaction.getPurchaseTime().getMonthValue()));
    	temp.setRewardsPoints(calcRewardsForPurchase(transaction.getPurchaseAmt()));
    	
    	return temp;
    }
    
  }
