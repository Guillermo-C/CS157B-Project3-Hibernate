/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.cs157bproject3;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 *
 * @author ninjamemo
 */
public class App {
    
    Scanner input = new Scanner(System.in);

    //  Print the query results.
    public static void printQueryResults(Query query){
            List<?> list = query.list();
            SalesTransactions result;

            for(int i = 0; i < list.size(); i++){
            result = (SalesTransactions)list.get(i);
            System.out.println(result);
            }
            
    }
    
    //  Create new record entering values manually.
    public SalesTransactions createNewRecord(){
        
        SalesTransactions sale;
        String manualDate, ProductName;
        int quantity; 
        double unit_cost, total_cost; 
        
        System.out.println("\nPlease enter date in yyyy/MM/dd format: ");
        manualDate = input.nextLine();
        
        System.out.println("\nPlease enter the product name: ");
        ProductName = input.nextLine();
        
        System.out.println("\nPlease enter quantity: ");
        quantity = input.nextInt();
        
        System.out.println("\nPlease enter unit cost: ");
        unit_cost = input.nextDouble();
        
        total_cost = quantity * unit_cost;
        System.out.printf("\nTotal_cost will be $%.2f", total_cost);
        
        return sale = new SalesTransactions(manualDate, ProductName, quantity, unit_cost);
    }

    public Query findProduct(Session session){
        System.out.println("\nEnter ProductName: ");
        String ProductName = input.nextLine();
        Query query  = session.createQuery("FROM Sales WHERE ProductName = :ProductName");
        query.setParameter("ProductName", ProductName);
        
        return query;
    }
    
    public static void main(String[] args){
        
        App app = new App();       
        
        Configuration con = new Configuration().configure().addAnnotatedClass(SalesTransactions.class);
        
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        
        SessionFactory sf = con.buildSessionFactory(reg);
        
        Session session = sf.openSession();
        
        Transaction transaction = session.beginTransaction();
        
        //  Query for testing
        Query query = session.createQuery("FROM Sales WHERE Quantity = 1");
        
        //System.out.println("From the query results function");
        //printQueryResults(query);
        
        //  End testing

        
        //SalesTransactions newOne = app.createNewRecord();     
        //session.save(newOne);
        
        //SalesTransactions something = new SalesTransactions("12/09/11", "something",2, 43.23);
        //session.save(something);
        
        Query query2 = app.findProduct(session);
        printQueryResults(query2);
        
        transaction.commit();
        session.close();
        
    }
}
