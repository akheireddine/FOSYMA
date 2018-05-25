package mas.agents;


import java.util.HashSet;
import java.util.Set;

import jade.lang.acl.ACLMessage;
import jade.util.leap.Serializable;
import mas.abstractAgent;
import tools.GraphAK;

public abstract class AK_Agent extends abstractAgent {

	private static final long serialVersionUID = -383346975870383010L;
	protected GraphAK G = new GraphAK();
	private ACLMessage toread = null; /** If there's something to read **/
	private boolean exploDone = false;
	private int cpt_exploration = 0;
	
	private int nombreDeCollision = 0;
	private String recent_collision_node="";
	private Set<String> removedVerticesName=new HashSet<String>();
	private boolean noCollisionSince= true;
	

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
	
	public Set<String> getSetRemovedVertices(){
		return this.removedVerticesName;
	}
	
	public boolean isExplorationDone(){
		return this.exploDone;
	}
	
	public boolean getNoCollisionSince() {
		return this.noCollisionSince;
	}
	public void exploration_is_done(){
		this.noCollisionSince = true;
		this.exploDone  = true;
	}

	public void setCollisionNode(String next_pos) {
		if(!next_pos.equals(""))
			this.noCollisionSince= false;
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
	
	
	public abstract Serializable getObjectToSend();

//	public void resetVerticesToGraph() {
//		this.G.resetVertices(this.removedVerticesName);
////		if(!this.removedVerticesName.isEmpty()) {
////			for(String i:this.removedVerticesName){
////				this.G.resetVertex(i);
////			}
//////			this.G.getOuverts().add(this.removedVertexName);
//////			this.G.getFermes().remove(this.removedVertexName);
////		}
//		this.removedVerticesName.clear();;
//	}

}
