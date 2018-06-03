package mas.behaviours.collector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mas.abstractAgent;
import mas.agents.AK_Agent;
import mas.agents.AK_Collector;
import mas.behaviours.GWalkBehaviour;
import tools.DFDServices;
import tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class CWalkBehaviour extends GWalkBehaviour {
	
	private static final long serialVersionUID = -5308185370371990470L;


	public CWalkBehaviour(abstractAgent myagent,GraphAK g) {
		super(g,g.getFermes(),g.getOuverts(),myagent);
	}


		
		
	public boolean isThereAnyTreasur(String myPosition){
		String type = ((mas.abstractAgent)myAgent).getMyTreasureType();
		Attribute a = G.containsTreasur(myPosition, type);
		if(a != null){
//			final int capacityBag = ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace();
//			if(capacityBag > (int)a.getValue()) {
				((mas.abstractAgent)this.myAgent).pick();
				((AK_Collector)myAgent).setPicked(true);
				return true;
//			}
		}
		return false;
	}
	
	
	public boolean throwMyTreasur() {
		AID[] sellerAgents = DFDServices.getAgentsByService("silo",myAgent);
		if(sellerAgents != null)
			for(AID agt : sellerAgents) {
				return ((mas.abstractAgent)myAgent).emptyMyBackPack(agt.getLocalName());
			}
		return false;
	}
	
	public String centering(){
		Set<String> nodes = new HashSet<String>(G.getOuverts());
		nodes.addAll(G.getFermes());
//		for (String node :  G.getDictAdjacences().keySet())
//			nodes.addAll(G.getDictAdjacences().get(node));
//		
		
//		Set<String> nodes = G.getHashNode().keySet();
		String src = "";// nodes.iterator().next();
		int max = 0; //G.degreeOf(src);
		for(String v : nodes){
			if(G.degreeOf(v) > max){
				max = G.degreeOf(v);
				src = v;
			}
		}
		
		for(String v : nodes){
			if(G.degreeOf(v) == max && src.compareTo(v)<0){
				src = v;
			}
		}
		
		return src;
	}
	
	
	public void action() {
		System.out.println("\n****************************** COLLECTOR "+myAgent.getLocalName()+" ******************************\n");

		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		
		if (myPosition!=""){
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			try {
//					System.in.read();
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
//			((AK_Agent)myAgent).setLastMove(myPosition);      //Mis là Manu
			
			ouverts.remove(myPosition);
			fermes.add(myPosition);
			
			
			
			List<Couple<String, List<Attribute>>> adjacents = lobs;
			List<String> adj_names = m_a_j_graphe(adjacents);
			
			String next_pos = "";
			int nb_collision=0;
			ACLMessage get_msg = null;

			
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(this.isThereAnyTreasur(myPosition)){
				adjacents =((mas.abstractAgent)this.myAgent).observe();
				adj_names = m_a_j_graphe(adjacents);
//				G.updateTreasure(node);
//				G.updateNode(myPosition, lobs.ge); 
				System.out.println(myAgent.getLocalName()+" : j'ai trouve un tresor, picked it!");
			}
			
			((AK_Collector)myAgent).goal= G.chooseTreasureToPick(myPosition, ((mas.abstractAgent)myAgent).getMyTreasureType(), ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
			
			
			
			if(( (((AK_Collector)myAgent).goal.equals("") && ((AK_Collector)myAgent).iPicked()) )   || ((AK_Collector)myAgent).goal.equals(myPosition)){
				((AK_Collector)myAgent).goal = this.centering();
			}
				
			


			
			if(((AK_Collector)myAgent).goal.equals("") ){
			
	
				List<String> voisins_ouverts = get_open_neighbors(adj_names);
	
				if(this.ouverts.isEmpty() ){
					if(((AK_Agent)myAgent).getCpt() > 0)
						((AK_Agent)myAgent).exploration_is_done(); //___________________!!! A REVOIR !!!___________________________
					System.out.println(myAgent.getLocalName()+" : Maybe Exploration is COMPLETE ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
					G.clearFermes();      
					G.addAllOuverts(myPosition);
					((AK_Agent)myAgent).setNombreDeCollision(0);
					((AK_Agent)myAgent).RAZCpt();
					voisins_ouverts = get_open_neighbors(adj_names);
				}
				
				
	
	
				next_pos = getNextPosition(voisins_ouverts);
				nb_collision = ((AK_Agent)myAgent).getNombreDeCollision();
				
			}
		//#################################################################################################################
			else{
				System.out.println(myAgent.getLocalName() +  " : EN AVANT VERS __________________________________________ : " + ((AK_Collector)myAgent).goal);
				next_pos = chemin_vers_goal(myPosition,((AK_Collector)myAgent).goal);
			}
			
			
			
			
			
			if(throwMyTreasur()){
				System.out.println(myAgent.getLocalName()+" : i throwed it");
				((AK_Collector)myAgent).setPicked(false);
				((AK_Collector)myAgent).goal="";
			}
			
			
			
			
			
			
			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);


			if (has_moved){
				this.finished=false;
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).CptPlus();
			}
			else{
				
				get_msg = ((AK_Agent)myAgent).getMessage();

				ouverts.remove(myPosition);
				fermes.add(myPosition);
				
				System.out.println(myAgent.getLocalName()+" : cant move to "+next_pos+" curr pos : "+myPosition);
				nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				Set<String> detect_golem = G.isGolemAround(myPosition);
				
				boolean golem_is_here = false;

				if(nb_collision == 2 && get_msg==null){
					this.onEndValue = 1;
					this.finished=true;
				}
				
				else{
					this.onEndValue = 0;
					this.finished = true;
				}
				
				if(!detect_golem.isEmpty() && get_msg==null)
					golem_is_here = true;

				
				if(golem_is_here){
					G.clearFermes();
					G.addAllOuverts(myPosition);
					ouverts.remove(next_pos);
					fermes.add(next_pos);
					System.out.println(myAgent.getLocalName()+" (G): Have to restart my exploration.");

				}
			}
			((AK_Agent)myAgent).setLastMove(next_pos);       //DEPLACE D'ICI
			((AK_Agent)myAgent).setToread(null);
			// A DECOMMENTER
//			System.out.println(myAgent.getLocalName()+" : current_pos "+myPosition+" goal "+((AK_Collector)myAgent).goal+"\n\t Current capacity	"+((mas.abstractAgent)this.myAgent).getBackPackFreeSpace());
//			System.out.println(myAgent.getLocalName()+" ("+myPosition+","+next_pos+")::::\nfermes : "+this.fermes+"\nouverts : "+this.ouverts);
	
		}

	}		
	
	
}