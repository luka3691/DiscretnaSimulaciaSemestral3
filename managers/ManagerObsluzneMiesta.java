package managers;

import OSPABA.*;
import Osoby.StavyOsoby;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

//meta! id="7"
public class ManagerObsluzneMiesta extends Manager
{
	public ManagerObsluzneMiesta(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentPredajna", id="72", type="Notice"
	public void processUvolneniePredajne(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="46", type="Request"
	public void processObsluhaZakaznika(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentObsluzneMiesta obsluzne = (AgentObsluzneMiesta)myAgent();
		sprava.setAddressee(myAgent().findAssistant(Id.dotazZaradNaObsluzne));
		execute(sprava);
		int id = sprava.getCisloObsluzneho();
		if (id != -1) {
			//nasla sa volna pokladna, naplanuj zaciatok obsluhy
			if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
				myAgent().getOnlineObsluzne()[id] = false;
			} else {
				myAgent().getNormalneObsluzne()[id] = false;
			}
			sprava.getZakaznik().setIdObsluzneho(id);
			sprava.getZakaznik().setStav(StavyOsoby.JE_OBSLUHOVANY);
			sprava.setAddressee(myAgent().findAssistant(Id.procesObsluhy));
			startContinualAssistant(sprava);
		} else {
			//zarad osobu do radu pred obsluznymi
			myAgent().zaradDoRadu(sprava.getZakaznik());
			/*
			if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
				predajna.getPriemerDlzkaRaduPredObsluzOnline().pridajZaznam(predajna.getObsluzneMiesta().getOnlineQueue().size(), predajna.getSimCas());
			} else {
				predajna.getPriemerDlzkaRaduPredObsluzNormal().pridajZaznam(predajna.getObsluzneMiesta().getOsobyQueue().size(), predajna.getSimCas());
			}
			if (predajna.getObsluzneMiesta().zmestiSa(predajna.getAutomatIsEmpty())) {
				predajna.setAutomatIsEmpty(true);
			}

			 */
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_OSBLUHOU);
		};
	}

	//meta! sender="AgentPredajna", id="69", type="Notice"
	public void processStartObednejPrestavky(MessageForm message)
	{
	}

	//meta! sender="ProcesObsluhy", id="34", type="Finish"
	public void processFinishProcesObsluhy(MessageForm message)
	{
		message.setCode(Mc.platenieUPokoladne);
		response(message);
	}

	//meta! sender="Scheduler1", id="60", type="Finish"
	public void processFinishScheduler1(MessageForm message)
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.scheduler1:
				processFinishScheduler1(message);
			break;

			case Id.procesObsluhy:
				processFinishProcesObsluhy(message);
			break;
			}
		break;

		case Mc.startObednejPrestavky:
			processStartObednejPrestavky(message);
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
		break;

		case Mc.uvolneniePredajne:
			processUvolneniePredajne(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

}