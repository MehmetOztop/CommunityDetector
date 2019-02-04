import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;

public class Reader {

	public static int[] Ids;//ids of people
	public static String[] Labels;//Labels(names) of people
	static Edge[] Edges = new Edge[0];//list of the edges
	static EdgeWeightedGraph MyGraph;

	public static int[] resizeaddI(int[] IdsL, int Id) {//resizes the array and adds the input integer to the array
		int IdLength = IdsL.length;
		int[] RIds = new int[IdLength + 1];
		System.arraycopy(IdsL, 0, RIds, 0, IdLength);
		RIds[IdLength] = Id;
		return RIds;
	}

	public static String[] resizeaddL(String[] LabelsL, String LabelName) {//resizes the array and adds the input string to the array
		int LabelLength = LabelsL.length;
		String[] RLabels = new String[LabelLength + 1];
		System.arraycopy(LabelsL, 0, RLabels, 0, LabelLength);
		RLabels[LabelLength] = LabelName;
		return RLabels;
	}

	public static Edge[] resizeaddE(Edge[] EdgesI, Edge Input) {//resizes the array and adds the input Edge to the array
		int EdgesLength = EdgesI.length;
		Edge[] REdges = new Edge[EdgesLength + 1];
		System.arraycopy(EdgesI, 0, REdges, 0, EdgesLength);
		REdges[EdgesLength] = Input;
		return REdges;
	}

	public Edge[] resizeremoveE(Edge[] EdgesI, Edge Input) {//resizes the array and removes the input Edge to the array
		int EdgesLength = EdgesI.length;
		Edge[] REdges = new Edge[EdgesLength - 1];
		System.arraycopy(EdgesI, 0, REdges, 0, indexfinderE(EdgesI, Input));
		System.arraycopy(EdgesI, indexfinderE(EdgesI, Input) + 1, REdges, indexfinderE(EdgesI, Input),
				EdgesI.length - indexfinderE(EdgesI, Input) - 1);
		return REdges;
	}

	public static int indexfinder(int[] list, int y) {//finds the index of given number at the given list
		int b = 0;
		for (int x = 0; x < list.length; x++) {
			if (list[x] == y) {
				b = x;
			}
		}
		return b;
	}

	public int indexfinderE(Edge[] list, Edge y) {//finds the index of given Edge at the given list
		int b = 0;
		for (int x = 0; x < list.length; x++) {
			if (list[x] == y) {
				b = x;
			}
		}
		return b;
	}

	public static Edge[] ReadN() {//Reads given data, fills Ids of people, Labels of people and Edges which is list of edges
		File f = new File("babel.gexf.txt");
		String line = "";
		try {
			Ids = new int[0];
			Labels = new String[0];
			FileInputStream FIS = new FileInputStream(f);
			DataInputStream DIS = new DataInputStream(FIS);
			BufferedReader BR = new BufferedReader(new InputStreamReader(DIS));
			boolean b = true;
			while (b) {
				if ((line = BR.readLine()) != null) {//if line is not null keep reading
					String[] dataL = line.split(" ");//splits line with space
					while (dataL[0].equals("") ) {//if first letter of line is space deletes it to gain full array
						String[] t = new String[dataL.length - 1];
						System.arraycopy(dataL, 1, t, 0, dataL.length - 1);
						dataL = t;
					}
					if (dataL[0].equals("<node")) {//if the first letter of array is "<node" it means the line is about node 
						String[] IdDataT = dataL[1].split("\"(,\")?");//after splitting, the first index contains id of node like id="123456",then we split id="123456" with "  
						String[] LabelDataT = dataL[2].split("\"(,\")?");//after splitting, the second index contains label of node like label="MAHMUT", then we split label="MAHMUT" with "
						int idT = Integer.parseInt(IdDataT[1]);//first index of IdDataT which is like [id,213452] is id of the node
						Ids = resizeaddI(Ids, idT);//adds the id of node to ids list
						Labels = resizeaddL(Labels, LabelDataT[1]);//adds the label of node to labels list
					}

					else if (dataL[0].equals("<edge")) {//if the first letter of array is "<edge" it means the line is about edge
						String[] EdgeSourceT = dataL[1].split("\"(,\")?");//after splitting, the first index contains id of source node like id="123456",then we split id="123456" with "  
						String[] EdgeTargetT = dataL[2].split("\"(,\")?");//after splitting, the second index contains id of target node like id="123456",then we split id="123456" with "  
						if (dataL.length == 5) {//at that line if the weight of the node is not 1.0 then it is written at the end, which increases the length of the line to 5  
							String[] EdgeWeightT = dataL[4].split("\"(,\")?");//after splitting, the fourth index contains weight of edge like Weight="3.0",then we split Weight="3.0" with "  
							int SourceT = Integer.parseInt(EdgeSourceT[1]);//first index of EdgeSourceT is the id of the Source node
							int TargetT = Integer.parseInt(EdgeTargetT[1]);//first index of EdgeTargetT is the id of the Target node
							double WeightT = Double.parseDouble(EdgeWeightT[1]);//first index of EdgeWeightT is the weight of the edge
							Edges = resizeaddE(Edges,//adds the current edge to the Edges array
									new Edge(indexfinder(Ids, SourceT), indexfinder(Ids, TargetT), WeightT));
						} else {//if the length of the line is not 5, it means weight is 1.0 
							int SourceT = Integer.parseInt(EdgeSourceT[1]);
							int TargetT = Integer.parseInt(EdgeTargetT[1]);
							Edges = resizeaddE(Edges,
									new Edge(indexfinder(Ids, SourceT), indexfinder(Ids, TargetT), 1.0));

						}

					}
				} else {//if the line is empty then stop reading
					b = false;
				}

			}
			BR.close();

		} catch (Exception e) {
			System.out.println("hata");
		}
		return Edges;

	}

	public EdgeWeightedGraph GraphGenerator(Edge[] EdgeList) {//creates an Edge Weighted Graph out of a given list of edges
		MyGraph = new EdgeWeightedGraph(Ids.length);//creates an empty graph with an empty vertex for each person
		for (int i = 0; i < EdgeList.length; i++) {//adds the edges of the given list to the graph
			MyGraph.addEdge(EdgeList[i]);
		}
		return MyGraph;
	}

	public EdgeWeightedGraph terminator(EdgeWeightedGraph MyGraph) {//finds the most used edge while creating the shortest path for each vertex from a starting node and deletes it
		int[] EdgeCount = new int[Edges.length];//empty array to count how many times each edge is used

		for (int l = 0; l < EdgeCount.length; l++) {//assigns zero for every edge
			EdgeCount[l] = 0;
		}
		for (int i = 0; i < Labels.length - 1; i++) {//creates the shortest path between every vertex
			for (int j = i + 1; j < Labels.length; j++) {
				DijkstraUndirectedSP sp = new DijkstraUndirectedSP(MyGraph, i);
				if (sp.hasPathTo(j)) {
					for (Edge e : sp.pathTo(j)) {
						int x = indexfinderE(Edges, e);
						EdgeCount[x] = EdgeCount[x] + 1;//if an edge was part of a shortest path, increase its count
					}
				}
			}
		}

		int y = 0;
		int index = 0;

		for (int h = 0; h < EdgeCount.length; h++) {//finds the highest count
			if (EdgeCount[h] > y) {
				y = EdgeCount[h];
				index = h;
			}
		}

		System.out.println("The most important Edge   : "+Labels[Edges[index].v]+"-"+Labels[Edges[index].w] +"  Weight : "+Edges[index].weight+ "   " + y);
		Edges = resizeremoveE(Edges, Edges[index]);//deletes the most used edge
		EdgeWeightedGraph RGraph = GraphGenerator(Edges);//regenerates the graph without that edge

		return RGraph;

	}
	
	public EdgeWeightedGraph MegaTerminator(EdgeWeightedGraph GraphI,int n) {//creates n many loops of terminator for a given graph
		for(int i=0;i<n;i++) {
			GraphI=terminator(GraphI);
		}
		return GraphI;
	}

	public static void main(String[] args) {
		Reader r = new Reader();
		Edge[] deneme = ReadN();
		System.out.println("Kнон SAYISI" + " " + Labels.length);
		System.out.println("EDGE SAYISI" + " " + deneme.length);
		EdgeWeightedGraph gp = r.GraphGenerator(deneme);
		r.MegaTerminator(gp, 10);

	}
}
