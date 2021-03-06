package personifiler.example;

import java.io.File;
import java.util.Map;

import personifiler.cluster.ClusterPeople;
import personifiler.cluster.RandIndex;
import personifiler.featureMatrix.BinaryFeatureMatrix;
import personifiler.featureMatrix.FeatureMatrix;
import personifiler.util.Cluster;
import personifiler.util.CreateGraph;
import personifiler.util.GroundTruth;

public class Example
{

	public static void main(String[] args)
	{
		// Ingest the data into a feature matrix
		FeatureMatrix matrix = new BinaryFeatureMatrix();
		matrix.readFile(new File("data/file-lists/example.txt"), "\t");
		matrix.calculateFeatureMatrix();
		
		// Create a ClusterPeople object, used to perform the clustering
		ClusterPeople cluster = new ClusterPeople(matrix.getFeatureMatrix());
		
		// Obtain the clustered groups
		Map<String, Integer> clustered = cluster.getClusteredMap();
		
		// Transform the clustered map into a Cluster object
		Cluster<String> clusteredCluster = Cluster.transformIntoCluster(clustered);
		
		// Obtain the ground truth cluster
		GroundTruth groundTruth = new EmployeeGroundTruth("data/companyGroups.txt");
		Map<String, String> groundTruthGroups = groundTruth.getGroundTruthCluster(clustered.keySet());
		Cluster<String> groundTruthCluster = Cluster.transformIntoCluster(groundTruthGroups);
		
		// Calculate the rand index evaluation
		double randIndex = RandIndex.getRandIndex(clusteredCluster, groundTruthCluster);
		System.out.println("rand index: " + randIndex);
		
		// Write to a GraphML file
		CreateGraph createGraph = new CreateGraph(matrix, cluster, groundTruth);
		createGraph.createGraphML("data/test-graph-binary.graphml");
	}

}
