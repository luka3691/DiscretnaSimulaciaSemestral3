package managers;

import OSPABA.*;
import Osoby.Osoba;
import Osoby.StavyOsoby;
import Osoby.TypZakaznika;
import simulation.*;
import agents.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.Queue;

//meta! id="6"
public class ManagerPokladne extends Manager
{
	public ManagerPokladne(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentPredajna", id="58", type="Notice"
	public void processInit(MessageForm message)
	{
	}

	//meta! sender="AgentPredajna", id="24", type="Notice"
	public void processStartObednejPrestavky(MessageForm message)
	{
	}

	//meta! sender="Scheduler2", id="62", type="Finish"
	public void processFinishScheduler2(MessageForm message)
	{
	}

	//meta! sender="ProcesPlatenia", id="37", type="Finish"
	public void processFinishProcesPlatenia(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		int idPokladne = sprava.getCisloPokladne();
		pokladne.getPokladne()[idPokladne] = true;
		//ak si nechal tovar na vydajni musi si pre neho ist
		//ak nie je prazdny rad pred danou pokladnou tak naplanuj zaciatok platenia
		if (!pokladne.getRady()[idPokladne].isEmpty()) {
			MyMessage spravaCopy = new MyMessage((MyMessage) message);
			Osoba osobaNova = pokladne.getRady()[idPokladne].poll();
			spravaCopy.setZakaznik(osobaNova);
			pokladne.getPokladne()[idPokladne] = false;
			spravaCopy.setCode(Mc.platenieUPokoladne);
			spravaCopy.setAddressee(proces());
			startContinualAssistant(spravaCopy);
			//statistiky
			//predajna.getPriemerDlzkaRadovPriPokladniach().get(osoba.getIdPokladne()).pridajZaznam(predajna.getPokladne().getRady()[osoba.getIdPokladne()].size(), predajna.getSimCas());
		}
		if (osoba.isNechalTovarNaVydajni()) {
			osoba.setStav(StavyOsoby.IDE_SI_PRE_NADROZMERNY_TOVAR);
			predajna.naplanujUdalost(new PrevzatieNadrozmernehoTovaru(predajna, predajna.getSimCas() + predajna.getNahodnyJav().getSpatnePrevzatieTovaru(), osoba));
		} else {
			osoba.setStav(StavyOsoby.ODCHADZA);
			predajna.getPriemerCasVObchode().pridajZaznam(predajna.getSimCas() - osoba.getCasPrichodu());
			int pocetObsluzenych = predajna.getPocetObsluzenychZakaznikov() + 1;
			predajna.setPocetObsluzenychZakaznikov(pocetObsluzenych);
		}


	}

	//meta! sender="AgentPredajna", id="49", type="Request"
	public void processPlatenieUPokoladne(MessageForm message)
	{
		MyMessage sprava = new MyMessage((MyMessage) message);
		AgentPokladne pokladne = (AgentPokladne)myAgent();
		sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
		sprava.setAddressee(myAgent().findAssistant(Id.dotazNaZaradneieDoPokladne));
		execute(sprava);
		int idPokladneNaZaradenie = (int) sprava.msgResult();
		boolean[] obsluzneDanehoTypu;
		/*
		if (sprava.getZakaznik().getTypZakaznika() == TypZakaznika.ONLINE) {
			obsluzneDanehoTypu = pokladne.getObsluzneMiesta().getOnlineObsluzne();
		} else {
			obsluzneDanehoTypu = predajna.getObsluzneMiesta().getNormalneObsluzne();
		}

		obsluzneDanehoTypu[osoba.getIdObsluzneho()] = !osoba.isNechalTovarNaVydajni();
		 */
		if (idPokladneNaZaradenie != -1) {
			//nasla sa volna pokladna tak zarad osobu do tej pokladne
			pokladne.getPokladne()[idPokladneNaZaradenie] = false;
			message.setAddressee(proces());
			startContinualAssistant(message);
		} else {
			//zarad osobu do najratsieho radu
			sprava.getZakaznik().setStav(StavyOsoby.V_RADE_PRED_POKLADNOU);
			sprava.setAddressee(myAgent().findAssistant(Id.dotazNaZaradneieDoPokladne));
			execute(sprava);
			int idRaduNaZaradenie = (int) sprava.msgResult();
			pokladne.getRady()[idRaduNaZaradenie].add(((MyMessage) message).getZakaznik());
			//predajna.getPriemerDlzkaRadovPriPokladniach().get(idRaduNaZaradenie).pridajZaznam(predajna.getPokladne().getRady()[idRaduNaZaradenie].size(), predajna.getSimCas());
		}
		/*
		Queue<Osoba> queue;
		if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
			queue = predajna.getObsluzneMiesta().getOnlineQueue();
		} else {
			queue = predajna.getObsluzneMiesta().getOsobyQueue();
		}
		//naplanuj novu obsluhu ak nie je rad prazdny
		if (obsluzneDanehoTypu[osoba.getIdObsluzneho()] && !queue.isEmpty()) {
			Osoba novaOsoba = queue.poll();
			int id = predajna.getObsluzneMiesta().getIDVolnaPokladna(novaOsoba);
			if (id != -1) {
				//pokladna je volna
				predajna.naplanujUdalost(new ZačiatokObsluhy(predajna, predajna.getSimCas(), novaOsoba, id));
			}

		}

		 */
		predajna.setStavyOsob(osoba.toArray());
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
			case Id.scheduler2:
				processFinishScheduler2(message);
			break;

			case Id.procesPlatenia:
				processFinishProcesPlatenia(message);
			break;
			}
		break;

		case Mc.startObednejPrestavky:
			processStartObednejPrestavky(message);
		break;

		case Mc.platenieUPokoladne:
			processPlatenieUPokoladne(message);
		break;

		case Mc.init:
			processInit(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}
	public ContinualAssistant proces()
	{ return ((AgentPokladne)myAgent()).f; }
}