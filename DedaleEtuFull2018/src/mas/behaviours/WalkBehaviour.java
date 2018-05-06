package mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import env.Attribute;
import env.Couple;
import jade.core.behaviours.SimpleBehaviour;

public class WalkBehaviour extends SimpleBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5308185370371990470L;
	private List<String> fermes,ouverts;
	private Graph<String, DefaultEdge> G;
	private Boolean finished=false;
	
	public WalkBehaviour(final mas.abstractAgent myagent) {
		super(myagent);
		fermes = new ArrayList<String>();
		ouverts = new ArrayList<String>();
		G = new SimpleGraph<>(DefaultEdge.class);
	}


	
	public List<String> getAdjacents(List<Couple<String,List<Attribute>>> lobs){
		List<String> adjacents = new ArrayList<String>();
		lobs.remove(0);
		for(Couple<String,List<Attribute>> couple_adj:lobs){
			adjacents.add(couple_adj.getLeft());
		}
		return adjacents;
		
	}
	
	
	public void majGraph(String src, List<String> adjacents){
		G.addVertex(src);
		for(String adjacent: adjacents){
			G.addVertex(adjacent);
			G.addEdge(src, adjacent);
		}
	}
	
	public String getNextPositionNearestOpenVertex(String src){
		
		DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<String, DefaultEdge>(G);
		int dist_min = G.vertexSet().size();
		String next_pos = "";
		
		for(String dst: ouverts){
			List<String> shortestPath = dijkstraShortestPath.getPath(src,dst).getVertexList();
			if(shortestPath.size() < dist_min){
				dist_min = shortestPath.size();
				next_pos = shortestPath.get(1);
			}
		}
		return next_pos;
	}
	
	
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			System.out.println(this.myAgent.getLocalName()+" -- list of observables: "+lobs);

//			//Little pause to allow you to follow what is going on
			try {
				System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////

			//list of attribute associated to the currentPosition
			List<Attribute> lattribute= lobs.get(0).getRight();

			
			//example related to the use of the backpack for the treasure hunt
			Boolean b=false;
			
			for(Attribute a:lattribute){
				switch (a) {
				case TREASURE:
					System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
					System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("Value of the treasure on the current position: "+a.getValue());
					System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
					System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
					System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
					b=true;
					//Little pause to allow you to follow what is going on
					try {
						System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
						System.in.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			case DIAMONDS:
				System.out.println("My type is : "+((mas.abstractAgent)this.myAgent).getMyTreasureType());
				System.out.println("My current backpack capacity is:"+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("Value of the diamonds on the current position: "+a.getValue());
				System.out.println("The agent grabbed :"+((mas.abstractAgent)this.myAgent).pick());
				System.out.println("the remaining backpack capacity is: "+ ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
				System.out.println("The value of treasure on the current position: (unchanged before a new call to observe()): "+a.getValue());
				b=true;
				//Little pause to allow you to follow what is going on
				try {
					System.out.println("Press Enter in the console to allow the agent "+this.myAgent.getLocalName() +" to execute its next move");
					System.in.read();
				} catch (IOException e) {
					e.printStackTrace();
				}
				default:
					break;
				}
			}
			//If the agent picked (part of) the treasure
			if (b){
				List<Couple<String,List<Attribute>>> lobs2=((mas.abstractAgent)this.myAgent).observe();//myPosition
				System.out.println("list of observables after picking "+lobs2);
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			//repeter la meme action
			if(ouverts.isEmpty())
				fermes.clear();
			
			if(ouverts.contains(myPosition))
				ouverts.remove(myPosition);

			fermes.add(myPosition);
			List<String> adjacents = getAdjacents(lobs);
			majGraph(myPosition, adjacents);
			
			//Completer les noeuds ouverts et determiner l'ensemble des successeurs potentiels
			List<String> successeurs_non_visites = new ArrayList<String>();
			for(String adj: adjacents){
				if(!fermes.contains(adj)){
					if(!ouverts.contains(adj)){
						ouverts.add(adj);
					}
					successeurs_non_visites.add(adj);
				}
			}
			
			String next_pos = "";
			if(!successeurs_non_visites.isEmpty()){
				next_pos = successeurs_non_visites.get(new Random().nextInt(successeurs_non_visites.size()));
			}
			
			else{
				next_pos = getNextPositionNearestOpenVertex(myPosition);
			}
			
			
			((mas.abstractAgent)this.myAgent).moveTo(next_pos);

		}
	}

	public boolean done() {
//		if(ouverts.isEmpty())
//			this.finished = true;
		return this.finished;
	}

}