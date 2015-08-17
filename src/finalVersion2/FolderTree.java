package finalVersion2;

import java.util.ArrayList;
import java.util.List;

public class FolderTree 
{
	Folder root;
	
	public FolderTree(Folder root)
	{
		this.root = root;
	}
	
	/**
	 * Adds the owner of the specified path file to the 
	 * appropriate Folder node. 
	 * 
	 * Precondition: The path parameter must be in the format
	 * 		"root\\aaa\\bbb\\ccc\\...\\eee.ff"
	 * 		where root is the name of the root.
	 * @param path
	 * 
	 */
	public void addFile(String path, String owner)
	{
		root.addFile(path, owner);
	}
	
	
	
	
	public static class Folder 
	{
		String name;
		List<String> owners = new ArrayList<String>();
		
		Folder parent;
		List<Folder> children = new ArrayList<Folder>();
		
		public Folder(String name, Folder parent)
		{
			this.name = name;
			this.parent = parent;
		}
		
		public void addOwner(String o)
		{
			owners.add(o);
		}
		
		/**
		 * Adds the owner of the specified path file to the 
		 * appropriate Folder node. 
		 * 
		 * Precondition: The path parameter must be in the format
		 * 		"name\\aaa\\bbb\\ccc\\...\\eee.ff"
		 * 		where name is the name parameter of this folder.
		 * @param path
		 * 
		 */
		public void addFile(String path, String owner)
		{

			String stuffAfterName = path.replace(name + "\\", "");


			String[] split = stuffAfterName.split("\\\\");

			
			System.out.println("adding " + path);
			
			// We say that it's a file if it contains a "."
			if (split[0].contains("."))
				this.addOwner(owner);
			// Otherwise, add the file to one of the child nodes of this folder
			// (create the folder object if it doesn't exist)
			else if (split.length > 1)
			{
				for (Folder f: children)
				{
					if (f.name.equals(split[0]))
					{
						f.addFile(stuffAfterName, owner);
						return;
					}
				}
				
				// The child folder doesn't exist, so create that folder!
				Folder folder = new Folder(split[0], this);
				this.children.add(folder);
				folder.addFile(stuffAfterName, owner);
			}
		}
		

		public String toString()
		{
			String s = "";
			
			s += name + "\n\t";
			for (String owner: owners)
				s += owner + "\n\t";
			
			for (Folder f: children)
				s += f;
			
			return s;
		}
		
		public boolean equals(Object o)
		{
			Folder other = (Folder)o;
			
			return this.name.equals(other.name);
		}
	}
}
