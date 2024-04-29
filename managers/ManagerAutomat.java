package managers;

import OSPABA.*;
import Osoby.StavyOsoby;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="9"
public class ManagerAutomat extends Manager
{
	public ManagerAutomat(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentPredajna", id="56", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="48", type="Request"
	public void processZadavanieDoAutomatu(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentAutomat predajna = (AgentAutomat) myAgent();
		sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
		//predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
		//ak je automat prazdny a zmesti sa do radu pred obsluznymi rovno ho zarad do automatu inak ho zarad do radu

		if (predajna.isAutomatIsEmpty() /* && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())*/) {
			predajna.setAutomatIsEmpty(false);
			sprava.getZakaznik().setStav(StavyOsoby.ZADAVANIE_DO_AUTOMATU);
			sprava.setAddressee(myAgent().findAssistant(Id.procesZadavaniaDoAutomatu));
			startContinualAssistant(sprava);

		} else {
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
			predajna.getFrontZakaznikov().add(sprava.getZakaznik());
			//predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
		}
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());

	}

	//meta! sender="AgentPredajna", id="63", type="Notice"
	public void processUvolnenieAutomatu(MessageForm message)
	{
	}

	//meta! sender="ProcesZadavaniaDoAutomatu", id="31", type="Finish"
	public void processFinish(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentAutomat predajna = (AgentAutomat) myAgent();
		((MyMessage)message).getZakaznik().setStav(StavyOsoby.KONIEC_ZADAVANIA_DO_AUTOMATU);
		if (!predajna.getFrontZakaznikov().isEmpty() /*&& predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())*/) {
			sprava.setZakaznik(predajna.getFrontZakaznikov().poll());
			sprava.setAddressee(myAgent().findAssistant(Id.procesZadavaniaDoAutomatu));
			startContinualAssistant(sprava);
		} else {
			predajna.setAutomatIsEmpty(true);
		}
		message.setCode(Mc.obsluhaZakaznika);
		response(message);
		//predajna.setStavyOsob(osoba.toArray());
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentPredajna", id="121", type="Request"
	public void processSpatnePrevzatie(MessageForm message)
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
			processFinish(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.zadavanieDoAutomatu:
			processZadavanieDoAutomatu(message);
		break;

		case Mc.spatnePrevzatie:
			processSpatnePrevzatie(message);
		break;

		case Mc.uvolnenieAutomatu:
			processUvolnenieAutomatu(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}