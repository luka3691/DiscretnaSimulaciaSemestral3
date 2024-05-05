package agents;

import OSPABA.*;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="8"
public class AgentPredajna extends Agent
{
	public AgentPredajna(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		MyMessage sprava = new MyMessage(mySim());
		sprava.setAddressee(this);
		sprava.setCode(Mc.init);
		manager().notice(sprava);
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerPredajna(Id.managerPredajna, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.uvolnenieObsluzneho);
		addOwnMessage(Mc.zadavanieDoAutomatu);
		addOwnMessage(Mc.spatnePrevzatie);
		addOwnMessage(Mc.endObednejPrestavky);
		addOwnMessage(Mc.platenieUPokoladne);
		addOwnMessage(Mc.zablokovanieAutomatu);
		addOwnMessage(Mc.zakaznikVPredajni);
	}
	//meta! tag="end"
}