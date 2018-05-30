package mas.agents;


import jade.domain.DFService;
import jade.domain.FIPAException;


import env.EntityType;
import env.Environment;
import mas.behaviours.tanker.TFSMBehaviour1;
import tools.DFDServices;


public class AK_Tanker extends AK_Agent{


	private static final long serialVersionUID = -1593633423403750683L;
	public String goal = "";
	public int mv = 0;
	
	public void noeud_suivant() {
		mv++;
		if(G.siloPositions().size()!=0)
			goal = G.changer_de_noeud_silo(mv%G.siloPositions().size());
	}
	
	
	

	protected void setup(){
		super.setup();
		DFDServices.registerToService("silo", this);

		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}

		//Add the behaviours
		addBehaviour(new TFSMBehaviour1(this));
		System.out.println("the agent "+this.getLocalName()+ " is started");

	}

	protected void takeDown(){
		try{
			DFService.deregister(this);
		}catch(FIPAException fe) {
			fe.printStackTrace();
		}
	}
}

