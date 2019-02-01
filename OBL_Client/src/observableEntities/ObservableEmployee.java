package observableEntities;

import javafx.beans.property.SimpleStringProperty;

public class ObservableEmployee
{

	public SimpleStringProperty empNumber;
	public SimpleStringProperty id;
	public SimpleStringProperty firstName;
	public SimpleStringProperty lastName;
	public SimpleStringProperty email;
	public SimpleStringProperty role;
	public SimpleStringProperty department;

	public ObservableEmployee(String empNumber, String id, String firstName, String lastName, String email, String role,
			String department)
	{
		this.empNumber = new SimpleStringProperty(empNumber);
		this.id = new SimpleStringProperty(id);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty(email);
		this.role = new SimpleStringProperty(role);
		this.department = new SimpleStringProperty(department);
	}

	public String getEmpNumber()
	{
		return empNumber.get();
	}

	public void setEmpNumber(String empNumber)
	{
		this.empNumber = new SimpleStringProperty(empNumber);
	}

	public String getId()
	{
		return id.get();
	}

	public void setId(String id)
	{
		this.id = new SimpleStringProperty(id);
	}

	public String getFirstName()
	{
		return firstName.get();
	}

	public void setFirstName(String firstName)
	{
		this.firstName = new SimpleStringProperty(firstName);
	}

	public String getLastName()
	{
		return lastName.get();
	}

	public void setLastName(String lastName)
	{
		this.lastName = new SimpleStringProperty(lastName);
	}

	public String getEmail()
	{
		return email.get();
	}

	public void setEmail(String email)
	{
		this.email = new SimpleStringProperty(email);
	}

	public String getRole()
	{
		return role.get();
	}

	public void setRole(String role)
	{
		this.role = new SimpleStringProperty(role);
	}

	public String getDepartment()
	{
		return department.get();
	}

	public void setDepartment(String department)
	{
		this.department = new SimpleStringProperty(department);
	}

}
