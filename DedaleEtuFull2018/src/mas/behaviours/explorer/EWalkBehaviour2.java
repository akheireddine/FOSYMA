package mas.behaviours.explorer;

import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Set;

import tools.GraphAK;
import env.Attribute;
import env.Couple;
import mas.agents.AK_Agent;
import mas.behaviours.GWalkBehaviour;

public class EWalkBehaviour2 extends GWalkBehaviour {

	private static final long serialVersionUID = 5174875974684725407L;




	public EWalkBehaviour2(final mas.abstractAgent myagent, GraphAK g) {
		super(g,g.getFermes(),g.getOuverts(),myagent);
	}


	@Override
	public String choisirLeProchainVoisinOuvertLePlusGrand(List<String> successors){
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
			List<String> adj_names = m_a_j_graphe(adjacents);
			List<String> voisins_ouverts = get_open_neighbors(adj_names);

			if(this.ouverts.isEmpty() && ((AK_Agent)myAgent).getCpt()>0){
				String b = "NOT DONE";
				if(((AK_Agent)myAgent).getNombreDeCollision()==0 || ((AK_Agent)myAgent).isExplorationDone()){
					((AK_Agent)myAgent).exploration_is_done(); //___________________!!! A REVOIR !!!___________________________
					b = "DONE";
				}
				
				G.clearFermes();      
				G.addAllOuverts(myPosition);
				((AK_Agent)myAgent).setNombreDeCollision(0);
				System.out.println(myAgent.getLocalName()+" : Exploration "+b+" ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
				((AK_Agent)myAgent).RAZCpt();
				voisins_ouverts = get_open_neighbors(adj_names);
			}
			


			String next_pos = getNextPosition(voisins_ouverts);

			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);


			if (has_moved){
				this.finished=false;
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).CptPlus();
			}
			else{

				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				Set<String> detect_golem = G.isGolemAround(myPosition);
				ACLMessage get_msg = ((AK_Agent)myAgent).getMessage();
				ouverts.remove(next_pos);
				fermes.add(next_pos);
				//Si premiere collision, envoie un message d'information
				boolean golem_is_here = false;
				if(nb_collision==1 ) {
					this.onEndValue = 0;
					this.finished=true;

				}
				else if(nb_collision == 2 && get_msg==null){
					this.onEndValue = 1;
					this.finished=true;
				}
				else if(!detect_golem.isEmpty() && get_msg==null)
					golem_is_here = true;

				else if(nb_collision > 2 && next_pos.equals(last_move)){
					G.clearFermes();
					G.addAllOuverts(myPosition);
					ouverts.remove(next_pos);
					fermes.add(next_pos);
					System.out.println(myAgent.getLocalName()+" (A): Have to restart my exploration.");
				}
				
				if(golem_is_here){
					G.clearFermes();
					G.addAllOuverts(myPosition);
					ouverts.remove(next_pos);
					fermes.add(next_pos);
					System.out.println(myAgent.getLocalName()+" (G): Have to restart my exploration.");

				}
				last_move=next_pos;
				((AK_Agent)myAgent).setToread(null);

			}

		}
	}

}
