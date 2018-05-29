package mas.behaviours.tanker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import jade.core.behaviours.OneShotBehaviour;
import tools.GraphAK;

public class ClosenessVertices extends OneShotBehaviour {

	private static final long serialVersionUID = 7339937027576841690L;
	private GraphAK G;
	private int n = 1;
	public ClosenessVertices(final mas.abstractAgent myagent, GraphAK g) {
		super(myagent);
		this.G = g;
	}
	
	
//	public String PCC(String src){
//		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
//		int dist_min = G.vertexSet().size();
//		String next_node = src;
//		for(String dst: ouverts){
//			try{
//				List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
//				if(shortestPath.size() < dist_min ){
//					dist_min = shortestPath.size();
//					next_node = shortestPath.get(1);
//				}
//			}catch(Exception e){
//				System.out.println(myAgent.getLocalName()+": error in "+dst);
//			}
//		}
//		return next_node;
//	}
	
	
	public void carrefour() {
		Set<String> nodes = G.getHashNode().keySet();
		Set<String> noeuds_centrales = new HashSet<String>();
		for(String v : nodes) {
			int nb_degree = G.degreeOf(v);
			boolean adj = false;
			if(noeuds_centrales.size() == n) {
				int min = nb_degree;
				String to_remove = v;
				for(String u : noeuds_centrales) {
					int val = G.degreeOf(u);
					for(String n : noeuds_centrales) {
						if(G.getDictAdjacences().get(n).contains(v)) {
							adj = true;
							break;
						}
					}
					if(!adj &&val < min && !(G.getDictAdjacences().get(u).contains(v))) {
						to_remove = u;
						min = val;
					}
				}
				if(!to_remove.equals(v)) {
					noeuds_centrales.remove(to_remove);
					noeuds_centrales.add(v);
				}
			}
			else if(noeuds_centrales.size() < n+1) {
				for(String n : noeuds_centrales) {
					if(G.getDictAdjacences().get(n).contains(v))
						continue;
				}
				noeuds_centrales.add(v);
			}
		}
		G.addAllPositionSilo(new ArrayList<String>(noeuds_centrales));
	}
	
	
	@Override
	public void action() {
		carrefour();
		System.out.println("I WILL BE MOVING BETWEEN : \n\t"+G.siloPositions());
	}

}
