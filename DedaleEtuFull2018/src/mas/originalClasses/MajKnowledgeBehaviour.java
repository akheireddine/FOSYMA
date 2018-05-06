package mas.originalClasses;

import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;

import env.Attribute;
import env.Couple;
import Tools.GraphAK;
import Tools.Tools;
import mas.agents.AK_Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class MajKnowledgeBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -6233943134155011042L;


	private GraphAK curr_graph;

	
	void createOrMAJTreasurNode(String s, List<Attribute> lattribute){
		if (!this.curr_graph.containsVertex(s)){  //Nouveau noeud
			this.curr_graph.addVertex(s,lattribute);
		}
		
		else{
			List<Attribute> curr_lattribute = curr_graph.getAttrOfNode(s);
			Couple<Float, Integer> couple_curr = Tools.getCoupleTreasurIndex(curr_lattribute);
			float curr_value = couple_curr.getLeft();
			int indexCurr = couple_curr.getRight();
			float value = Tools.getValueTreasurDiamond(lattribute);
	
			if(value < curr_value ){
				Attribute tresorValue = curr_lattribute.get(indexCurr);
				tresorValue.setValue(value);
				curr_lattribute.set(indexCurr, tresorValue);
			}
		}
	}
	
	
	public void action() {
		
		final ACLMessage received_graph = ((AK_Agent)myAgent).getMessage();
		try {
			//recuperer le graphe contenu dans le message
			if(!((AK_Agent)myAgent).getDoneExploration()){
				this.curr_graph = ((AK_Agent)myAgent).getGraph();
				GraphAK partial_graph = (GraphAK) received_graph.getContentObject();
				Set<DefaultEdge> edges = partial_graph.edgeSet(); 
				for(DefaultEdge e : edges){   //creation d'arete				
					String s = partial_graph.getEdgeSource(e);
					String t = partial_graph.getEdgeTarget(e);
					//Ajouter les nouveaux noeuds OU mettre a jour les valeurs des tresors contenu dans les noeuds
					createOrMAJTreasurNode(s,partial_graph.getAttrOfNode(s));
					createOrMAJTreasurNode(t,partial_graph.getAttrOfNode(t));
					//Si arete non existante, ajouter la
					if(!this.curr_graph.containsEdge(s,t) || !this.curr_graph.containsEdge(t, s))
//						this.curr_graph.addEdge(s,t,((mas.abstractAgent)this.myAgent).getCurrentPosition(),this.myAgent.getLocalName());
				System.out.println(myAgent.getLocalName()+" : MàJ GRAPHE");
				}
			}
			else{
//				HashMap<String, List<Attribute>> curr_nodes = this.curr_graph.getHashNode();
//				HashMap<String, List<Attribute>> partial_nodes = (HashMap<String, List<Attribute>>) received_graph.getContentObject();
//				for(String key:partial_nodes.keySet()){ //A quel moment peut on remplacer les infos courrante 
//					curr_nodes.replace(key,partial_nodes.get(key));
//				}
				System.out.println(myAgent.getLocalName()+" : MàJ Attribute Nodes");
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
	}

}












