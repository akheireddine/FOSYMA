package mas.behaviours.tanker;

import java.util.List;
import java.util.Set;

import mas.agents.AK_Agent;
import mas.behaviours.GWalkBehaviour;
import tools.GraphAK;
import env.Attribute;
import env.Couple;
import jade.lang.acl.ACLMessage;

public class TWalkBehaviour extends GWalkBehaviour {

	private static final long serialVersionUID = -5308185370371990470L;
	
	public TWalkBehaviour(final mas.abstractAgent myagent, GraphAK g) {
		super(g,g.getFermes(),g.getOuverts(),myagent);
	}
		
		
		
		
	public void action() {
		//Example to retrieve the current position
		String myPosition=((mas.abstractAgent)this.myAgent).getCurrentPosition();

		if (myPosition!=""){
			//List of observable from the agent's current position
			List<Couple<String,List<Attribute>>> lobs=((mas.abstractAgent)this.myAgent).observe();//myPosition

			try {
//					System.in.read();
				Thread.sleep(100);
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

			if(this.ouverts.isEmpty() ){
				if(((AK_Agent)myAgent).getCpt() > 0){
					((AK_Agent)myAgent).exploration_is_done(); //___________________!!! A REVOIR !!!___________________________
					System.out.println(myAgent.getLocalName()+" : Exploration DONE ("+((AK_Agent)myAgent).getCpt()+"). Restart !");
					this.finished = true;
					this.onEndValue = -1;
					return;
				}
				System.out.println("why here");
				G.clearFermes();      
				G.addAllOuverts(myPosition);
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).RAZCpt();
				voisins_ouverts = get_open_neighbors(adj_names);
			}
			
			


			String next_pos = getNextPosition(voisins_ouverts);

			ACLMessage get_msg = ((AK_Agent)myAgent).getMessage();
			int nb_collision = ((AK_Agent)myAgent).getNombreDeCollision();
			boolean has_moved = ((mas.abstractAgent)this.myAgent).moveTo(next_pos);


			if (has_moved){
				this.finished=false;
				((AK_Agent)myAgent).setNombreDeCollision(0);
				((AK_Agent)myAgent).CptPlus();
			}
			else{
				
				
				ouverts.remove(myPosition);
				fermes.add(myPosition);
				
				System.out.println(myAgent.getLocalName()+" : cant move to "+next_pos+" curr pos : "+myPosition);
				nb_collision = ((AK_Agent)myAgent).getNombreDeCollision()+1;
				((AK_Agent)myAgent).setNombreDeCollision(nb_collision);
				
				Set<String> detect_golem = G.isGolemAround(myPosition);
				
//				
				//Si premiere collision, envoie un message d'information
				boolean golem_is_here = false;
//				if(nb_collision==1 ) {
//					this.onEndValue = 0;
//					this.finished=true;
//
//				}
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