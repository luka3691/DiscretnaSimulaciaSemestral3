package managers;

import OSPABA.*;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="1"
public class ManagerModelu extends Manager
{
	public ManagerModelu(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentOkolia", id="12", type="Notice"
	public void processPrichodZakaznika(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentPredajna));
		message.setCode(Mc.zadavanieDoAutomatu);
		request(message);
	}

	//meta! sender="AgentPredajna", id="16", type="Response"
	public void processZakaznikVPredajni(MessageForm message)
	{
		message.setAddressee(mySim().findAgent(Id.agentOkolia));
		message.setCode(Mc.odchodZakaznika);
		notice(message);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.init:
				MyMessage initOkolie = new MyMessage((MyMessage) message);
				MyMessage initPredajne = new MyMessage((MyMessage) message);
				initOkolie.setAddressee(mySim().findAgent(Id.agentOkolia));
				initOkolie.setCode(Mc.init);
				initPredajne.setAddressee(mySim().findAgent(Id.agentPredajna));
				initPredajne.setCode(Mc.init);
				notice(initOkolie);
				notice(initPredajne);
				break;
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
		case Mc.zakaznikVPredajni:
			processZakaznikVPredajni(message);
		break;

		case Mc.prichodZakaznika:
			processPrichodZakaznika(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentModelu myAgent()
	{
		return (AgentModelu)super.myAgent();
	}

}