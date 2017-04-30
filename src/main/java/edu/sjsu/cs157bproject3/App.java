/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.cs157bproject3;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TemporalType;
import static javax.persistence.TemporalType.DATE;
import javax.persistence.TypedQuery;
import org.hibernate.Hibernate;
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
    
    //  Ask for user input.
    Scanner input = new Scanner(System.in);

    //  Print the query results.
    public static void printQueryResults(Query query){
            List<?> list = query.list();
            SalesTransactions result;
            System.out.println("\nResults:\n");
            for(int i = 0; i < list.size(); i++){
            result = (SalesTransactions)list.get(i);
            System.out.println(result);
            }
            System.out.println("\nEnd of results!\n");
            
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

    //  Look for records of a particular name
    public Query findProduct(Session session){
        
        System.out.println("\nEnter ProductName: ");
        String ProductName = input.nextLine();
        Query query  = session.createQuery("FROM Sales WHERE ProductName = :ProductName");
        query.setParameter("ProductName", ProductName);
        
        return query;
    }
    
    //  Look for records within a time interval
    public Query findProductWTimeInterval(Session session) throws IllegalArgumentException{
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); 
        Date dateX = new Date();
        Date dateY = new Date();
        
        System.out.println("\nPlease enter the first date: ");
        String date0 = input.nextLine();
        try {
            dateX = dateFormat.parse(date0);
        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("\nPlease enter the second date: ");
        String date1 = input.nextLine();
        try {
        dateY = dateFormat.parse(date1);
        } catch (ParseException ex) {
        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        Query query = session.createQuery("SELECT s FROM Sales s WHERE s.date BETWEEN :date0 AND :date1");
        query.setDate("date0", dateX);
        query.setDate("date1", dateY);
        
        return query;
    }
    
    public Query getAll(Session session){
        Query query = session.createQuery("FROM Sales");
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
        //Query query = session.createQuery("FROM Sales WHERE Quantity = 1");
        
        //System.out.println("From the query results function");
        //printQueryResults(query);
        
        //  End testing

        
        //SalesTransactions newOne = app.createNewRecord();     
        //session.save(newOne);
        
        //SalesTransactions something = new SalesTransactions("12/09/19", "Yes",2, 43.23);
        
        
        /*EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("edu.sjsu_CS157BProject3_jar_1.0-SNAPSHOTPU"); 
        EntityManager em = emfactory.createEntityManager(); 
        Query queryx = app.findProductWTimeInterval(session, em);*/
        //session.save(something);
        
        /*Query query2 = app.findProduct(session);
        printQueryResults(query2);

        Query query3 = app.findProductWTimeInterval(session);
        printQueryResults(query3);*/
        

        
        transaction.commit();
        session.close();
        
    }
}
