package application;

import java.sql.ResultSet;

public interface iSqlConnection
{
	int executeUpdate(String msg);
	ResultSet executeQuery(String query);
	
}
