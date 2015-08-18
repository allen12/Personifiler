package personifiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import personifiler.util.PersonifilerException;

public class A
{

	public static void main(String[] args)
	{
		replace(new File("data/file-lists/combined1.txt"), "\t\t\t", 2);
	}
	
	public static void readFile(File txt, String delimiter, int skipLines)
	{
		Set<String> set = new HashSet<>();
		
		if (skipLines < 0)
			throw new PersonifilerException("skipLines cannot be negative");
		
		try (BufferedReader r = new BufferedReader(new FileReader(txt)) )
		{
			for (int i = 0; i < skipLines && r.ready(); i++)
				r.readLine();
			
			while (r.ready())
			{
				String line = r.readLine();
				
				if (line == null || line.equals(""))
					continue;
				
				String[] split = line.split(delimiter);
				
				set.add(split[1]);
			}

		} catch (FileNotFoundException e) 
		{
			System.out.println("File not found!");
			
		} catch (IOException e) 
		{
			throw new PersonifilerException(e);
		}
		
		for (String s : set)
			System.out.println(s);
	}
	
	private static void replace(File txt, String delimiter, int skipLines)
	{
		try (BufferedReader r = new BufferedReader(new FileReader(txt));
			 BufferedWriter w = new BufferedWriter(new FileWriter(new File("data/file-lists/example.txt"))) )
		{
		
			for (int i = 0; i < skipLines && r.ready(); i++)
				r.readLine();
			
			while (r.ready())
			{
				String line = r.readLine();
				
				if (line == null || line.equals(""))
					continue;
				
				String[] split = line.split(delimiter);
				
				String name = "";
				switch (split[1])
				{
				case "Parker, Kim Lorin,": name = "Person 1"; break;
				case "Franzen, Daniel Wayne,": name = "Person 2"; break;
				case "Morey, Nyoka Mae,": name = "Person 3"; break;
				case "Handy, Shaleta Bennett,": name = "Person 4"; break;
				case "Hatleberg, Jackie Jean,": name = "Person 5"; break;
				case "Almeleh, Nancy Esther,": name = "Person 6"; break;
				case "Dodge, Elspeth Margaret,": name = "Person 7"; break;
				case "Smith, Douglas Scott,": name = "Person 8"; break;
				case "Henry, Matthew Haven,": name = "Person 9"; break;
				case "Gordon, J Daniel ,": name = "Person 10"; break;
				case "Minch, David Henry,": name = "Person 11"; break;
				case "Shaw, Thomas Tyler,": name = "Person 12"; break;
				case "Stallings, Kirk Thomas,": name = "Person 13"; break;
				case "Mason, Rae Stephanie,": name = "Person 14"; break;
				}
				
				w.write(split[0] + "\t" + name);
				w.newLine();
			}
			
			w.flush();
			w.close();
			
		} catch (IOException e)
		{
			
		}
	}

}
