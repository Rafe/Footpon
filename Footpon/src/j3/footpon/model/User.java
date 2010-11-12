package j3.footpon.model;

public class User 
{
	private String username;
	private String firstName;
	private String lastName;
	private long points;
	
	public User(String username, String firstName, String lastName, long points)
	{
		this.username=username;
		this.firstName=firstName;
		this.lastName=firstName;
		this.points=points;
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
	
	public long getPoints()
	{
		return points;
	}
	
	public void setPoints(long points)
	{
		this.points=points;
	}
}