package managers;

import OSPABA.*;
import Osoby.StavyOsoby;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="2"
public class ManagerOkolia extends Manager
{
	public ManagerOkolia(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentModelu", id="18", type="Notice"
	public void processInit(MessageForm message)
	{
		message.setAddressee(((AgentOkolia)myAgent()).findAssistant(Id.planovacPrichodov));
		System.out.println(_mySim.currentTime());
		startContinualAssistant(message);   // nastavy kod spravy na start
	}

	//meta! sender="PlanovacPrichodov", id="28", type="Finish"
	public void processFinish(MessageForm message)
	{
		((AgentOkolia)myAgent()).incPocetZakaznikov();
		((MyMessage)message).getZakaznik().setStav(StavyOsoby.PRICHOD);
		message.setAddressee(((MySimulation)mySim()).agentModelu());
		message.setCode(Mc.prichodZakaznika);
		notice(message);
	}

	//meta! sender="AgentModelu", id="13", type="Notice"
	public void processOdchodZakaznika(MessageForm message)
	{
		//nic
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.init:
			processInit(message);
		break;

		case Mc.finish:
			processFinish(message);
		break;

		case Mc.odchodZakaznika:
			processOdchodZakaznika(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolia myAgent()
	{
		return (AgentOkolia)super.myAgent();
	}

}