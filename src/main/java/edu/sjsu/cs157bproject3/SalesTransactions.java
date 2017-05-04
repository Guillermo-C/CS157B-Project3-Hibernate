/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package edu.sjsu.cs157bproject3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.persistence.Entity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 *
 * @author ninjamemo
 */

//  Table name is "Sales"
@Entity(name = "Sales")
public class SalesTransactions {
    
    
    //  Parameterized constructor of the SalesTransaction object
    SalesTransactions(String date, String ProductName, int Quantity, double UnitCost){
        
     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  
     
        try {
            this.date = dateFormat.parse(date);            
        } catch (ParseException ex) {
           System.out.println("Error parsing date!");
        }
        
        this.ProductName = ProductName;
        this.Quantity = Quantity; 
        this.UnitCost = UnitCost;
        this.TotalCost = (Quantity * UnitCost);
    }
    
    //  Default constructor of the SalesTransaction object
    SalesTransactions(){
    }
    
    //  Declaration of the attributes/columns of the table "Sales"
    @Id
    @Column(name = "Date_id")
    @Temporal(TemporalType.DATE)
    Date date;  // Primary Key
    
    @Column(name = "ProductName")
    String ProductName; 
    
    @Column(name = "Quantity")
    int Quantity;
    
    @Column(name = "UnitCost")
    double UnitCost; 
    
    @Column(name = "TotalCost")
    double TotalCost; 
    
    
    
    //  Setters for the attributes of type SalesTransactions
    
    public void setDate(Date date) {
        this.date = date;
    }

    public void setDate(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");   
        try {
            
            this.date = dateFormat.parse(date);
        } catch (ParseException ex) {
            System.out.println("Error parsing date!");
        }
    }
    
    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public void setUnitCost(double UnitCost) {
        this.UnitCost = UnitCost;
    }

    public void setTotalCost(double TotalCost) {
        this.TotalCost = TotalCost;
    }

    
    
    
    //  Getters for the attributes of type SalesTransactions
    public Date getDate() {
        return date;
    }

    public String getProductName() {
        return ProductName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getUnitCost() {
        return UnitCost;
    }

    public double getTotalCost() {
        return TotalCost;
    }
   
    
    //  Use toString() to print objects of type SalesTransactions
    @Override
    public String toString() {
        return "SalesTransactions{" + "date=" + date + ", ProductName=" + ProductName + ", Quantity=" + Quantity + ", UnitCost=" + UnitCost + ", TotalCost=" + TotalCost + '}';
    }
    
    
}
