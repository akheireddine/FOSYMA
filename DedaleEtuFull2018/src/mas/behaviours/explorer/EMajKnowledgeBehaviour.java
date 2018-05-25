package mas.behaviours.explorer;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import env.Attribute;
import mas.agents.AK_Agent;
import scala.Tuple4;
import tools.GraphAK;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class EMajKnowledgeBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -6233943134155011042L;

	private GraphAK curr_graph;
	
	
	
	public void action() {
		
		final ACLMessage received_graph = ((AK_Agent)myAgent).getMessage();
		if(received_graph!=null){
		try {
			//recuperer l'objet recu 
			Tuple4<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>> new_information = (Tuple4<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>>) received_graph.getContentObject();
			
			HashMap<String, List<Attribute>> info_nodes = new_information._1();
			
			//recuperer ma connaissance du graphe
			this.curr_graph = ((AK_Agent)myAgent).getGraph();
			
			//Si je n'ai pas fini mon exploration complete de l'environement
//			if(!((AK_Agent)myAgent).isExplorationDone()){
//				System.out.println(myAgent.getLocalName()+" : MàJ topo env");
			HashMap<String,Set<String>> adjacenes_received = new_information._2();
			
			
			for(String node : adjacenes_received.keySet()){
				if(!curr_graph.containsVertex(node)){
					curr_graph.addVertex(node,info_nodes.get(node));
//						curr_graph.getFermes().add(node);                              // Ce qu'a pu explorer l'agent sender, je ne l'explore plus
				}
				if(adjacenes_received.get(node) !=null){
					for(String adj: adjacenes_received.get(node)){
						curr_graph.addVertex(adj,info_nodes.get(adj));
						curr_graph.addEdge(node, adj);
					}
				}
			}
			//Sinon si j'ai tout explorer, je ne met a jour que les informations sur les noeuds
//			}else{
//				HashMap<String, List<Attribute>> curr_nodes = this.curr_graph.getHashNode();
//				for(String key:info_nodes.keySet()){ //A quel moment peut on remplacer les infos courrante 
//					curr_nodes.replace(key,info_nodes.get(key));
//				}
////				System.out.println(myAgent.getLocalName()+" : MàJ Attribute Nodes");
//			}
			
			this.curr_graph.addToFermes(new_information._4());
//			this.curr_graph.switchOF(new_information._3(),new_information._4());
			((AK_Agent)myAgent).setToread(null);
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}
	}

}





















//void createOrMAJTreasurNode(String s, List<Attribute> lattribute){
//	if (!this.curr_graph.containsVertex(s)){  //Nouveau noeud
//		this.curr_graph.addVertex(s,lattribute);
//	}
//	
//	else{
//		List<Attribute> curr_lattribute = curr_graph.getAttrOfNode(s);
//		Couple<Float, Integer> couple_curr = Tools.getCoupleTreasurIndex(curr_lattribute);
//		float curr_value = couple_curr.getLeft();
//		int indexCurr = couple_curr.getRight();
//		float value = Tools.getValueTreasurDiamond(lattribute);
//
//		if(value < curr_value ){
//			Attribute tresorValue = curr_lattribute.get(indexCurr);
//			tresorValue.setValue(value);
//			curr_lattribute.set(indexCurr, tresorValue);
//		}
//	}
//}





