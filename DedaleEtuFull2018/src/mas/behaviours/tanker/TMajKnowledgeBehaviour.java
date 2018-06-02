package mas.behaviours.tanker;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import mas.agents.AK_Agent;

import org.jgrapht.alg.util.Pair;

import scala.Tuple5;
import tools.DFDServices;
import tools.GraphAK;
import env.Attribute;

public class TMajKnowledgeBehaviour extends OneShotBehaviour {

	

	private static final long serialVersionUID = -5665881630261730486L;
	private GraphAK G;
	
	
	
	public void action() {
		
		final ACLMessage received_graph = ((AK_Agent)myAgent).getMessage();
		this.G = ((AK_Agent)myAgent).getGraph();

		if(received_graph!=null){

			try {
				@SuppressWarnings("unchecked")
				Tuple5<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String,Pair<Attribute,Long>>> new_information = (Tuple5<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String,Pair<Attribute,Long>>>) received_graph.getContentObject();
				
				HashMap<String, List<Attribute>> info_nodes = new_information._1();
				
				//recuperer ma connaissance du graphe
				
				HashMap<String,Set<String>> adjacenes_received = new_information._2();
				HashMap<String,Pair<Attribute,Long>> info_treasures = new_information._5();
				
				for(String node : adjacenes_received.keySet()){
					if(!G.getDictAdjacences().containsKey(node)){
						G.getDictAdjacences().put(node, adjacenes_received.get(node));
						
						
						G.addVertex(node);//,info_nodes.get(node));
//						if(adjacenes_received.get(node) !=null){
							for(String adj: adjacenes_received.get(node)){
								G.addVertex(adj);//,info_nodes.get(adj));
								G.addEdge(node, adj);
							}
						
					}
					
//					if(info_treasures.containsKey(node))
//						G.maj_treasure(node,info_treasures.get(node));
				}
				
				

				for(String n_t: info_treasures.keySet()){
					G.maj_treasure(n_t, info_treasures.get(n_t));
				}
				
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				if(DFDServices.typeOfserviceAgent(received_graph.getSender(), myAgent, "collector")){
					String myPosition = ((mas.abstractAgent)this.myAgent).getCurrentPosition();
					System.out.println(myAgent.getLocalName()+" : face d'un COLLECTOR"+myPosition);
					Set<String> adjacens = G.getDictAdjacences().get(myPosition);
					G.getOuverts().addAll(adjacens);
					String pos_agent = ((AK_Agent)myAgent).getLastMove();
					G.getOuverts().remove(pos_agent);
					G.getFermes().removeAll(adjacens);
					G.getFermes().add(myPosition);
					G.getOuverts().remove(myPosition);
					G.getFermes().add(pos_agent);
				}
				
				
				
				
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				this.G.updateOF(new_information._3(),new_information._4());
//				this.G.addToFermes(new_information._4());
//				((AK_Agent)myAgent).setToread(null);
//				System.out.println(myAgent.getLocalName()+" : MAJ");
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
	}




}
