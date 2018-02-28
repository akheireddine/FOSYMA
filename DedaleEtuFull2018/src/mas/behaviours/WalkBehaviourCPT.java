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
import jade.core.behaviours.TickerBehaviour;
import jade.util.leap.HashSet;

public class WalkBehaviourCPT extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1441296002023862551L;
	private List<String> fermes,ouverts;
//	List<String> goal = new ArrayList<String>();
	private Graph<String, DefaultEdge> G;
	
	
	public WalkBehaviourCPT (final mas.abstractAgent myagent) {
		super(myagent, 1000);
		fermes = new ArrayList<String>();
		ouverts = new ArrayList<String>();
		G = new SimpleGraph<>(DefaultEdge.class);
	}


	public void onTick() {
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


			Boolean repeat = false;
			Couple<String,List<Attribute>> curr_pos = lobs.remove(0);
			String name_curr_node = curr_pos.getLeft();
			String next_pos = name_curr_node;
			
			
			while(repeat){
				Boolean d = false;
				List<String> open_adj = new ArrayList<String>();
				List<String> open_candidats = new ArrayList<String>();
				repeat = false;
				
				next_pos = name_curr_node;
				
				if(!G.containsVertex(name_curr_node)){  //Creation du noeud courrant
					G.addVertex(name_curr_node);
				}
				
				for(Couple<String,List<Attribute>> adj : lobs){  // parcourir les noeuds adjacents
					String name_adj = adj.getLeft();
					if(!G.containsVertex(name_adj)){     //creation noeud adj
						G.addVertex(name_adj);
						G.addEdge(name_curr_node,name_adj);   // creation de l'arc
					}
					if( !fermes.contains(name_adj)){
						if(!ouverts.contains(name_adj)){
							ouverts.add(name_adj);
							d = true;
							open_adj.add(name_adj);
						}
					}
				}
				
				if(d){
					Random r = new Random();
					next_pos = open_adj.get(r.nextInt(open_adj.size()));
				}
				else if (!d && !ouverts.isEmpty()){
					Random r = new Random();
					goal = new DijkstraShortestPath<String, DefaultEdge>(G).getPath(name_curr_node,ouverts.get(r.nextInt(ouverts.size()))).getVertexList();;
//G.shortestPath(name_curr_node,open_candidats.get(r.nextInt(open_candidats.size())));
					next_pos = goal.remove(0);
				}else if (!d && ouverts.isEmpty()){
					fermes.clear();
					repeat = true;
				}
			}
			((mas.abstractAgent)this.myAgent).moveTo(next_pos);
			
			
		}

	}

}