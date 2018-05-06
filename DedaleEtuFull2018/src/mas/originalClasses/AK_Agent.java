package mas.originalClasses;


import Tools.GraphAK;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import env.EntityType;
import env.Environment;
import mas.abstractAgent;

public class AK_Agent extends abstractAgent {

	private static final long serialVersionUID = -5475387134737498221L;
	private GraphAK G = new GraphAK();
	private ACLMessage toread = null; /** If there's something to read **/
	private int nombreDeCollision = 0;
	private boolean doneExploration = false;

	/*POUR DU ONESHOT IMPORTANT DE CONNAITRE L'ENSEMBLE DES OUVERTS ET FERMES*/
	public GraphAK getGraph(){
		return G;
	}
	
	public boolean getDoneExploration(){
		return this.doneExploration;
	}
	
	public void setExplorationDone(){
		this.doneExploration  = true;
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

	
	protected void setup(){
		
		super.setup();
		

		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("explorer");
		sd.setName(getLocalName());
		dfd.addServices(sd);
		try{
			DFService.register(this,dfd);
		}catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);

		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		addBehaviour(new FSMBehaviourDRC(this));
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}


	/**
	 * This method is automatically called after doDelete()
	 */
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
