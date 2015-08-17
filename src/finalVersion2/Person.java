package finalVersion2;


public class Person
{
	String name;
	String group;
	String ID;
	
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
}