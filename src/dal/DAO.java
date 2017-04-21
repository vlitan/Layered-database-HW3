package dal;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import models.Orderf;
import models.Product;

public class DAO {
	
	private static DAO instance;
	
	public static DAO instance(){
		if (instance == null){
			instance = new DAO();
		}
		return (instance);
	}
	
	@SuppressWarnings("null")
	public int insert(Object o){
		int retVal = -1;
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		java.sql.PreparedStatement insertStatement = null;
		String insertStatementString = "INSERT INTO " + o.getClass().getSimpleName().toLowerCase() + " (";
		for (Field field : o.getClass().getDeclaredFields()) {
			if (!field.getName().equals("id" + o.getClass().getSimpleName())){
				insertStatementString += field.getName() + ",";
			}
		}
		insertStatementString = insertStatementString.substring(0, insertStatementString.length() - 1);
		insertStatementString += ") VALUES (";
		for (Field field : o.getClass().getDeclaredFields()) {
			if (!field.getName().equals("id" + o.getClass().getSimpleName())){
				try {
					if (field.getType() == String.class){
						insertStatementString += "'" + field.get(o) + "'" + ",";
					}
					else{//assume int
						insertStatementString += field.get(o) + ",";
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (-1);
				}
			}
		}
		insertStatementString = insertStatementString.substring(0, insertStatementString.length() - 1);
		insertStatementString += ")";
		try {
			insertStatement = dbConnection.prepareStatement(insertStatementString, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (-1);
		}
		try {
			System.out.println("[DAO] executing " + insertStatementString);
			insertStatement.executeUpdate();
			java.sql.ResultSet rs = insertStatement.getGeneratedKeys();
			if (rs.next()) {
				retVal = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return (-1);
		}
		ConnectionFactory.close((Statement)insertStatement);
		ConnectionFactory.close(dbConnection);
		return (retVal);
	}
	
	public boolean update(Object o){
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		PreparedStatement updateStatement = null;
		String updateStatementString = "UPDATE " + o.getClass().getSimpleName().toLowerCase() + " SET ";
		for (Field field : o.getClass().getDeclaredFields()) {
			if (!field.getName().equals("id" + o.getClass().getSimpleName())){
				updateStatementString += field.getName() + " = ";
				try {
					if (field.getType() == String.class){
						updateStatementString += "'" + field.get(o) + "'";
					}
					else{
						updateStatementString += field.get(o);
					}
				}
				catch  (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (false);
				}
				updateStatementString += ", ";
			}
		}
		updateStatementString = updateStatementString.substring(0, updateStatementString.length() - 2);
		updateStatementString += " WHERE ";
		for (Field field : o.getClass().getDeclaredFields()) {
			if (field.getName().equals("id" + o.getClass().getSimpleName())){
				try {
					updateStatementString += field.getName() + " = " + field.get(o);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (false);
				}
			}
		}
		try {
			updateStatement = (PreparedStatement) dbConnection.prepareStatement(updateStatementString);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (false);
		}
		try {
			System.out.println("[DAO] executing " + updateStatementString);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
		ConnectionFactory.close(updateStatement);
		ConnectionFactory.close(dbConnection);
		return (true);
	}
	public boolean delete(Object o){
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		PreparedStatement deleteStatement = null;
		String deleteStatementString = "DELETE FROM " + o.getClass().getSimpleName().toLowerCase() + " WHERE ";
		for (Field field : o.getClass().getDeclaredFields()) {
			if (field.getName().equals("id" + o.getClass().getSimpleName())){
				try {
					deleteStatementString += field.getName() + " = " + field.get(o);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					return (false);
				}
			}
		}
		try {
			deleteStatement = (PreparedStatement) dbConnection.prepareStatement(deleteStatementString);
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (false);
		}
		try {
			System.out.println("[DAO] executing " + deleteStatementString);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return (false);
		}
		ConnectionFactory.close(deleteStatement);
		ConnectionFactory.close(dbConnection);
		return (true);
	}
	
	public ArrayList<Object> selectAll(Class<?> objectType){
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		Statement selectStatement = null;
		ResultSet rs = null;
		ArrayList<Object> results = new ArrayList<Object>();
		String selectStatementString = "SELECT * FROM " + objectType.getSimpleName().toLowerCase();
		
		try {
			selectStatement = (Statement) dbConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (null);
		}
		try {
			System.out.println("[DAO] executing " + selectStatementString);
			rs = (ResultSet) selectStatement.executeQuery(selectStatementString);
		} catch (SQLException e) {
			e.printStackTrace();
			return (null);
		}
		try {
			while (rs.next())
			{
				Object o = Class.forName(objectType.getName()).getConstructor().newInstance();
				for (Field field : objectType.getDeclaredFields()) {
					if (field.getType() == String.class){
						field.set(o, rs.getString(field.getName().toString()));
					}
					else{//assume int
						field.set(o, rs.getInt(field.getName().toString()));
					}
				}
				results.add(o);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException
				| SQLException e) {
			e.printStackTrace();
			return (null);
		}
		
		ConnectionFactory.close(rs);
		ConnectionFactory.close(selectStatement);
		ConnectionFactory.close(dbConnection);
		return (results);
	}
	
	public ArrayList<Object> findBy(Class<?> objectType, String fieldName, int val){
		return null;
	}
	public ArrayList<Object> findBy(Class<?> objectType, String fieldName, String val){
		return null;
	}
	public ArrayList<Object> findBy(Class<?> objectType, String fieldName, Date val){
		return null;
	}
	
	public Object findById(Object o){
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		Statement selectStatement = null;
		ResultSet rs = null;
		ArrayList<Object> results = new ArrayList<Object>();
		int id = 0;
		String selectStatementString = "SELECT * FROM " + o.getClass().getSimpleName().toLowerCase() + 
										" WHERE id" + o.getClass().getSimpleName().toLowerCase() + " = ";
		for (Field field : o.getClass().getDeclaredFields()){
			if (field.getName().equals("id" + o.getClass().getSimpleName())){
				try {
					id = (int) field.get(o);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		selectStatementString += id;
		try {
			selectStatement = (Statement) dbConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (null);
		}
		try {
			System.out.println("[DAO] executing " + selectStatementString);
			rs = (ResultSet) selectStatement.executeQuery(selectStatementString);
		} catch (SQLException e) {
			e.printStackTrace();
			return (null);
		}
		try {
			while (rs.next())
			{
				Object obj = Class.forName(o.getClass().getName()).getConstructor().newInstance();
				for (Field field : o.getClass().getDeclaredFields()) {
					if (field.getType() == String.class){
						field.set(obj, rs.getString(field.getName().toString()));
					}
					else{//assume int
						field.set(obj, rs.getInt(field.getName().toString()));
					}
				}
				results.add(obj);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException
				| SQLException e) {
			e.printStackTrace();
			return (null);
		}
		
		ConnectionFactory.close(rs);
		ConnectionFactory.close(selectStatement);
		ConnectionFactory.close(dbConnection);
		if (results.size() > 0){
			return (results.get(0));
		}
		else {
			return (null);
		}
	}
	
	public ArrayList<Product> getProductsOfOrder(Orderf order){
		if (order == null) return null;
		java.sql.Connection dbConnection = ConnectionFactory.getConnection();
		ArrayList<Product> products = new ArrayList<Product>();
		Statement selectStatement = null;
		ResultSet rs = null;
		String selectStatementString = "SELECT p.* " +
										"FROM product as p " + 
										"INNER JOIN orderfhasproduct as op ON op.idProduct = p.idProduct " + 
										"INNER JOIN orderf as o ON op.idOrder = o.idOrderf " + 
										"WHERE o.idOrderf = " + order.idOrderf;
		try {
			selectStatement = (Statement) dbConnection.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return (null);
		}
		try {
			System.out.println("[DAO] executing " + selectStatementString);
			rs = (ResultSet) selectStatement.executeQuery(selectStatementString);
		} catch (SQLException e) {
			e.printStackTrace();
			return (null);
		}
		try {
			while (rs.next())
			{
				Product p = new Product();
				for (Field field : p.getClass().getDeclaredFields()) {
					if (field.getType() == String.class){
						field.set(p, rs.getString(field.getName().toString()));
					}
					else{//assume int
						field.set(p, rs.getInt(field.getName().toString()));
					}
				}
				products.add(p);
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			return (null);
		}
		ConnectionFactory.close(rs);
		ConnectionFactory.close(selectStatement);
		ConnectionFactory.close(dbConnection);
		return (products);
	}

}
