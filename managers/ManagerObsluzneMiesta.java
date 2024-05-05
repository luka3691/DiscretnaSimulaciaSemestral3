package managers;

import OSPABA.*;
import Osoby.Osoba;
import Osoby.StavyOsoby;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.EventListener;
import java.util.Queue;

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

	//meta! userInfo="Removed from model"
	public void processUvolneniePredajne(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="46", type="Request"
	public void processObsluhaZakaznika(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.setAddressee(myAgent().findAssistant(Id.dotazZaradNaObsluzne));
		execute(sprava);
		int id = sprava.getZakaznik().getIdObsluzneho();
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());
		if (id != -1) {
			//nasla sa volna pokladna, naplanuj zaciatok obsluhy
			if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
				myAgent().getOnlineObsluzne()[id] = false;
			} else {
				myAgent().getNormalneObsluzne()[id] = false;
			}
			sprava.getZakaznik().setIdObsluzneho(id);
			sprava.setCisloObsluzneho(id);
			sprava.getZakaznik().setStav(StavyOsoby.JE_OBSLUHOVANY);
			sprava.setAddressee(myAgent().findAssistant(Id.procesObsluhy));
			startContinualAssistant(sprava);
		} else {
			//zarad osobu do radu pred obsluznymi
			myAgent().zaradDoRadu(sprava.getZakaznik());
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_OSBLUHOU);
		};
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());
		odoslanieNotifikacie(sprava);
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
	}

	//meta! userInfo="Removed from model"
	public void processStartObednejPrestavky(MessageForm message)
	{
	}

	//meta! sender="ProcesObsluhy", id="34", type="Finish"
	public void processFinishProcesObsluhy(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());
		int idNavstivenehoObsluzneho = sprava.getZakaznik().getIdObsluzneho();
		boolean[] obsluzneDanehoTypu;
		if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			obsluzneDanehoTypu = myAgent().getOnlineObsluzne();
		} else {
			obsluzneDanehoTypu = myAgent().getNormalneObsluzne();
		}
		if (!sprava.getZakaznik().isNechalTovarNaVydajni()) {
			Queue<Osoba> queue;
			if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
				queue = myAgent().getOnlineQueue();
			} else {
				queue = myAgent().getOsobyQueue();
			}
			//naplanuj novu obsluhu ak nie je rad prazdny
			if (!queue.isEmpty()) {
				Osoba novaOsoba = queue.poll();
				sprava.setCisloObsluzneho(idNavstivenehoObsluzneho);
				novaOsoba.setIdObsluzneho(idNavstivenehoObsluzneho);
				novaOsoba.setStav(StavyOsoby.JE_OBSLUHOVANY);
				obsluzneDanehoTypu[idNavstivenehoObsluzneho] = false;
				sprava.setZakaznik(novaOsoba);
				sprava.setAddressee(myAgent().findAssistant(Id.procesObsluhy));
				startContinualAssistant(sprava);
				odoslanieNotifikacie(message);
			} else {
				obsluzneDanehoTypu[idNavstivenehoObsluzneho] = true;
			}

		}
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());

		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
		message.setCode(Mc.platenieUPokoladne);
		response(message);
	}

	//meta! sender="PlanovacPrestavkaObsluzne", id="60", type="Finish"
	public void processFinishPlanovacPrestavkaObsluzne(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentPredajna", id="135", type="Request"
	public void processSpatnePrevzatie(MessageForm message)
	{
		//ak nie je prazdny rad pred danou pokladnou tak naplanuj zaciatok platenia
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.setAddressee(myAgent().findAssistant(Id.spatnePrevzatie));
		startContinualAssistant(sprava);
	}

	//meta! sender="SpatnePrevzatie", id="139", type="Finish"
	public void processFinishSpatnePrevzatie(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		int idNavstivenehoObsluzneho = sprava.getZakaznik().getIdObsluzneho();
		boolean[] obsluzneDanehoTypu;
		if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			obsluzneDanehoTypu = myAgent().getOnlineObsluzne();
		} else {
			obsluzneDanehoTypu = myAgent().getNormalneObsluzne();
		}
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());
		Queue<Osoba> queue;
		if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			queue = myAgent().getOnlineQueue();
		} else {
			queue = myAgent().getOsobyQueue();
		}
		//naplanuj novu obsluhu ak nie je rad prazdny
		if (!queue.isEmpty()) {
			Osoba novaOsoba = queue.poll();
			novaOsoba.setIdObsluzneho(idNavstivenehoObsluzneho);
			novaOsoba.setStav(StavyOsoby.JE_OBSLUHOVANY);
			obsluzneDanehoTypu[idNavstivenehoObsluzneho] = false;
			sprava.setZakaznik(novaOsoba);
			sprava.setAddressee(myAgent().findAssistant(Id.procesObsluhy));
			startContinualAssistant(sprava);
			odoslanieNotifikacie(message);

		} else {
			obsluzneDanehoTypu[idNavstivenehoObsluzneho] = true;
		}
		myAgent().getPriemerDlzkaRaduPredObsluzOnline().addSample(myAgent().getOnlineQueue().size());
		myAgent().getPriemerDlzkaRaduPredObsluzNormal().addSample(myAgent().getOsobyQueue().size());
		((MySimulation)mySim()).setStavyOsob(((MyMessage) message).getZakaznik().toArray());
		((MyMessage) message).getZakaznik().setNechalTovarNaVydajni(false);
		message.setCode(Mc.spatnePrevzatie);

		response(message);
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

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.procesObsluhy:
				processFinishProcesObsluhy(message);
			break;

			case Id.planovacPrestavkaObsluzne:
				processFinishPlanovacPrestavkaObsluzne(message);
			break;

			case Id.spatnePrevzatie:
				processFinishSpatnePrevzatie(message);
			break;
			}
		break;

		case Mc.obsluhaZakaznika:
			processObsluhaZakaznika(message);
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
	private void odoslanieNotifikacie(MessageForm message) {
		MyMessage sprava = new MyMessage((MyMessage) message);
		sprava.setAddressee(myAgent().findAssistant(Id.dotazZmestiSaDoObsluznych));
		execute(sprava);
		if (sprava.msgResult() == 0) {
			sprava.setAddressee(mySim().findAgent(Id.agentPredajna));
			sprava.setCode(Mc.zablokovanieAutomatu);
			notice(sprava);
		} else {
			sprava.setAddressee(mySim().findAgent(Id.agentPredajna));
			sprava.setCode(Mc.uvolnenieAutomatu);
			notice(sprava);
		}
	}
}