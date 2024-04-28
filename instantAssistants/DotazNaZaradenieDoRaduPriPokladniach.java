package instantAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;

import java.util.ArrayList;

//meta! id="100"
public class DotazNaZaradenieDoRaduPriPokladniach extends Query
{
	private UniformContinuousRNG nahodneZaradenieDoRadu = new UniformContinuousRNG(0.0, 1.0);
	public DotazNaZaradenieDoRaduPriPokladniach(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		MyMessage sprava = (MyMessage) message;
		int cisloPokladne = getIDNajmensiehoRadu();
		sprava.getZakaznik().setIdPokladne(cisloPokladne);
		sprava.setCisloPokladne(cisloPokladne);
	}

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

	public int getIDNajmensiehoRadu() {
		int idNajmensiRad = 0;
		int pocetVNajmensomRade = Integer.MAX_VALUE;
		int pocetRovankychRadov = 1;
		for (int i = 0; i < Config.pocetPokladni; i++) {
			if (myAgent().getRady()[i].size() < pocetVNajmensomRade) {
				idNajmensiRad = i;
				pocetRovankychRadov = 1;
				pocetVNajmensomRade = myAgent().getRady()[i].size();
			} else if (myAgent().getRady()[i].size() == pocetVNajmensomRade) {
				pocetRovankychRadov++;
			}
		}
		if (pocetRovankychRadov >= 2) {
			return zaradNahodneDoRadu();
		} else {
			return idNajmensiRad;
		}
	}

	public int zaradNahodneDoRadu() {
		ArrayList<Integer> prazdneRady = new ArrayList<>();
		int pocetVNajmensomRade = Integer.MAX_VALUE;
		for (int i = 0; i < Config.pocetPokladni; i++) {
			if (myAgent().getRady()[i].size() < pocetVNajmensomRade) {
				prazdneRady.clear();
				prazdneRady.add(i);
				pocetVNajmensomRade = myAgent().getRady()[i].size();
			} else if (myAgent().getRady()[i].size() == pocetVNajmensomRade) {
				prazdneRady.add(i);
			}
		}
		double pravdepodobnost = nahodneZaradenieDoRadu.sample();
		double krok = 1.0 / prazdneRady.size();
		double prav = krok;
		int poradieVPrazdnych = 0;
		while (prav <= 1.0) {
			if (pravdepodobnost < prav) {
				return prazdneRady.get(poradieVPrazdnych);
			}
			poradieVPrazdnych++;
			prav+= krok;
		}
		return poradieVPrazdnych;
	}
}
