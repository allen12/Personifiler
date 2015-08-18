package personifiler.util;

import java.io.IOException;
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
import personifiler.featureMatrix.FeatureMatrix;

/**
 * <p> Class that outputs a GML or GraphML file representing a graph.
 * 
 * <p> The resulting graph implementation contains two types of vertices:
 * clusters and people
 * 
 * <p> Edges are drawn from each person vertex to the cluster vertex that they belong to,
 * according to the results of the Personifiler clustering. Each person vertex also 
 * contains their actual ground truth group. This allows analytics to be performed on
 * the graph visualization, such as in {@link http://i.imgur.com/F4kuIap.png}. 
 * 
 * <p>The blue nodes are cluster vertices, the red nodes are person vertices. Each person 
 * node belongs to some colored region, and all the nodes in a colored region belong to the 
 * same ground truth group. We can then roughly determine how accurate the Personifiler
 * clustering is based on whether most of the nodes within a colored region have edges
 * pointing to the same blue cluster node.
 * 
 * @author Allen Cheng
 */
public class CreateGraph 
{
	private Graph graph;
	
	private Map<String, Integer> groups;
	
	private Vertex[] people;
	private Vertex[] clusters;
	private Edge[] edges;
	private Vertex randIndex;
	
	private FeatureMatrix b;
	private ClusterPeople c;
	private GroundTruth g;
	
	
	public CreateGraph(FeatureMatrix b, ClusterPeople c, GroundTruth groundTruth)
	{
		this.b = b;
		this.c = c;
		this.g = groundTruth;
	}
	
	/**
	 * Generates a graph, as described in the class documentation.
	 */
	public void generateGraph()
	{
		graph = new TinkerGraph();
		
		addNodes();
		addClusters();
		addEdges();
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
			
			edges[i] = graph.addEdge(null, people[i], graph.getVertex(group), "");
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
			for (Person p: g.getGroundTruth())
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
		
		double r = c.randIndex(g); 
		
		randIndex.setProperty("rand", Double.toString(r));
	}

	/**
	 * Outputs a GML file
	 * 
	 * @param PATH_TO_OUT
	 */
	public void createGML(final String PATH_TO_OUT)
	{
		if (graph == null)
			generateGraph();
		
		try {
			GMLWriter.outputGraph(graph, PATH_TO_OUT);
		} catch (IOException e) {
			throw new PersonifilerException(e);
		}
	}
	
	/**
	 * Outputs a GraphML file
	 * 
	 * @param PATH_TO_OUT
	 */
	public void createGraphML(final String PATH_TO_OUT)
	{
		if (graph == null)
			generateGraph();

		try {
			GraphMLWriter.outputGraph(graph, PATH_TO_OUT);
		} catch (IOException e) {
			throw new PersonifilerException(e);
		}
	}

}
