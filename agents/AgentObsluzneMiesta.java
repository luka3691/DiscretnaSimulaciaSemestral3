package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class AgentObsluzneMiesta extends Agent
{
	public AgentObsluzneMiesta(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerObsluzneMiesta(Id.managerObsluzneMiesta, mySim(), this);
		new Scheduler1(Id.scheduler1, mySim(), this);
		new ProcesObsluhy(Id.procesObsluhy, mySim(), this);
		addOwnMessage(Mc.uvolneniePredajne);
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.startObednejPrestavky);
	}
	//meta! tag="end"
}
