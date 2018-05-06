package mas.agents;


import java.util.HashSet;
import java.util.Set;

import Tools.GraphAK;
import jade.lang.acl.ACLMessage;
import mas.abstractAgent;

public abstract class AK_Agent extends abstractAgent {

	private static final long serialVersionUID = -383346975870383010L;
	private GraphAK G = new GraphAK();
	private ACLMessage toread = null; /** If there's something to read **/
	private int nombreDeCollision = 0;
	private boolean doneExploration = false;
	private int cpt_exploration = 0;
	private String recent_collision_node="";
	private Set<String> removedVerticesName=new HashSet<String>();
	static int nb_ak_agent = 0;

	public GraphAK getGraph(){
		return G;
	}
	
	public void CptPlus(){
		this.cpt_exploration ++;
	}
	public void RAZCpt(){
		this.cpt_exploration = 0;
	}
	public int getCpt(){
		return this.cpt_exploration;
	}
	
	public String getRecentCollisionNode() {
		return this.recent_collision_node;
	}
	
	public boolean getDoneExploration(){
		return this.doneExploration;
	}
	
	public void exploration_is_done(){
		this.doneExploration  = true;
	}

	public void setCollisionNode(String next_pos) {
		this.recent_collision_node = next_pos;
	}
	
	public void setToread(ACLMessage msg){
		this.toread = msg;
	}
	
	public ACLMessage getMessage(){
		return toread;
	}
	
	public int getNombreDeCollision() {
		return nombreDeCollision;
	}

	public void setNombreDeCollision(int nombreDeCollision) {
		this.nombreDeCollision = nombreDeCollision;
	}



	public void removeVertexCollision(String next_pos) {
		this.removedVerticesName.add(next_pos);
		this.G.removeVertex(next_pos);
	}

	public void resetVerticesToGraph() {
		this.G.resetVertices(this.removedVerticesName);
//		if(!this.removedVerticesName.isEmpty()) {
//			for(String i:this.removedVerticesName){
//				this.G.resetVertex(i);
//			}
////			this.G.getOuverts().add(this.removedVertexName);
////			this.G.getFermes().remove(this.removedVertexName);
//		}
		this.removedVerticesName.clear();;
	}

}
