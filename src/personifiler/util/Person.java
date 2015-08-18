package personifiler.util;

/**
 * Person is a class representing a file owner.
 * 
 * @author Allen Cheng
 */
public class Person
{
	private String name;
	private String group;
	
	public Person(String n, String g)
	{
		name = n; group = g;
	}
	
	public boolean equals(Object p)
	{
		Person person = (Person) p;
		
		if (name.equals(person.getName() ) && group.equals(person.getGroup() ))
			return true;
		
		return false;
	}
	
	public String toString()
	{
		return(name + "\t" + group);
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getGroup()
	{
		return group;
	}

}