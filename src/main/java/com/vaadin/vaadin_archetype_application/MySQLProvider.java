package com.vaadin.vaadin_archetype_application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

public abstract class MySQLProvider implements DBProvider {

	@Override
	public Connection OpenConnection(String connectionString)
	{
		try 
		{
			return DriverManager.getConnection(connectionString);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Object[]> ReadSQLOutput(ResultSet rs)
	{
		try
		{
			int columns = rs.getMetaData().getColumnCount();
			ArrayList<Object[]> results = new ArrayList<>();
			while(rs.next())
			{
				Object[] row = new Object[columns];
				for (int i = 0; i < columns; i++)
				row[i] = rs.getObject(i + 1);
				results.add(row);
			}
			return results;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Object[]> ExecuteQuery(Connection connection, String query)
	{
		try 
		{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			ArrayList<Object[]> results = ReadSQLOutput(rs);
			rs.close();
			return results;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean Execute(Connection connection, String query)
	{
		try 
		{
			Statement st = connection.createStatement();
			st.execute(query);
			return true;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public ArrayList<Object[]> ExecuteStoredProcedure(Connection connection, String name, Object[] params)
	{
		String sParams = "";
		if (params != null && params.length != 0)
		{
			for (int i = 0; i < params.length; i++)
				sParams += "?,";
			sParams = "(" + sParams.substring(0, sParams.length() - 1) + ")";
		}
		
		try
		{
			CallableStatement cs = connection.prepareCall("{ call " + name + sParams + "}");
			if (params != null)
				for (int i = 0; i < params.length; i++)
					if (params[i] instanceof String)
						cs.setString(i + 1, (String)params[i]);
					else if (params[i] instanceof Date)
						cs.setDate(i + 1, new java.sql.Date(((Date)params[i]).getTime()));
					else if (params[i] instanceof Integer)
						cs.setInt(i + 1, (int)params[i]);
					else if (params[i] instanceof Long)
						cs.setLong(i + 1, (long)params[i]);
			
			ResultSet rs = cs.executeQuery();
			ArrayList<Object[]> results = ReadSQLOutput(rs);
			rs.close();
			return results;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
}
