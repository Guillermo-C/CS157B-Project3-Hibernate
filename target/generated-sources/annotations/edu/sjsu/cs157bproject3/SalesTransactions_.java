package edu.sjsu.cs157bproject3;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(SalesTransactions.class)
public abstract class SalesTransactions_ {

	public static volatile SingularAttribute<SalesTransactions, Date> date;
	public static volatile SingularAttribute<SalesTransactions, Double> TotalCost;
	public static volatile SingularAttribute<SalesTransactions, Double> UnitCost;
	public static volatile SingularAttribute<SalesTransactions, String> ProductName;
	public static volatile SingularAttribute<SalesTransactions, Integer> Quantity;

}

