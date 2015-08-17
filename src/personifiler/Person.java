package personifiler;

/**
 * Person is a class representing a file owner.
 * 
 * @author Allen Cheng
 */
public class Person
{
	private String name;
	private String group;
	private String ID;
	
	public Person(String n, String g, String i)
	{
		name = n; group = g; ID = i;
	}
	
	public boolean equals(Object p)
	{
		Person person = (Person) p;
		
		if (name.equals(person.name) && group.equals(person.group) && ID.equals(person.ID))
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return(name + "\t" + group + "\t" + ID);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getGroup()
	{
		return group;
	}
	
	public String getID()
	{
		return ID;
	}
}