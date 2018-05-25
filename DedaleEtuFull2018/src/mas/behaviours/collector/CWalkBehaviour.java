package mas.behaviours.collector;

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

public class CWalkBehaviour extends GWalkBehaviour {
	
	private static final long serialVersionUID = -5308185370371990470L;


	public CWalkBehaviour(GraphAK g, Set<String> f, Set<String> o, abstractAgent myagent) {
		super(g,g.getFermes(),g.getOuverts(),myagent);
	}


		
		
	public void isThereAnyTreasur(String myPosition,List<Attribute> curr_observation){
		if(G.containsTreasur(myPosition,((mas.abstractAgent)myAgent).getMyTreasureType())){
			final int capacityBag = ((mas.abstractAgent)this.myAgent).getBackPackFreeSpace();
			for(Attribute a:curr_observation){
				switch(a) {
				case TREASURE: case DIAMONDS :
					if(capacityBag > (int)a.getValue()) {
						((mas.abstractAgent)this.myAgent).pick();
						((AK_Collector)myAgent).setPicked(true);
					}
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	public boolean throwMyTreasur() {
		AID[] sellerAgents = DFDServices.getAgentsByService("silo",myAgent);
		for(AID agt : sellerAgents) {
			return ((mas.abstractAgent)myAgent).emptyMyBackPack(agt.getLocalName());
		}
		return false;
	}
	
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();
		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			try {
//					System.in.read();
				Thread.sleep(700);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			if(!G.containsVertex(myPosition))
				G.addVertex(myPosition, lobs.get(0).getRight());
			
			ouverts.remove(myPosition);
			fermes.add(myPosition);
			
			
			
			List<Couple<String, List<Attribute>>> adjacents = lobs;
			Couple<String, List<Attribute>> curr_observation = adjacents.remove(0);
			G.updateNode(myPosition, curr_observation.getRight());                      //MaJ des informations sur les noeuds
			
			
			//############################## SI J'OBSERVE UN TRESOR ############################
			this.isThereAnyTreasur(myPosition, curr_observation.getRight());

			
			if(((AK_Collector)myAgent).iPicked()) {//chercher le silo
				G.updateNode(myPosition, curr_observation.getRight());                      //MaJ des informations sur les noeuds
//					this.onEndValue = 1;
//					this.finished= true;
				if(throwMyTreasur()){
					System.out.println("i throwed it");
					((AK_Collector)myAgent).setPicked(false);
				}
				System.out.println(myAgent.getLocalName()+" : j'ai trouve un tresor, picked it!");
			}
			
			

			List<String> adj_names = m_a_j_graphe(myPosition, adjacents);
			List<String> voisins_ouverts = get_open_neighbors(adj_names);
			
			if(this.ouverts.isEmpty()){
				((AK_Agent)myAgent).exploration_is_done(); //Avertir quil a fini l'exploration, elle sera connu des autres agents
				G.clearFermes();       //Repeter l'operation d'exploration
				G.addAllOuverts(myPosition);
//					this.finished = true;
				((AK_Agent)myAgent).setNombreDeCollision(0);

				System.out.println(myAgent.getLocalName()+" : Exploration DONE ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
				((AK_Agent)myAgent).RAZCpt();
				voisins_ouverts = get_open_neighbors(adj_names);
			}

			String next_pos = getNextPosition(voisins_ouverts);
			if(next_pos.equals(myPosition) ){//|| (next_pos.equals(last_move) && (((AK_Agent)myAgent).getNombreDeCollision() > 2)) ) {
				G.clearFermes();
				((AK_Agent)myAgent).setNombreDeCollision(0);

				G.addAllOuverts(myPosition);
				System.out.println(myAgent.getLocalName()+" : I passed in the wosrt case cuz "+((AK_Agent)myAgent).getCpt());
				next_pos = getNextPosition(voisins_ouverts);
			}
			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
//				System.out.println(myAgent.getLocalName()+" : moved to "+next_pos+" "+has_moved+" ("+((AK_Agent)myAgent).getNombreDeCollision());

			if (has_moved){
				this.finished=false;
//					((AK_Agent)myAgent).setCollisionNode("toto");
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).CptPlus();
			}
			else{

				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				
				Set<String> golem = G.isGolemAround(myPosition);;

			
				//Dans le cas ou on echange les Ouverts, fermes
//					ouverts.remove(next_pos);
//					fermes.add(next_pos);
			
				//Si premiere collision, envoie un message d'information
				if(nb_collision==1 && golem.isEmpty()) {
					this.finished=true;
					ouverts.remove(next_pos);
					fermes.add(next_pos);
//						((AK_Agent)myAgent).setCollisionNode(next_pos);
				}

				else if (nb_collision == 2 && golem.isEmpty()){//check s'il a bien lu le msg recu par l'agent collision
					this.finished=true;
					this.onEndValue = 1;    
				}
				
//					else if(nb_collision>2 && !golem.isEmpty()){
//						ouverts.remove(next_pos);
//						fermes.add(next_pos);
//					}
				
				else if(nb_collision>2 || next_pos.equals(last_move)){
					G.clearFermes();
					((AK_Agent)myAgent).setNombreDeCollision(0);
					G.addAllOuverts(myPosition);
					System.out.println(myAgent.getLocalName()+" : I passed in the wosrt case cuzzz "+next_pos);
				}
				
		

				last_move=next_pos;

			}
		}
	}		
//		List<String> adj_names = m_a_j_graphe(myPosition, adjacents);
//		List<String> voisins_ouverts = get_open_neighbors(adj_names);
//		
//		if(this.ouverts.isEmpty()) {
//			String d = "";
//			if(((AK_Agent)myAgent).getNoCollisionSince()) {
//				((AK_Agent)myAgent).exploration_is_done(); //Avertir quil a fini l'exploration, elle sera connu des autres agents
//				d = "DONE";
//			}
//			G.clearFermes();       //Repeter l'operation d'exploration
//			G.addAllOuverts(myPosition);
//
//			System.out.println(myAgent.getLocalName()+" : Exploration "+d+" ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
//			((AK_Agent)myAgent).RAZCpt();
//			voisins_ouverts = get_open_neighbors(adj_names);
//		}
//		
//		String next_pos = getNextPosition(voisins_ouverts);
//		if(next_pos.equals("")) {
//			G.clearFermes();
//			G.addAllOuverts(myPosition);
//			System.out.println(myAgent.getLocalName()+" : I passed in the wosrt case cuz "+next_pos);
//			next_pos = getNextPosition(voisins_ouverts);
//		}
//		
//		boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
//		
//		if (has_moved){
//			this.finished=false;
//			((AK_Agent)myAgent).setCollisionNode("");
//			((AK_Agent)myAgent).setNombreDeCollision(0);
//			((AK_Agent)myAgent).CptPlus();
////			System.out.println(myAgent.getLocalName()+" : Deplace vers "+next_pos);
//		}
//		else{
//			
//			int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision();
//			((AK_Agent)myAgent).setNombreDeCollision(nb_collision+1);
//			
//			
//			Set<String> golem = G.isGolemAround(myPosition);;
//
//		
//			//Dans le cas ou on echange les Ouverts, fermes
//			ouverts.remove(next_pos);
//			fermes.add(next_pos);
//		
//			//################ Si premiere collision, envoie un message d'information ################
//			if(nb_collision==1 && golem.isEmpty()) {
//				this.finished=true;
//				this.onEndValue = 2;
////				((AK_Agent)myAgent).setCollisionNode(next_pos);
//			}
////			else if (nb_collision ==2 && !golem)
////				((AK_Agent)myAgent).setCollisionNode(next_pos);
//			//################ REVOIR SA BOITE AU LETTRE ################
//			else if (nb_collision == 2 && golem.isEmpty()){//check s'il a bien lu le msg recu par l'agent collision
//				this.finished=true;
//				this.onEndValue = 3;    
//			}
//			
//			else if (nb_collision>2 && !golem.isEmpty()) {
//				((AK_Agent)myAgent).setCollisionNode(next_pos);
//			}
//
//
//		}
//		
//
//		
//
//		
//
////		if (((AK_Agent)myAgent).getNombreDeCollision() == 5 && (!this.ouverts.isEmpty())){ //N'a qu'un seul noeud ouvert a explorer
////				G.clearFermes();
////				this.fermes.add(next_pos);
////				this.fermes.add(myPosition);
//////				this.onEndValue=1;
////				((AK_Agent)myAgent).setNombreDeCollision(0);
////				System.out.println("COLLISIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOooN");
////		}
//	}
//}

	
	
}