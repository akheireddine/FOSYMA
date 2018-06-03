package mas.behaviours;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.DefaultEdge;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;
import mas.agents.AK_Agent;
import tools.GraphAK;

public abstract class GWalkBehaviour extends SimpleBehaviour {

	private static final long serialVersionUID = -3874406343681374104L;
	protected Set<String> fermes,ouverts;
	protected GraphAK G;
	protected int onEndValue=-1;
	protected boolean finished = false;

	
	
	
	
	public GWalkBehaviour(GraphAK g, Set<String> f, Set<String> o,final mas.abstractAgent myagent) {
		super(myagent);
		this.G = g;
		this.fermes= f;
		this.ouverts = o;
	}
	
	
	public String chemin_vers_goal(String src,String dest) {
		String next_node = src;
		if(!src.equals(dest)){
			DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
			List<String> shortestPath = dijkstraShortestPath.getPath(src,dest).getVertexList();
			next_node = shortestPath.get(1);
		}
		return next_node;
	}
	
	/***
	 * @param src position courante 
	 * @param adjacents : liste de couple contenant le nom du noeud et les informations sur ce noeud
	 * @return List<String> des noeuds adjacents
	 */
	public List<String> m_a_j_graphe(List<Couple<String, List<Attribute>>> adjacents){
		List<String> ladj_node = new ArrayList<String>();
		Couple<String, List<Attribute>> curr_observation = adjacents.remove(0);
		String myPosition = curr_observation.getLeft();
		
		G.getHashNode().put(myPosition, curr_observation.getRight());   //MAJ NODE              NE CONTIENT PAS TOUT APRES 21H
		
		G.updateTreasure(myPosition, curr_observation.getRight());     // A LA PLACE FAIRE
		
		
		G.addVertex(myPosition);

		
		for(Couple<String, List<Attribute>> adjacent: adjacents){
			String adj_name = adjacent.getLeft();
			G.addVertex(adj_name);
			G.addEdge(myPosition, adj_name);
			ladj_node.add(adj_name);
		}
		G.getDictAdjacences().put(myPosition, new HashSet<String>(ladj_node));       //MAJ DICT_ADJ
		return ladj_node;
	}
	
	
	/***
	 * @param src position courante 
	 * @return le prochain deplacement vers le noeud ouvert non exploré le plus proche(DIJKSTRA)
	 */
	
	public String getNextPositionNearestOpenVertexD(String src){
		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
		String next_node = "";//G.getDictAdjacences().get(src).iterator().next();
		int dist_min =	G.vertexSet().size();
//		String goal="";
		
		for(String dst: ouverts){
			
			try{
				List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
				if(shortestPath.size() <= dist_min ){
					dist_min = shortestPath.size();
					next_node = shortestPath.get(1);
//					goal = dst;
				}
			}catch(Exception e){
				System.out.println(myAgent.getLocalName()+": error______________________________________ "+src+" in "+dst+" \n\t"+G.vertexSet());
			}
		}
//		if(myAgent.getLocalName().equals("C1")){
////			System.out.println("ouverts : __________________" + ouverts);
//			System.out.println(myAgent.getLocalName()+" : goal _________________________________"+goal);
//		}
		return next_node;
	}
	

	
	public String getNextPositionNearestOpenVertexK(String src){
		KShortestSimplePaths<String,DefaultEdge> k_paths = new KShortestSimplePaths<String,DefaultEdge>(G);
		int max = 0;
		String next_pos = src;
		if(((AK_Agent)myAgent).pathToGoal.size() == 0 || ((AK_Agent)myAgent).getNombreDeCollision()>1) {
			((AK_Agent)myAgent).myGoal = "";
			((AK_Agent)myAgent).pathToGoal.clear();
		}
		if(((AK_Agent)myAgent).myGoal != "") {
			return ((AK_Agent)myAgent).pathToGoal.remove(0);
		}
		for(String dst : this.ouverts) {
			Set<GraphPath<String,DefaultEdge>> paths = new HashSet<GraphPath<String,DefaultEdge>>(k_paths.getPaths(src, dst,2));
			
			for(GraphPath<String,DefaultEdge>  l : paths){
				List<String> p = l.getVertexList();
				Set<String> sp = new HashSet<String>(p);
				sp.retainAll(this.ouverts);
				if(sp.size() > max){
//					System.out.println("LE chemin vers "+dst+" : "+p);
					p.remove(0);
					next_pos = p.remove(0);
					max = sp.size();
					((AK_Agent)myAgent).myGoal = dst;
					((AK_Agent)myAgent).pathToGoal = p;
//					System.out.println("AFTER LE chemin vers "+dst+" : "+p);

				}
			}
		}
		
		System.out.println(myAgent.getLocalName()+" : from "+src+" to goal "+((AK_Agent)myAgent).myGoal+" ("+max+")\n\t"+((AK_Agent)myAgent).pathToGoal);
//		System.out.println(myAgent.getLocalName()+" : my goal "+goal);
		return next_pos;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public String choisirLeProchainNodeOuvert(List<String> successors){
		String next_node = successors.get(0);
		int max = G.getNbOpenNeighborVertex(next_node);
		for(String succ : successors) {
			int value_tmp_node = G.getNbOpenNeighborVertex(succ);
			if(value_tmp_node > max) {
				max = value_tmp_node;
				next_node = succ;
			}
			
		}
		return next_node;
	}
	
	
	
	
	public String choisirLeProchainVoisinOuvertLePlusGrand(List<String> successors){
		String next_node = successors.get(0);
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
		String next_pos="";//((mas.abstractAgent)myAgent).getCurrentPosition();
		if(!successeurs_non_visites.isEmpty()){
			if(((AK_Agent)myAgent).isExplorationDone())
				next_pos = choisirLeProchainNodeOuvert(successeurs_non_visites);
			else
				next_pos =  choisirLeProchainVoisinOuvertLePlusGrand(successeurs_non_visites);
		}else{
			String myPosition = ((mas.abstractAgent)this.myAgent).getCurrentPosition();
			next_pos = getNextPositionNearestOpenVertexD(myPosition);
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
