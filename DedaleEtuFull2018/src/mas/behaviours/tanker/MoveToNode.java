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
		
			String goal_agent = ((AK_Tanker)myAgent).goal;
		
			if(myPosition.equals(goal_agent)) {
				try {
	//				System.in.read();
					Thread.sleep(2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				((AK_Tanker)myAgent).goal = "";
			}
			
			String next_pos = next_position_to_goal(myPosition);
			
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);
				
			if (has_moved){
				((AK_Agent)myAgent).setNombreDeCollision(0);
			}
			else{
				System.out.println(" cant move "+next_pos+" curr pos : "+myPosition);
				int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				ACLMessage get_msg = ((AK_Agent)myAgent).getMessage();
				
				//Si premiere collision, envoie un message d'information
				if(nb_collision==1 ) {
					this.onEndValue = 0;
					this.finished=true;
				}
				else if(nb_collision == 2 && get_msg==null){
					this.onEndValue = 1;
					this.finished=true;
					((AK_Tanker)myAgent).noeud_suivant();
				}
				else if(nb_collision > 2 ){
					((AK_Tanker)myAgent).noeud_suivant();
				}
				
				((AK_Agent)myAgent).setToread(null);

			}
		
		}
	}


	
}
