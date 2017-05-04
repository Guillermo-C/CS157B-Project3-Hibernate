/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.cs157bproject3;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
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
// Something
public class App {
    
    /*  Initialize the menu, the integer is the index and the String
        is the description for that index. 
    */
    public App(){
        Menu.put(0, "Show all records");
        Menu.put(1, "Find product (by ProductName)");
        Menu.put(2, "Find records in a time interval");
        Menu.put(3, "Create a new record of type Sales");
        Menu.put(4, "Find products sold last month");
    }  
    
    /*  Mapping of menu, Integer used for index and String used for menu 
        description of a particular index     
    */
    static LinkedHashMap<Integer,String> Menu = new LinkedHashMap<Integer,String>();
    
    //  Ask for user input.
    static Scanner input = new Scanner(System.in);

    
    //  Methods   
    
    //  Print the query results.
    
    //  Display the menu to the user. 
    public static void presentMenu(Map<Integer, String> map){
        
        Set set = Menu.entrySet();
        Iterator it = set.iterator();

        while(it.hasNext()){
            Map.Entry<Integer, String> menu = (Map.Entry<Integer, String>)it.next();
            int key = menu.getKey();
            String value = menu.getValue();
            System.out.println(key + "\t" + value);
        }
        
    }
    
    public static void printQueryResults(Query query){
            List<?> list = query.list();
            if(list.size() > 0){
                SalesTransactions result;
                System.out.println("\nResults:\n");
                for(int i = 0; i < list.size(); i++){
                result = (SalesTransactions)list.get(i);
                System.out.println(result);
                }
            System.out.println("\nEnd of results!\n");
            }
            else{
                System.out.println("No records found with given criteria.\n");
            }
            
    }
    
    //  Execute the menu based on the user's choice. 
    public static void executeMenu(int choice, Session session){
        Query query;
        switch(choice){
            case 0: query = getAllRecords(session);
                    printQueryResults(query);
                    break;
                    
            case 1: query = findProduct(session);
                    printQueryResults(query);
                    break;
            
            case 2: query = findProductWTimeInterval(session);
                    printQueryResults(query);           
                    break;
                    
            case 3: createNewRecord(session);
                    query = getAllRecords(session);
                    printQueryResults(query);
                    break;
            
            case 4: query = soldLastMonth(session);
                    printQueryResults(query);
                    break;
                    
            default: break;              
        }
    }
            
    //  Create new record entering values manually.
    public static void createNewRecord(Session session){
        
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
        
        sale = new SalesTransactions(manualDate, ProductName, quantity, unit_cost);
        
        session.save(sale);
        //return sale = new SalesTransactions(manualDate, ProductName, quantity, unit_cost);
    }
    
    /*  Display the menu, execute the query of the user's choice and quit if -1
        is entered.
    */
    public static void DoMenu(Map<Integer, String> map, Session session){
        int choiceInt = 0;

        System.out.println("Enter a number from the available options (enter -1 to exit): ");
        while(choiceInt != -1){
            
                presentMenu(map);
                System.out.println("Enter number: ");
                choiceInt = input.nextInt();
                input.nextLine();
                executeMenu(choiceInt, session);  
        
        }
    }
    
    //  End of Methods
    
    
    //  Query Methods
    
    //  Look for records of a particular name
    public static Query findProduct(Session session){
        

        System.out.println("\nEnter ProductName: ");
        String ProductName = input.nextLine();

        Query query  = session.createQuery("FROM Sales WHERE ProductName = :ProductName");
        query.setParameter("ProductName", ProductName);

        return query;
    }
    
    //  Look for records within a time interval
    public static final Query findProductWTimeInterval(Session session) throws IllegalArgumentException{
        
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
    
    //  Query to get all records from this session. 
    public static final Query getAllRecords(Session session){
            
        Query query = session.createQuery("FROM Sales");

    return query;
    
    }
    
    public static Query soldLastMonth(Session session){
        Query query = session.createQuery("SELECT *" +
                                          "FROM Sales" +
                                          "WHERE MONTH(Date_id) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH)");
        return query;
    }
    
    //  End of Query Methods

 
    public static void main(String[] args){

        App app = new App(); 
        
        Configuration con = new Configuration().configure().addAnnotatedClass(SalesTransactions.class);
        
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        
        SessionFactory sf = con.buildSessionFactory(reg);     
        
        Session session = sf.openSession();
        
        Transaction transaction = session.beginTransaction();
        
        DoMenu(Menu, session);
        
        transaction.commit();
        
        session.close();
        
    }
}
