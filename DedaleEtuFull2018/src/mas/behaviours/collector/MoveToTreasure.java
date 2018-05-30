package mas.behaviours.collector;

import java.util.List;
import java.util.Set;

import mas.agents.AK_Agent;
import mas.agents.AK_Tanker;
import mas.behaviours.GWalkBehaviour;
import tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.lang.acl.ACLMessage;

public class MoveToTreasure extends GWalkBehaviour {


	private static final long serialVersionUID = -8925345780958073775L;


	public MoveToTreasure(final mas.abstractAgent myagent, GraphAK g) {
		super(g,g.getFermes(),g.getOuverts(),myagent);

	}
	
	
	public String next_position_to_goal(String myPosition) {
		String next_pos = myPosition;

		String goal_agent = ((AK_Tanker)myAgent).goal;
		if(goal_agent.equals("")) {
			((AK_Tanker)myAgent).noeud_suivant();
			goal_agent = ((AK_Tanker)myAgent).goal;
			
			if(myPosition.equals(goal_agent)) {
				((AK_Tanker)myAgent).noeud_suivant();
				goal_agent = ((AK_Tanker)myAgent).goal;
			}
			next_pos = chemin_vers_goal(myPosition,goal_agent);
		}else {
			next_pos = chemin_vers_goal(myPosition,goal_agent);
		}
		
		return next_pos;
	}
	

	@Override
	public void action() {
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){

			
			try {
//				System.in.read();
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition
			List<Couple<String, List<Attribute>>> adjacents = lobs;
			m_a_j_graphe(adjacents);
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
//			String goal_agent = ((AK_A)myAgent).goal;
		
			if(myPosition.equals(goal_agent)) {
				try {
	//				System.in.read();
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				((AK_Tanker)myAgent).goal = "";
			}
			ACLMessage get_msg = ((AK_Agent)myAgent).getMessage();

			String next_pos = next_position_to_goal(myPosition);
			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
				
			if (has_moved){
				((AK_Agent)myAgent).setNombreDeCollision(0);
			}
			
			else{
				ouverts.remove(myPosition);
				fermes.add(myPosition);
				
				System.out.println(myAgent.getLocalName()+" : cant move to "+next_pos+" curr pos : "+myPosition);
				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				Set<String> detect_golem = G.isGolemAround(myPosition);
				
//					
				//Si premiere collision, envoie un message d'information
				boolean golem_is_here = false;
//					if(nb_collision==1 ) {
//						this.onEndValue = 0;
//						this.finished=true;
//
//					}
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
			((AK_Agent)myAgent).setLastMove(next_pos);
			((AK_Agent)myAgent).setToread(null);
		
		}
	}


	
}