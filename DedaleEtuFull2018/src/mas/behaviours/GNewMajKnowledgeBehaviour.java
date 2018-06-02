//package mas.behaviours;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Set;
//
//import org.jgrapht.alg.util.Pair;
//
//import env.Attribute;
//import mas.agents.AK_Agent;
//import scala.Tuple5;
//import tools.GraphAK;
//import jade.core.behaviours.OneShotBehaviour;
//import jade.lang.acl.ACLMessage;
//import jade.lang.acl.UnreadableException;
//
//public class GNewMajKnowledgeBehaviour extends OneShotBehaviour {
//
//	private static final long serialVersionUID = -6233943134155011042L;
//
//	private GraphAK curr_graph;
//	
//	
//	
//	public void action() {
//		
//		final ACLMessage received_graph = ((AK_Agent)myAgent).getMessage();
//		if(received_graph!=null){
//			try {
//				@SuppressWarnings("unchecked")
//				Tuple5<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String,Pair<Attribute,Long>>> new_information = (Tuple5<HashMap<String, List<Attribute>>, HashMap<String,Set<String>>,Set<String>,Set<String>,HashMap<String,Pair<Attribute,Long>>>) received_graph.getContentObject();
//				
//				HashMap<String, List<Attribute>> info_nodes = new_information._1();
//				
//				//recuperer ma connaissance du graphe
//				this.curr_graph = ((AK_Agent)myAgent).getGraph();
//				
//				//Si je n'ai pas fini mon exploration complete de l'environement
//	//			if(!((AK_Agent)myAgent).isExplorationDone()){
//	//				System.out.println(myAgent.getLocalName()+" : MàJ topo env");
//				HashMap<String,Set<String>> adjacenes_received = new_information._2();
//				HashMap<String,Pair<Attribute,Long>> info_treasures = new_information._5();
//				
//				for(String node : adjacenes_received.keySet()){
//					if(!curr_graph.containsVertex(node)){
//						curr_graph.addVertex(node,info_nodes.get(node));
//					}else{
//						if(info_treasures.containsKey(node)){
//							Pair<Attribute,Long> curr_treasure = curr_graph.getTreasures().get(node);
//						}
//					}
//					if(adjacenes_received.get(node) !=null){
//						for(String adj: adjacenes_received.get(node)){
//							curr_graph.addVertex(adj,info_nodes.get(adj));
//							curr_graph.addEdge(node, adj);
//						}
//					}
//				}
//				this.curr_graph.updateOF(new_information._3(),new_information._4());
////				this.curr_graph.switchOF(new_information._3(),new_information._4());
////				((AK_Agent)myAgent).setToread(null);
//				System.out.println(myAgent.getLocalName()+" : MAJ");
//			} catch (UnreadableException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////void createOrMAJTreasurNode(String s, List<Attribute> lattribute){
////	if (!this.curr_graph.containsVertex(s)){  //Nouveau noeud
////		this.curr_graph.addVertex(s,lattribute);
////	}
////	
////	else{
////		List<Attribute> curr_lattribute = curr_graph.getAttrOfNode(s);
////		Couple<Float, Integer> couple_curr = Tools.getCoupleTreasurIndex(curr_lattribute);
////		float curr_value = couple_curr.getLeft();
////		int indexCurr = couple_curr.getRight();
////		float value = Tools.getValueTreasurDiamond(lattribute);
////
////		if(value < curr_value ){
////			Attribute tresorValue = curr_lattribute.get(indexCurr);
////			tresorValue.setValue(value);
////			curr_lattribute.set(indexCurr, tresorValue);
////		}
////	}
////}
//
//
//
//
//
