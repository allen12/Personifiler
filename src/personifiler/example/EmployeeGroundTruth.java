package personifiler.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import personifiler.util.GroundTruth;
import personifiler.util.Person;
import personifiler.util.PersonifilerException;

public class EmployeeGroundTruth implements GroundTruth
{
	private final String EMPLOYEE_FILE;
	private List<Person> groundTruth;
	
	public EmployeeGroundTruth(final String DATA_FILE)
	{
		EMPLOYEE_FILE = DATA_FILE;
	}
	
	private void initGroundTruth()
	{
		List<Person> people = new ArrayList<>();

		try (BufferedReader b = new BufferedReader(new FileReader(new File(EMPLOYEE_FILE))) )
		{
			b.readLine(); // skip first line
			
			while (b.ready())
			{
				String line = b.readLine();
				String[] split = line.split("\t");
				
				Person person = new Person(split[0], split[1]);
				people.add(person);
			}
			
		} catch (IOException e) 
		{
			throw new PersonifilerException(e);
		}
		
		groundTruth = people;
	}
	
	public Collection<Person> getGroundTruth()
	{
		if (groundTruth == null)
			initGroundTruth();
		
		return groundTruth;
	}
	
	/**
	 * Retrieves a mapping of name-->group from the ground truth list
	 * 
	 * @param people A collection of the people's names that need to be retrieved
	 * @return
	 */
	public Map<String, String> getGroundTruthCluster(Collection<String> people)
	{
		Map<String, String> map = new HashMap<>();
		
		for (Person p: getGroundTruth())
		{
			if (people.contains(p.getName()))
				map.put(p.getName(), p.getGroup());
		}

		return map;
	}
}
