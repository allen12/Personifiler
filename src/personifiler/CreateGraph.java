package personifiler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;

public class CreateGraph 
{
	Graph graph = new TinkerGraph();
	
	Map<String, Integer> groups;
	
	FeatureMatrix b;
	
	Vertex[] people;
	Vertex[] clusters;
	Edge[] edges;
	Vertex randIndex;
	
	ClusterPeople c;
	
	List<Person> groundTruth = GroundTruth.getList();
	
	public CreateGraph(String txtFile)
	{
		b = new BinaryFeatureMatrixV2();
		b.readFile(new File(txtFile));
		b.calculateFeatureMatrix();
		c = new ClusterPeople(b.featureMatrix);

		addNodes();
		addClusters();
		addEdges();
		createGML();
		
	}
	
	private void addEdges()
	{
		edges = new Edge[people.length];
		
		for (int i = 0; i < people.length; i++)
		{
			int group = groups.get(people[i].getProperty("name"));
			
			edges[i] = graph.addEdge(null, people[i], graph.getVertex(group), "lol");
		}
	}
	
	private void addClusters()
	{
		b.calculateFeatureMatrix();
		groups = c.getClusteredMap();
		
		Set<Integer> set = new TreeSet<Integer>(groups.values());
		Object[] allC = set.toArray();
		clusters = new Vertex[set.size()];

		for (int i = 0; i < allC.length; i++)
		{
			clusters[i] = graph.addVertex(allC[i]);
			clusters[i].setProperty("name", allC[i]);
			clusters[i].setProperty("type", "cluster");
		}
	}
	
	public void addNodes()
	{
		Set<String> allPeople = new TreeSet<String>(b.filesAndOwners.values());
		
		Object[] allP = allPeople.toArray();
		
		people = new Vertex[allPeople.size()];
		
		for (int i = 0; i < allPeople.size(); i++)
		{
			people[i] = graph.addVertex(allP[i]);
			people[i].setProperty("name", allP[i]);
			
			String group = "";
			for (Person p: groundTruth)
				if (p.name.equals(allP[i]))
				{
					group = p.group;
					break;
				}
			
			people[i].setProperty("group", group);
			people[i].setProperty("type", "person");
		}
		
		randIndex = graph.addVertex(allP.length);
		randIndex.setProperty("name", "rand index");
		randIndex.setProperty("type", "rand index");
		
		double r = c.randIndex(); 
		System.out.println("Rand index: " + r);
		
		randIndex.setProperty("rand", Double.toString(r));
	}
	
	private void createGML()
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("lol.txt")));
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(new File("lol2.txt")));
			
			Iterator<Map.Entry<String, Integer>> iterator = groups.entrySet().iterator();
			while (iterator.hasNext())
			{
				Map.Entry<String, Integer> entry = iterator.next();
				writer.write(entry.getKey() + "\t\t" + entry.getValue());
				writer.newLine();
				
				String owner = entry.getKey();
				for (Person p: groundTruth)
				{
					if (owner.equals(p.name))
					{
						writer2.write(owner + "\t\t" + p.group);
						writer2.newLine();
						break;
					}
				}
			}
			
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		try {
			GMLWriter.outputGraph(graph, "C:/temp/test-graph-binary.gml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) 
	{
		new CreateGraph("C:\\temp\\combine\\combined.txt");
	}

}
