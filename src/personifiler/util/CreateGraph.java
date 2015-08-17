package personifiler.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import personifiler.cluster.ClusterPeople;
import personifiler.featureMatrix.BinaryFeatureMatrix;
import personifiler.featureMatrix.FeatureMatrix;

/**
 * 
 * Class that outputs a .gml file
 * 
 * @author Allen Cheng
 */
public class CreateGraph 
{
	private Graph graph = new TinkerGraph();
	
	private Map<String, Integer> groups;
	
	private FeatureMatrix b;
	
	private Vertex[] people;
	private Vertex[] clusters;
	private Edge[] edges;
	private Vertex randIndex;
	
	private ClusterPeople c;
	
	private List<Person> groundTruth = GroundTruth.getList();
	
	/**
	 * 
	 * @param txtFile The text file to ingest data from
	 */
	public CreateGraph(final String in, final String out)
	{
		b = new BinaryFeatureMatrix();
		b.readFile(new File(in));
		b.calculateFeatureMatrix();
		c = new ClusterPeople(b.getFeatureMatrix());

		addNodes();
		addClusters();
		addEdges();
		
		createGML(out);
	}
	
	public CreateGraph(FeatureMatrix b, ClusterPeople c)
	{
		this.b = b;
		this.c = c;
		
		addNodes();
		addClusters();
		addEdges();
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
	
	private void addNodes()
	{
		Set<String> allPeople = new TreeSet<String>(b.getFilesAndOwners().values());
		
		Object[] allP = allPeople.toArray();
		
		people = new Vertex[allPeople.size()];
		
		for (int i = 0; i < allPeople.size(); i++)
		{
			people[i] = graph.addVertex(allP[i]);
			people[i].setProperty("name", allP[i]);
			
			String group = "";
			for (Person p: groundTruth)
				if (p.getName().equals(allP[i]))
				{
					group = p.getGroup();
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
	
	public void createGML(final String PATH_TO_OUT)
	{
		try {
			GMLWriter.outputGraph(graph, PATH_TO_OUT);
		} catch (IOException e) {
			throw new PersonifilerException(e);
		}
	}
	
	public void createGraphML(final String PATH_TO_OUT)
	{
		try {
			GraphMLWriter.outputGraph(graph, PATH_TO_OUT);
		} catch (IOException e) {
			throw new PersonifilerException(e);
		}
	}

}
