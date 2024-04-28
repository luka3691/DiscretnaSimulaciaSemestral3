package managers;

import OSPABA.*;
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
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		osoba.setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
		predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
		//ak je automat prazdny a zmesti sa do radu pred obsluznymi rovno ho zarad do automatu inak ho zarad do radu
		if (predajna.getAutomatIsEmpty() && predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
			predajna.naplanujUdalost(new ZačiatokZadavaniaDoAutomatu(predajna, predajna.getSimCas(), osoba));
			predajna.setAutomatIsEmpty(false);
		} else {
			predajna.getOsobyQueue().add(osoba);
			osoba.setStav(StavyOsoby.V_RADE_PRED_AUTOMATOM);
			predajna.getPriemerDlzkaRadu().pridajZaznam(predajna.getOsobyQueue().size(), predajna.getSimCas());
		}
		//naplanuj dalsi prichod
		double dalsiPrichod = predajna.getSimCas() + predajna.getNahodnyJav().getPrichodLudi();
		if (dalsiPrichod < predajna.getKoniecCasu()) {
			predajna.naplanujUdalost(new PrichodZakaznika(jadro, dalsiPrichod));
		}
	}

	//meta! sender="AgentPredajna", id="63", type="Notice"
	public void processUvolnenieAutomatu(MessageForm message)
	{
	}

	//meta! sender="ProcesZadavaniaDoAutomatu", id="31", type="Finish"
	public void processFinish(MessageForm message)
	{
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
		case Mc.zadavanieDoAutomatu:
			processZadavanieDoAutomatu(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		case Mc.uvolnenieAutomatu:
			processUvolnenieAutomatu(message);
		break;

		case Mc.finish:
			processFinish(message);
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