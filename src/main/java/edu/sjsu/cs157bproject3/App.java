/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sjsu.cs157bproject3;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.InputMismatchException;
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
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 *
 * @author ninjamemo
 */


public class App { 
    
    
    //  Declaration of Variables
    
    
    /*  Mapping of menu. Integer used for index and String used for menu 
    description of a particular index. */
    static LinkedHashMap<Integer,String> Menu = new LinkedHashMap<Integer,String>();
    
    //  Initialize scanner to ask for the user's input.
    static Scanner input = new Scanner(System.in);
    
    /*  Initialize the menu. The integer is the index and the String
        is the description for that index. */
    public App(){
        Menu.put(0, "Show all records");
        Menu.put(1, "Find product (by ProductName)");
        Menu.put(2, "Find records in a time interval");
        Menu.put(3, "Create a new record of type Sales");
        Menu.put(4, "Find products sold last month");
        Menu.put(5, "Find the product with most sales");
        Menu.put(6, "Transaction with the most units sold");
        Menu.put(7, "Total number of units sold");
        Menu.put(8, "Transaction(s) with highest TotalCost");
        Menu.put(9, "Sale(s) with smallest TotalCost");
        Menu.put(10, "Show all sales ordered by Month");
        Menu.put(11, "Show all sales ordered by Year");
        Menu.put(12, "Find the transactions with single-unit sales");
        
    }  
    
    
    //  End of Declaration of Variables
    
    
    
    //  Methods   
    
    
    //  Display the menu to the user. 
    public static void presentMenu(Map<Integer, String> map){
        
        Set set = Menu.entrySet();
        Iterator it = set.iterator();

        while(it.hasNext()){
            Map.Entry<Integer, String> menu = (Map.Entry<Integer, String>)it.next();
            int key = menu.getKey();
            String value = menu.getValue();
            System.out.println(key + "\t - " + value);
        }
        
    }
    
    //  Print the query results.
    public static void printQueryResults(Query query){
            List<?> list = query.list();
            if(list.size() > 1){
                SalesTransactions result;
                System.out.println("\nResults:\n");
                for(int i = 0; i < list.size(); i++){
                result = (SalesTransactions)list.get(i);
                System.out.println(result);
                }
                System.out.println("\nEnd of results!\n");
            }
            else if(list.size() == 1){
                System.out.println("\nResults:\n"+ list.toString() +"\nEnd of results!\n");
            }
            else{
                System.out.println("No records found with the given criteria.\n");
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
                    break;            
            case 4: query = soldLastMonth(session);
                    printQueryResults(query);
                    break;
            case 5: query = productWMostSales(session);
                    printQueryResults(query);
                    break;
            case 6: query = transWMostUnitsSold(session);
                    printQueryResults(query);
                    break;
            case 7: query = totalNumberOfUnitsSold(session);
                    printQueryResults(query);
                    break;
            case 8: query = highestTotalCost(session);
                    printQueryResults(query);
                    break;
            case 9: query = saleWLeastTotalCost(session);
                    printQueryResults(query);
                    break;
            case 10: query = salesOrderedByMonth(session);
                    printQueryResults(query);
                    break;
            case 11: query = salesOrderedByYear(session);
                    printQueryResults(query);  
                    break;
            case 12: query = transWLeastUnitsSold(session);
                     printQueryResults(query); 
                    break;
            default: break;              
        }
    }
            
    //  Create new record entering values manually.
    public static void createNewRecord(Session session){
        
        SalesTransactions sale = null;
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
        System.out.println();
        
        sale = new SalesTransactions(manualDate, ProductName, quantity, unit_cost);
        
        try{         
            Transaction trans = session.beginTransaction();
            session.save(sale);
            
            if(!session.getTransaction().wasCommitted()){
                session.getTransaction().commit();
            }
            
            System.out.println("\nNew record was saved!\n");
        }
        catch(ConstraintViolationException e){
            System.out.println("\nPrimary key (date) is already being used!.\n");
        }


        
    }
    
    /*  Display the menu, execute the query of the user's choice and quit if -1
        is entered. */
    public static void DoMenu(Map<Integer, String> map, Session session){
        int choiceInt = 0;

        System.out.println("Enter a number from the available options (enter -1 to exit): ");
        while(choiceInt != -1){

                presentMenu(map);
                try{
                    System.out.println("Enter number: ");
                    choiceInt = input.nextInt();
                    input.nextLine();
                    executeMenu(choiceInt, session); 
                }
                catch(InputMismatchException e){
                    System.out.println("Check your input! Make sure you enter an integer available in the menu.\n");
                    input.nextLine();
                }
                //input.nextLine();
        
        }
    }
    
    
    //  End of Methods
    
    
    
    //  Query Methods
    
    //  Look for records of a particular name.
    public static Query findProduct(Session session){
        
        System.out.println("\nEnter ProductName: ");
        String ProductName = input.nextLine();
        //input.nextLine();

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
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        LocalDate currentMonth = LocalDate.now();
        Date lastMonthAsDate = java.sql.Date.valueOf(lastMonth);
        Date currentMonthAsDate = java.sql.Date.valueOf(currentMonth);
        System.out.println("\nCurrenty looking with:" + lastMonthAsDate.toString() + "\n");
        System.out.println("\ncurrentMonthAsDate:" + currentMonthAsDate.toString() + "\n");
        Query query = session.createQuery("SELECT s FROM Sales s WHERE Date_id BETWEEN :from AND :to ");
        query.setDate("from", lastMonthAsDate);
        query.setDate("to", currentMonthAsDate);
        
        return query;
    }
    
    public static Query productWMostSales(Session session){
        int number = 1;
        Query query = session.createQuery("SELECT s " + "FROM Sales s WHERE s.Quantity > :number AND s.ProductName IN (SELECT s.ProductName FROM Sales s GROUP BY s.ProductName)");
        query.setParameter("number", number);
        
        return query;
    }
    
    public static Query transWMostUnitsSold(Session session){
        Query query = session.createQuery("SELECT s " +
                                          "FROM Sales s " +
                                          "WHERE s.Quantity = (SELECT MAX(s.Quantity) " +
                                          "FROM Sales s)");

        return query;
    }

    public static Query totalNumberOfUnitsSold(Session session){
        Query query = session.createSQLQuery("SELECT SUM(s.Quantity) FROM Sales s");
        
        return query;
    }
    
    public static Query highestTotalCost(Session session){
        Query query = session.createQuery("SELECT s " +
                                          "FROM Sales s " +
                                          "WHERE s.TotalCost = (SELECT MAX(s.TotalCost) " + 
                                                                "FROM Sales s)");
        return query;
    }
    
    public static Query saleWLeastTotalCost(Session session){
        Query query = session.createQuery("SELECT s " +
                                          "FROM Sales s " + 
                                          "WHERE s.TotalCost = (SELECT MIN(s.TotalCost) " + 
                                          "FROM Sales s)");
        
        return query;
        
    }
    
    public static Query salesOrderedByMonth(Session session){
        Query query = session.createQuery("SELECT s FROM Sales s ORDER BY Month(Date_id)");
        return query;
    }
    
    public static Query salesOrderedByYear(Session session){
        Query query = session.createQuery("SELECT s FROM Sales s ORDER BY Year(Date_id)");
        return query;
    }
    
    public static Query transWLeastUnitsSold(Session session){
        Query query = session.createQuery("SELECT s " +
                                          "FROM Sales s " +
                                          "WHERE s.Quantity = (SELECT MIN(s.Quantity) " +
                                          "FROM Sales s)");

        return query;
    }
    //  End of Query Methods

 
    public static void main(String[] args){

        App app = new App(); 
        
        Configuration con = new Configuration().configure().addAnnotatedClass(SalesTransactions.class);
        
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
        
        SessionFactory sf = con.buildSessionFactory(reg);     
        
        Session session = sf.openSession();
        
        //Transaction transaction = session.beginTransaction();
        
        DoMenu(Menu, session);
        
        //  Commented out just for testing
        //transaction.commit();
        
        //session.close();
        
    }
}
