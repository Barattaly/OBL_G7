package entitiesQueries;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Employee;

public class EmployeeQueries
{

	public static String SelectAllEmployees()
	{
		String queryMsg = "SELECT employeeNumber,employeeID,firstName,lastName,emailAddress,role,department "
				+ "FROM obl_db.employees INNER JOIN obl_db.users ON obl_db.employees.employeeID = obl_db.users.id;";
		return queryMsg;
	}

	public static ArrayList<Employee> createEmpListFromRS(ResultSet rs)
	{
		ArrayList<Employee> empList = new ArrayList<>();
		try
		{
			while (rs.next())
			{

				Employee temp = new Employee(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getString(7));
				empList.add(temp);
			}
		} catch (SQLException e)
		{
			empList = new ArrayList<>();//empty list
			e.printStackTrace();
		}
		return empList;
	}

}
