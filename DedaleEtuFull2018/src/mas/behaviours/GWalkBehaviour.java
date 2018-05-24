package mas.behaviours;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import Tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

public abstract class GWalkBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -3874406343681374104L;
	protected Set<String> fermes,ouverts;
	protected GraphAK G;
	protected int onEndValue=0;
	protected boolean finished = false;
	protected String last_move="";
	
	
	
	
	public GWalkBehaviour(GraphAK g, Set<String> f, Set<String> o,final mas.abstractAgent myagent) {
		super(myagent);
		this.G = g;
		this.fermes= f;
		this.ouverts = o;
	}
	
	
	
	
	/***
	 * @param src position courante 
	 * @param adjacents : liste de couple contenant le nom du noeud et les informations sur ce noeud
	 * @return List<String> des noeuds adjacents
	 */
	public List<String> m_a_j_graphe(String src, List<Couple<String, List<Attribute>>> adjacents){
		List<String> ladj_node = new ArrayList<String>();

			G.addVertex(src);
			for(Couple<String, List<Attribute>> adjacent: adjacents){
				String adj_name = adjacent.getLeft();
				ladj_node.add(adjacent.getLeft());
				G.addVertex(adj_name,adjacent.getRight());
				G.addEdge(src,adj_name);
		}
		return ladj_node;
	}
	
	
	/***
	 * @param src position courante 
	 * @return le prochain deplacement vers le noeud ouvert non exploré le plus proche(DIJKSTRA)
	 */
//	public String getNextPositionNearestOpenVertex(String src){
//		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
//		int dist_min = G.vertexSet().size();
//		String next_node = src;
//		
//		for(String dst: ouverts){
//			List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
//			if(shortestPath.size() < dist_min){
//				dist_min = shortestPath.size();
//				next_node = shortestPath.get(1);
//			}
//		}
//		return next_node;
//	}
	
	
	public String getNextPositionNearestOpenVertex(String src){
		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
		int dist_min = G.vertexSet().size();
		String next_node = src;
		for(String dst: ouverts){
			try{
				List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
				if(shortestPath.size() < dist_min ){
					dist_min = shortestPath.size();
					next_node = shortestPath.get(1);
				}
			}catch(Exception e){
				System.out.println(myAgent.getLocalName()+": error in "+dst);
			}
		}
		return next_node;
	}
	
	
	
	public String choisirLeProchainNodeOuvert(List<String> successors){
		String next_node;
		
		next_node = successors.get(0);
		int max = G.getDegreeOfNode(next_node);
			for(String succ : successors) {
				int value_tmp_node = G.getDegreeOfNode(succ);
				if(value_tmp_node > max) {
					max = value_tmp_node;
					next_node = succ;
				}
			}
		return next_node;
	}
	
	
	/***
	 * DEUX POSSIBILITES :
	 *  	Priorite au deplacement vers les adjacents ouverts
	 *  	Sinon vers la recherche d'un deplacement vers le plus proche noeud ouvert non voisin
	 * @param successeurs_non_visites Liste des adjacents non visité
	 * @return prochain deplacement
	 */
	public String getNextPosition(List<String> successeurs_non_visites ){
		String next_pos=((mas.abstractAgent)myAgent).getCurrentPosition();
		if(!successeurs_non_visites.isEmpty()){
			next_pos =  choisirLeProchainNodeOuvert(successeurs_non_visites);
		}else{
			String myPosition = ((mas.abstractAgent)this.myAgent).getCurrentPosition();
			next_pos = getNextPositionNearestOpenVertex(myPosition);
		}
		return next_pos;
	}
	
	
	
	public List<String> get_open_neighbors(List<String> adj_names){
		//Completer les noeuds ouverts et determiner l'ensemble des successeurs potentiels
		List<String> successeurs_non_visites = new ArrayList<String>();
		for(String adj: adj_names){
			if(!this.fermes.contains(adj)){
				this.ouverts.add(adj);
				successeurs_non_visites.add(adj);
			}
		}
		return successeurs_non_visites;
	}
	
	
	public boolean done() {
		return this.finished;
	}
	
	 // le Behavior s'arrête
    public int onEnd() {
      return this.onEndValue;
    } 

}
