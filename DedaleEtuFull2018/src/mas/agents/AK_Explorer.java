package mas.agents;



import Tools.DFDServices;
import jade.domain.DFService;
import jade.domain.FIPAException;
import env.EntityType;
import env.Environment;
import mas.behaviours.explorer.NewFSMBehaviourDSCM;

public class AK_Explorer extends AK_Agent {

	private static final long serialVersionUID = -1742937994368634241L;
	
	protected void setup(){
		
		super.setup();
		

		DFDServices.registerToService("explorer", this);
		
		//get the parameters given into the object[]. In the current case, the environment where the agent will evolve
		final Object[] args = getArguments();
		if(args!=null && args[0]!=null && args[1]!=null){
			deployAgent((Environment) args[0],(EntityType)args[1]);
		}else{
			System.err.println("Malfunction during parameter's loading of agent"+ this.getClass().getName());
			System.exit(-1);
		}
		
		boolean b = false;
		if (nb_ak_agent==0)
			b = true;
		nb_ak_agent ++;
		
		addBehaviour(new NewFSMBehaviourDSCM(this,b));

		System.out.println("the agent "+this.getLocalName()+ " is started with method "+b);

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