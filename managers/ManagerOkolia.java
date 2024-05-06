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
		MyMessage prichody = new MyMessage((MyMessage) message);
		prichody.setAddressee((myAgent()).findAssistant(Id.planovacPrichodov));
		startContinualAssistant(prichody);
		if (Config.zmenenyTok) {
			MyMessage prichodyOnline = new MyMessage((MyMessage) message);
			prichodyOnline.setAddressee((myAgent()).findAssistant(Id.planovacPrichodovOnline));
			startContinualAssistant(prichodyOnline);
			MyMessage prichodyZmluvny = new MyMessage((MyMessage) message);
			prichodyZmluvny.setAddressee((myAgent()).findAssistant(Id.planovacPrichodovBizinis));
			startContinualAssistant(prichodyZmluvny);
		}
	}

	//meta! sender="PlanovacPrichodov", id="28", type="Finish"
	public void processFinishPlanovacPrichodov(MessageForm message)
	{
		((MyMessage)message).getZakaznik().setStav(StavyOsoby.PRICHOD);
		message.setAddressee(((MySimulation)mySim()).agentModelu());
		message.setCode(Mc.prichodZakaznika);
		myAgent().incPocetZakaznikov();
		notice(message);
	}

	//meta! sender="AgentModelu", id="13", type="Notice"
	public void processOdchodZakaznika(MessageForm message)
	{
		//nic
		myAgent().getPriemerCasVObchode().addSample(mySim().currentTime() - ((MyMessage)message).getZakaznik().getCasPrichodu());
		myAgent().incPocetObsluzenychZakaznikov();
		myAgent().setCasOdchoduPosledneho(mySim().currentTime());
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="ZatvaraniePredajneOkolie", id="125", type="Finish"
	public void processFinishZatvaraniePredajneOkolie(MessageForm message)
	{
	}

	//meta! sender="PlanovacPrichodovBizinis", id="184", type="Finish"
	public void processFinishPlanovacPrichodovBizinis(MessageForm message)
	{
	}

	//meta! sender="PlanovacPrichodovOnline", id="182", type="Finish"
	public void processFinishPlanovacPrichodovOnline(MessageForm message)
	{
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.zatvaraniePredajneOkolie:
				processFinishZatvaraniePredajneOkolie(message);
			break;

			case Id.planovacPrichodov:
				processFinishPlanovacPrichodov(message);
			break;

			case Id.planovacPrichodovBizinis:
				processFinishPlanovacPrichodovBizinis(message);
			break;

			case Id.planovacPrichodovOnline:
				processFinishPlanovacPrichodovOnline(message);
			break;
			}
		break;

		case Mc.init:
			processInit(message);
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