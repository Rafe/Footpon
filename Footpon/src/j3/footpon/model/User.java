package j3.footpon.model;

public class User 
{
	private String username;
	private String firstName;
	private String lastName;
	private long steps;
	
	public User(String username, String firstName, String lastName, long steps)
	{
		this.username=username;
		this.firstName=firstName;
		this.lastName=firstName;
		this.steps=steps;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public void setUsername(String username)
	{
		this.username=username;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	
	public long getSteps()
	{
		return steps;
	}
	
	public void setSteps(long steps)
	{
		this.steps=steps;
	}
}