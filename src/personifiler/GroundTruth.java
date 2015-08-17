package personifiler;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroundTruth 
{
	
	static List<Person> list = generateList();
	
	private static List<Person> generateList()
	{
		List<Person> people = new ArrayList<Person>();


		try 
		{
			BufferedReader b = new BufferedReader(new FileReader(new File("\\\\dom1\\aos\\Public\\Lieberman\\people_locator_with521.txt")));
			b.readLine();

			String line = b.readLine();

			while (line != null)
			{
				char[] charArray = line.toCharArray();
				// Find the index of where the name is terminated (when there are numbers representing the employee ID)
				int index = 0;
				for (index = 0; index < charArray.length; index++)
					if (charArray[index] >= '0' && charArray[index] <= '9')
						break;

				String name = line.substring(0, index);
				name = name.trim();


				// Find the index of where the group name starts
				for (int i = index; i < charArray.length; i++)
					if (charArray[i] >= 'A' && charArray[i] <= 'Z')
					{
						index = i; break;
					}

				//Find the index of where the group name ends
				int end = index;
				for (int i = index; i < charArray.length; i++)
				{
					if (charArray[i] == ' ' || charArray[i] == '\t')
					{
						end = i; break;
					}
				}
				String group = line.substring(index, end);
				group = group.trim();

				// Find the index of the JHU login
				int indexAt = -1;
				int indexStart = 0;
				int indexEnd = 0;
				String ID = "";
				for (int i = 0; i < charArray.length; i++)
					if (charArray[i] == '@') indexAt = i;

				if (indexAt != -1)
				{
					for (indexStart = indexAt; indexStart < charArray.length; indexStart++)
						if (charArray[indexStart] >= 'A' && charArray[indexStart] <= 'Z')
							break;

					for (indexEnd = indexStart; indexEnd < charArray.length; indexEnd++)
						if (charArray[indexEnd] == ' ' || charArray[indexEnd] == '\t')
							break;

					ID = line.substring(indexStart, indexEnd);
				}

				ID = ID.toLowerCase();

				Person p = new Person(name, group, ID);


				people.add(p);

				line = b.readLine();
			}

			b.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return people;
	}
	
	
	
	//Get a list of the ground truth people
	public static List<Person> getList()
	{
		return list;
	}
}
