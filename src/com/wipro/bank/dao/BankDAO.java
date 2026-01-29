package com.wipro.bank.dao;
import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDAO {
	public int generateSequenceNumber()
	{
		Connection connection=DBUtil.getDBConnection();
		String query="select transactionId.NEXTVAL from dual";
		try
		{
			PreparedStatement ps=connection.prepareStatement(query);
			ResultSet rs=ps.executeQuery();
			rs.next();
			int seqNumber=rs.getInt(1);
			return seqNumber;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	public boolean validateAccount(String accountNumber) 
	{
		Connection connection=DBUtil.getDBConnection();
		String query="Select * from account_tbl where Account_Number =?";
		try {
		PreparedStatement ps=connection.prepareStatement(query);
		ps.setString(1,accountNumber);
		ResultSet rs=ps.executeQuery();
		if(rs.next()) 
		{
			return true;
		}
		return false;
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public float findBalance(String accountNumber)
	{
		if(validateAccount(accountNumber))
		{
		Connection connection=DBUtil.getDBConnection();
		String query="Select Balance from account_tbl where Account_Number =?";
		try {
		PreparedStatement ps=connection.prepareStatement(query);
		ps.setString(1,accountNumber);
		ResultSet rs=ps.executeQuery();
		rs.next();
		float balance=rs.getFloat(1);
		return balance;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
		}
		return -1;
	}
	public boolean updateBalance(String accountNumber,float newBalance)
	{

		Connection connection=DBUtil.getDBConnection();
		String query="Update account_tbl SET Balance=? where Account_Number =?";
		try
		{
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setDouble(1,newBalance);
			ps.setString(2,accountNumber);
			int rows=ps.executeUpdate();
			if(rows>0)
			{
				return true;
			}
			return false;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	public boolean  transferMoney(TransferBean transferBean)
	{
		transferBean.setTransactionID(generateSequenceNumber());
		Connection connection=DBUtil.getDBConnection();
		String query="Insert into transfer_tbl values(?,?,?,?,?)";
		try
		{
			PreparedStatement ps=connection.prepareStatement(query);
			ps.setLong(1,transferBean.getTransactionID());
			ps.setString(2,transferBean.getFromAccountNumber());
			ps.setString(3,transferBean.getToAccountNumber());
			ps.setDate(4,new Date(transferBean.getDateOfTransaction().getTime()));
			ps.setFloat(5,transferBean.getAmount());
			int rows=ps.executeUpdate();
			if(rows>0)
			{
				return true;
			}
			return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
}
