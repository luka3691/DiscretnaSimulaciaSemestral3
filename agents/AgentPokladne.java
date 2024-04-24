package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="6"
public class AgentPokladne extends Agent
{
	public AgentPokladne(int id, Simulation mySim, Agent parent)
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
		new ManagerPokladne(Id.managerPokladne, mySim(), this);
		new Scheduler2(Id.scheduler2, mySim(), this);
		new ProcesPlatenia(Id.procesPlatenia, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.startObednejPrestavky);
		addOwnMessage(Mc.platenieUPokoladne);
	}
	//meta! tag="end"
}
