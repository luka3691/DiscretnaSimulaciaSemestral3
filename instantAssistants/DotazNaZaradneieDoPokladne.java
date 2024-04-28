package instantAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import simulation.*;
import agents.*;

import java.util.ArrayList;

//meta! id="97"
public class DotazNaZaradneieDoPokladne extends Query
{
	private UniformContinuousRNG nahodneZaradenieDoPokladne = new UniformContinuousRNG(0.0, 1.0);

	public DotazNaZaradneieDoPokladne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void execute(MessageForm message)
	{
		MyMessage sprava = (MyMessage) message;
		int cisloPokladne = getIDPokladne();
		sprava.setCisloPokladne(cisloPokladne);
	}

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

	public int getIDPokladne() {
		int pocetPrazdnych = 0;
		int prazdnaPokladna = 0;
		for (int i = 0; i < Config.pocetPokladni; i++) {
			if (myAgent().getPokladne()[i]) {
				pocetPrazdnych++;
				prazdnaPokladna = i;
			}
		}
		if (pocetPrazdnych == 0) {
			return -1;
		} else if (pocetPrazdnych == 1) {
			return prazdnaPokladna;
		} else {
			return this.zaradNahodneDoPokladne();
		}
	}
	public int zaradNahodneDoPokladne() {
		ArrayList<Integer> prazdnePokladne = new ArrayList<>();
		for (int i = 0; i < Config.pocetPokladni; i++) {
			if (myAgent().getPokladne()[i]) {
				prazdnePokladne.add(i);
			}
		}
		double pravdepodobnost = nahodneZaradenieDoPokladne.sample();
		double krok = krok = 1.0 / prazdnePokladne.size();;
		double prav = krok;
		int poradieVPrazdnych = 0;
		while (prav <= 1.0) {
			if (pravdepodobnost < prav) {
				return prazdnePokladne.get(poradieVPrazdnych);
			}
			poradieVPrazdnych++;
			prav+= krok;
		}
		return poradieVPrazdnych;
	}

}