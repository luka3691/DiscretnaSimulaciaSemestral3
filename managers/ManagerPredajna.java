package managers;

import OSPABA.*;
import Osoby.Osoba;
import Osoby.StavyOsoby;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="8"
public class ManagerPredajna extends Manager
{
	public ManagerPredajna(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentModelu", id="20", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="AgentObsluzneMiesta", id="46", type="Response"
	public void processObsluhaZakaznika(MessageForm message)
	{
		message.setAddressee(myAgent().mySim().findAgent(Id.agentObsluzneMiesta));
		message.setSender(myAgent());
		message.setCode(Mc.obsluhaZakaznika);
		request(message);
	}

	//meta! sender="AgentObsluzneMiesta", id="21", type="Notice"
	public void processUvolnenieObsluzneho(MessageForm message)
	{
	}

	//meta! sender="AgentAutomat", id="48", type="Response"
	public void processZadavanieDoAutomatu(MessageForm message)
	{
		message.setAddressee(myAgent().mySim().findAgent(Id.agentAutomat));
		message.setSender(myAgent());
		message.setCode(Mc.zadavanieDoAutomatu);
		request(message);
	}

	//meta! sender="AgentObsluzneMiesta", id="71", type="Notice"
	public void processEndObednejPrestavkyAgentObsluzneMiesta(MessageForm message)
	{
	}

	//meta! sender="AgentPokladne", id="25", type="Notice"
	public void processEndObednejPrestavkyAgentPokladne(MessageForm message)
	{
	}

	//meta! sender="AgentPokladne", id="49", type="Response"
	public void processPlatenieUPokoladne(MessageForm message)
	{
		message.setAddressee(myAgent().mySim().findAgent(Id.agentPokladne));
		message.setSender(myAgent());
		message.setCode(Mc.platenieUPokoladne);
		request(message);
	}

	//meta! sender="AgentModelu", id="16", type="Request"
	public void processZakaznikVPredajni(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentObsluzneMiesta", id="135", type="Response"
	public void processSpatnePrevzatie(MessageForm message)
	{
		Osoba zakaznik = ((MyMessage)message).getZakaznik();
		if (zakaznik.isNechalTovarNaVydajni()) {
			zakaznik.setStav(StavyOsoby.IDE_SI_PRE_NADROZMERNY_TOVAR);
			message.setAddressee(myAgent().mySim().findAgent(Id.agentObsluzneMiesta));
			message.setSender(myAgent());
			message.setCode(Mc.spatnePrevzatie);
			request(message);
		} else {
			zakaznik.setStav(StavyOsoby.ODCHADZA);
			//predajna.getPriemerCasVObchode().pridajZaznam(predajna.getSimCas() - osoba.getCasPrichodu());
			//int pocetObsluzenych = predajna.getPocetObsluzenychZakaznikov() + 1;
			//predajna.setPocetObsluzenychZakaznikov(pocetObsluzenych);
			response(message);
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
		case Mc.spatnePrevzatie:
			processSpatnePrevzatie(message);
		break;

		case Mc.endObednejPrestavky:
			switch (message.sender().id())
			{
			case Id.agentPokladne:
				processEndObednejPrestavkyAgentPokladne(message);
			break;

			case Id.agentObsluzneMiesta:
				processEndObednejPrestavkyAgentObsluzneMiesta(message);
			break;
			}
		break;

		case Mc.platenieUPokoladne:
			processPlatenieUPokoladne(message);
		break;

		case Mc.zadavanieDoAutomatu:
			processZadavanieDoAutomatu(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.uvolnenieObsluzneho:
			processUvolnenieObsluzneho(message);
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
		break;

		case Mc.zakaznikVPredajni:
			processZakaznikVPredajni(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPredajna myAgent()
	{
		return (AgentPredajna)super.myAgent();
	}

}