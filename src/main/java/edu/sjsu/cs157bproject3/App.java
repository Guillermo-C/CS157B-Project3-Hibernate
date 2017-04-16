/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.cs157bproject3;

;
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
    
    
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    //  Print the query results.
    public static void printQueryResults(Query query){
            List<?> list = query.list();
            SalesTransactions result;

            for(int i = 0; i < list.size(); i++){
            result = (SalesTransactions)list.get(i);
            System.out.println(result);
            }
            
    }
    
    public SalesTransactions createNewRecord(){
        
        SalesTransactions sale = new SalesTransactions(); 
        Date manualDate; 
        String ProductName;
        int quantity; 
        double unit_cost;
        double total_cost; 
        
        System.out.println("\nPlease enter date in yyyy/MM/dd format: ");
        try {
            //salesTrans.setDate(calendar.ge);
            manualDate = dateFormat.parse(input.nextLine());
            sale.setDate(manualDate);
            
        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        //sale.setDate(manualDate);
        
        System.out.println("\nPlease enter the product name: ");
        ProductName = input.nextLine();
        sale.setProductName(ProductName);
        
        System.out.println("\nPlease enter quantity: ");
        quantity = input.nextInt();
        sale.setQuantity(quantity);
        
        System.out.println("\nPlease enter unit cost: ");
        unit_cost = input.nextDouble();
        sale.setUnitCost(unit_cost);
        
        total_cost = quantity * unit_cost;
        System.out.printf("\nTotal_cost will be $%.2f", total_cost);
        sale.setTotalCost(total_cost);
        
        return sale;
    }

    
    public static void main(String[] args){
        
        App app = new App();
                
                
        Date date = new Date();
        Scanner input = new Scanner(System.in);
        
        
        SalesTransactions salesTrans = new SalesTransactions();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        
        try {
            //salesTrans.setDate(calendar.ge);
            date = dateFormat.parse("2017/11/15");
        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        salesTrans.setDate(date);
        salesTrans.setProductName("Something");
        salesTrans.setQuantity(1);
        salesTrans.setUnitCost(23.50);
        salesTrans.setTotalCost(89.33);
        
        Configuration con = new Configuration().configure().addAnnotatedClass(SalesTransactions.class);
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        
        
        SessionFactory sf = con.buildSessionFactory(reg);
        
        Session session = sf.openSession();

        
        Transaction transaction = session.beginTransaction();
        
        //  For testing
        Query query = session.createQuery("FROM Sales WHERE Quantity = 1");
        
        
        List<?> list = query.list();

        System.out.println("\nI guess after using toString would be: \n");
        System.out.print(salesTrans);
        //  End testing
        
        /*for(int i = 0; i < list.size(); i++){
            result = (SalesTransactions)list.get(i);
            System.out.println(result);
        }*/
        
        //System.out.println("From the query results function");
        //printQueryResults(query);
        
     
        //session.save(salesTrans);
        
        SalesTransactions newOne = app.createNewRecord();
        
        
        session.save(newOne);
        
        transaction.commit();
        session.close();
        
    }
}
