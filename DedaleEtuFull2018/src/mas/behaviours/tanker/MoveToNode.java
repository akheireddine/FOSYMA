package mas.behaviours.tanker;


import java.util.List;

import env.Attribute;
import env.Couple;
import jade.lang.acl.ACLMessage;
import mas.agents.AK_Agent;
import mas.agents.AK_Tanker;
import mas.behaviours.GWalkBehaviour;
import tools.GraphAK;

public class MoveToNode extends GWalkBehaviour {

	private static final long serialVersionUID = -2386922834587307954L;

	public MoveToNode(final mas.abstractAgent myagent, GraphAK g) {
		super(g,g.getFermes(),g.getOuverts(),myagent);
		G.clearFermes();
		this.fermes.add(myagent.getCurrentPosition());
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
		System.out.println("\n********************************** SILO **********************************\n");

		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			
			try {
//				System.in.read();
				Thread.sleep(princ.Principal.SPEED_AGENT+50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition

			ouverts.remove(myPosition);
			fermes.add(myPosition);

			
			List<Couple<String, List<Attribute>>> adjacents = lobs;
			m_a_j_graphe(adjacents);
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
			String goal_agent = ((AK_Tanker)myAgent).goal;
		
			if(myPosition.equals(goal_agent)) {
//####################################### NEW ###################################
				this.finished=true;
				this.onEndValue=1;
				((AK_Tanker)myAgent).goal = "";
				return;
			}
			
			ACLMessage get_msg = ((AK_Agent)myAgent).getMessage();

			String next_pos = next_position_to_goal(myPosition);
			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
				
			if (has_moved){
				this.onEndValue = -1;
				this.finished=false;
				((AK_Agent)myAgent).setNombreDeCollision(0);
			}
			
			else{
				
				ouverts.remove(myPosition);
				fermes.add(myPosition);
				
				System.out.println(myAgent.getLocalName()+" : Can't move from "+myPosition+" to "+next_pos);
				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
//				Set<String> detect_golem = G.isGolemAround(myPosition);
				
//					
				//Si premiere collision, envoie un message d'information
//				boolean golem_is_here = false;

				((AK_Tanker)myAgent).goal=next_position_to_goal(myPosition);
				
				
				if(nb_collision == 2 && get_msg==null){
					this.onEndValue = 1;
					this.finished=true;
				}
				
				else{
					this.onEndValue = 0;
					this.finished = true;
				}
				
			
//				if(!detect_golem.isEmpty() && get_msg==null)
//					golem_is_here = true;

				
//				if(golem_is_here){
//					G.clearFermes();
//					G.addAllOuverts(myPosition);
//					ouverts.remove(next_pos);
//					fermes.add(next_pos);
//					System.out.println(myAgent.getLocalName()+" (G): Have to restart my exploration.");
//
//				}
			}
			((AK_Agent)myAgent).setLastMove(next_pos);
			((AK_Agent)myAgent).setToread(null);
		
		}
	}


	
}
