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
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.setCode(Mc.start);
		sprava.setAddressee(myAgent().findAssistant(Id.zatvaraniePredajneAutomat));
		startContinualAssistant(sprava);
	}

	//meta! sender="AgentPredajna", id="48", type="Request"
	public void processZadavanieDoAutomatu(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentAutomat predajna = (AgentAutomat) myAgent();
		sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
		myAgent().getPriemerDlzkaRaduAutomat().addSample(myAgent().getFrontZakaznikov().size());
		//ak je automat prazdny a zmesti sa do radu pred obsluznymi rovno ho zarad do automatu inak ho zarad do radu

		if (predajna.isAutomatIsEmpty() && !predajna.isJeBlokovany()) {
			predajna.setAutomatIsEmpty(false);
			sprava.getZakaznik().setStav(StavyOsoby.ZADAVANIE_DO_AUTOMATU);
			sprava.setAddressee(myAgent().findAssistant(Id.procesZadavaniaDoAutomatu));
			startContinualAssistant(sprava);

		} else {
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
			predajna.getFrontZakaznikov().add(sprava.getZakaznik());
			myAgent().getPriemerDlzkaRaduAutomat().addSample(myAgent().getFrontZakaznikov().size());
		}
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());

	}

	//meta! sender="AgentPredajna", id="63", type="Notice"
	public void processUvolnenieAutomatu(MessageForm message)
	{
		myAgent().setJeBlokovany(false);
		if (!myAgent().getFrontZakaznikov().isEmpty() && myAgent().isAutomatIsEmpty()) {
			MyMessage sprava = new MyMessage((MyMessage) message);
			message.stack().add(mySim().findAgent(Id.agentModelu));
			message.stack().add(myAgent());
			myAgent().setAutomatIsEmpty(false);
			sprava.setZakaznik(myAgent().getFrontZakaznikov().poll());
			sprava.setAddressee(myAgent().findAssistant(Id.procesZadavaniaDoAutomatu));
			myAgent().getPriemerDlzkaRaduAutomat().addSample(myAgent().getFrontZakaznikov().size());
			startContinualAssistant(sprava);
		}
	}

	//meta! sender="ProcesZadavaniaDoAutomatu", id="31", type="Finish"
	public void processFinishProcesZadavaniaDoAutomatu(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentAutomat predajna = (AgentAutomat) myAgent();
		((MyMessage)message).getZakaznik().setStav(StavyOsoby.KONIEC_ZADAVANIA_DO_AUTOMATU);
		if (!predajna.getFrontZakaznikov().isEmpty() && !predajna.isJeBlokovany()) {
			sprava.setZakaznik(predajna.getFrontZakaznikov().poll());
			sprava.setAddressee(myAgent().findAssistant(Id.procesZadavaniaDoAutomatu));
			myAgent().getPriemerDlzkaRaduAutomat().addSample(myAgent().getFrontZakaznikov().size());
			startContinualAssistant(sprava);
		} else {
			predajna.setAutomatIsEmpty(true);
		}
		message.setCode(Mc.obsluhaZakaznika);
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
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


	//meta! sender="ZatvaraniePredajneAutomat", id="128", type="Finish"
	public void processFinishZatvaraniePredajneAutomat(MessageForm message)
	{
		myAgent().getFrontZakaznikov().clear();
	}


	//meta! sender="AgentPredajna", id="150", type="Notice"
	public void processZablokovanieAutomatu(MessageForm message)
	{
		myAgent().setJeBlokovany(true);
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
		case Mc.uvolnenieAutomatu:
			processUvolnenieAutomatu(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.zatvaraniePredajneAutomat:
				processFinishZatvaraniePredajneAutomat(message);
			break;

			case Id.procesZadavaniaDoAutomatu:
				processFinishProcesZadavaniaDoAutomatu(message);
			break;
			}
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.zadavanieDoAutomatu:
			processZadavanieDoAutomatu(message);
		break;

		case Mc.zablokovanieAutomatu:
			processZablokovanieAutomatu(message);
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