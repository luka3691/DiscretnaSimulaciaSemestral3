package simulation;

import OSPABA.*;
import Objects.Statistika;
import agents.*;

import java.util.*;

public class MySimulation extends Simulation
{
	private Statistika priemerCasVObchode;
	private Statistika priemerDlzkaRadu;
	private Statistika priemerPocetLudiCelkovy;
	private Statistika priemerCasVObchodeCelkovy;
	private Statistika priemerCakanieVRadePredAutomatomCalkovy;
	private Statistika pocetObsluzenychZakaznikovCelkove;
	private Statistika priemerPoslednyOdchod;


	private Statistika priemerVytazenieAutomatuCelkove;
	private ArrayList<Statistika> priemerVytazenostPokladniCelkove;
	private ArrayList<Statistika> priemerVytazenostObsluznychOnlineCelkove;
	private ArrayList<Statistika> priemerVytazenostObsluznychOstatneCelkove;
	private ArrayList<Statistika> priemerDlzkaRadovPriPokladniachCelkove;

	private Statistika priemerDlzkaRaduPredObsluzOnlineCelkove;
	private Statistika priemerDlzkaRaduPredObsluzNormalCelkove;
	private PriorityQueue<Osoba> osobyQueue;
	private int personIndex;
	private int pocetObsluzenychZakaznikov;
	private ObsluzneMiesta obsluzneMiesta;

	private Osoba osobaUAutomatu = null;

	private boolean automatIsEmpty;

	private double zaciatokCasu;
	private double koniecCasu;
	private NahodneJavy nahodnyJav;
	private Pokladne pokladne;
	private int pocetObsluznych;
	private int pocetPokladni;
	private ArrayList<String> stavyOsob;

	public MySimulation()
	{
		init();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
		nahodnyJav = new NahodneJavy(new Random());
		priemerPocetLudiCelkovy = new Statistika(false);
		priemerCasVObchodeCelkovy = new Statistika(false);
		priemerDlzkaRaduCelkovy = new Statistika(false);
		priemerCakanieVRadePredAutomatomCalkovy = new Statistika(false);
		pocetObsluzenychZakaznikovCelkove = new Statistika(false);
		priemerPoslednyOdchod = new Statistika(false);
		priemerVytazenostObsluznychOnlineCelkove = new ArrayList<>();
		priemerVytazenostPokladniCelkove = new ArrayList<>();
		priemerDlzkaRadovPriPokladniachCelkove = new ArrayList<>();
		priemerVytazenostObsluznychOstatneCelkove = new ArrayList<>();
		priemerDlzkaRaduPredObsluzOnlineCelkove = new Statistika(false);
		priemerDlzkaRaduPredObsluzNormalCelkove = new Statistika(false);


		for (int i = 0; i < pocetPokladni; i++) {
			priemerVytazenostPokladniCelkove.add(new Statistika(false));
			priemerDlzkaRadovPriPokladniachCelkove.add(new Statistika(false));
		}
		for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
			priemerVytazenostObsluznychOnlineCelkove.add(new Statistika(false));
		}
		for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
			priemerVytazenostObsluznychOstatneCelkove.add(new Statistika(false));
		}

		priemerVytazenieAutomatuCelkove = new Statistika(false);

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
		priemerCasVObchode = new Statistika(false);
		priemerDlzkaRadu = new Statistika(true);
		priemerCakanieVRadePredAutomatom = new Statistika(false);
		priemerVytazenieAutomatu = new Statistika(false);
		priemerDlzkaRaduPredObsluzOnline = new Statistika(true);
		priemerDlzkaRaduPredObsluzNormal = new Statistika(true);
		stavyOsob = new ArrayList<>();
		priemerVytazenostObsluznychOnline = new ArrayList<>();
		priemerVytazenostPokladni = new ArrayList<>();
		priemerDlzkaRadovPriPokladniach = new ArrayList<>();
		priemerVytazenostObsluznychOstatne = new ArrayList<>();
		for (int i = 0; i < pocetPokladni; i++) {
			priemerVytazenostPokladni.add(new Statistika(false));
			priemerDlzkaRadovPriPokladniach.add(new Statistika(true));
		}
		for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
			priemerVytazenostObsluznychOnline.add(new Statistika(false));
		}
		for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
			priemerVytazenostObsluznychOstatne.add(new Statistika(false));
		}
		this.pocetObsluznych = pocetObsluznychMiest;
		this.pocetPokladni = pocetPokladni;
		this.osobyQueue = new PriorityQueue<>(new OsobaComparatorNoPriority());
		this.zaciatokCasu = 9*60;
		this.koniecCasu = 17*60;
		obsluzneMiesta = new ObsluzneMiesta(pocetObsluznychMiest);
		pokladne = new Pokladne(pocetPokladni);
		simCas = 0.0;
		this.personIndex = 0;
		automatIsEmpty = true;
		double prvyPrichod = zaciatokCasu + nahodnyJav.getPrichodLudi();
		udalostiQueue.add(new PrichodZakaznika(this, prvyPrichod));
		udalostiQueue.add(new KoniecČasu(this, koniecCasu));
		simCas = prvyPrichod;
		pocetObsluzenychZakaznikov = 0;
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();
		osobyQueue.clear();
		udalostiQueue.clear();

		priemerPocetLudiCelkovy.pridajZaznam(personIndex);
		priemerDlzkaRaduCelkovy.pridajZaznam(priemerDlzkaRadu.vypocitaj());
		priemerCasVObchodeCelkovy.pridajZaznam(priemerCasVObchode.vypocitaj());
		priemerCakanieVRadePredAutomatomCalkovy.pridajZaznam(priemerCakanieVRadePredAutomatom.vypocitaj());
		priemerPoslednyOdchod.pridajZaznam(simCas);
		priemerVytazenieAutomatuCelkove.pridajZaznam(priemerVytazenieAutomatu.getVytazenie(koniecCasu - zaciatokCasu));
		priemerDlzkaRaduPredObsluzOnlineCelkove.pridajZaznam(priemerDlzkaRaduPredObsluzOnline.vypocitaj());
		priemerDlzkaRaduPredObsluzNormalCelkove.pridajZaznam(priemerDlzkaRaduPredObsluzNormal.vypocitaj());
		pocetObsluzenychZakaznikovCelkove.pridajZaznam(pocetObsluzenychZakaznikov);

		for (int i = 0; i < pocetPokladni; i++) {
			priemerVytazenostPokladniCelkove.get(i).pridajZaznam(priemerVytazenostPokladni.get(i).getVytazenie(simCas - zaciatokCasu));
			priemerDlzkaRadovPriPokladniachCelkove.get(i).pridajZaznam(priemerDlzkaRadovPriPokladniach.get(i).vypocitaj());
		}
		for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
			priemerVytazenostObsluznychOnlineCelkove.get(i).pridajZaznam(priemerVytazenostObsluznychOnline.get(i).getVytazenie(simCas-zaciatokCasu));
		}
		for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
			priemerVytazenostObsluznychOstatneCelkove.get(i).pridajZaznam(priemerVytazenostObsluznychOstatne.get(i).getVytazenie(simCas-zaciatokCasu));
		}

	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
		System.out.println("Primer pocet ludi: " + priemerPocetLudiCelkovy.vypocitaj());
		System.out.println("Primer dlzka radu: " + priemerDlzkaRaduCelkovy.vypocitaj());
		System.out.println("Primer cas v obchode: " + priemerCasVObchodeCelkovy.vypocitaj());
		System.out.println("Priemer cakanie pred automatom: " + priemerCakanieVRadePredAutomatomCalkovy.vypocitaj());
		System.out.println("Posledny cas odchodu: " + priemerPoslednyOdchod.vypocitaj());
		System.out.println("Vytazenie automatau:" + priemerVytazenieAutomatuCelkove.vypocitaj()*100);
		for (int i = 0; i < pocetPokladni; i++) {
			System.out.println(priemerVytazenostPokladniCelkove.get(i).vypocitaj()*100);
			System.out.println(priemerDlzkaRadovPriPokladniachCelkove.get(i).vypocitaj());
		}
		for (int i = 0; i < obsluzneMiesta.getOnlineObsluzne().length; i++) {
			System.out.println(priemerVytazenostObsluznychOnlineCelkove.get(i).vypocitaj());
		}
		for (int i = 0; i < obsluzneMiesta.getNormalneObsluzne().length; i++) {
			System.out.println(priemerVytazenostObsluznychOstatneCelkove.get(i).vypocitaj());
		}
		System.out.println(Arrays.toString(priemerCasVObchodeCelkovy.getIntervalSpolahlivosti()));
		System.out.println(priemerDlzkaRaduPredObsluzOnlineCelkove.vypocitaj());
		System.out.println(priemerDlzkaRaduPredObsluzNormalCelkove.vypocitaj());

	}
	public int getNewPersonIndex() {
		this.personIndex++;
		return this.personIndex;
	}

	public boolean getAutomatIsEmpty() {
		return automatIsEmpty;
	}

	public Statistika getPriemerCasVObchode() {
		return priemerCasVObchode;
	}

	public Statistika getPriemerDlzkaRadu() {
		return priemerDlzkaRadu;
	}

	public Queue<Osoba> getOsobyQueue() {
		return osobyQueue;
	}

	public NahodneJavy getNahodnyJav() {
		return nahodnyJav;
	}

	public double getKoniecCasu() {
		return koniecCasu;
	}

	public void setAutomatIsEmpty(boolean automatIsEmpty) {
		this.automatIsEmpty = automatIsEmpty;
	}

	public ObsluzneMiesta getObsluzneMiesta() {
		return obsluzneMiesta;
	}

	public Pokladne getPokladne() {
		return pokladne;
	}

	public void setOsobaUAutomatu(Osoba osobaUAutomatu) {
		this.osobaUAutomatu = osobaUAutomatu;
	}

	public Statistika getPriemerCakanieVRadePredAutomatom() {
		return priemerCakanieVRadePredAutomatom;
	}

	public int getPocetPokladni() {
		return pocetPokladni;
	}

	public Statistika getPriemerDlzkaRaduCelkovy() {
		return priemerDlzkaRaduCelkovy;
	}

	public void setStavyOsob(ArrayList<String> stavyOsob) {
		this.stavyOsob.clear();
		this.stavyOsob = stavyOsob;
	}

	public ArrayList<String> getStavyOsob() {
		return stavyOsob;
	}

	public Statistika getPriemerVytazenieAutomatu() {
		return priemerVytazenieAutomatu;
	}

	public ArrayList<Statistika> getPriemerVytazenostPokladni() {
		return priemerVytazenostPokladni;
	}

	public ArrayList<Statistika> getPriemerVytazenostObsluznychOnline() {
		return priemerVytazenostObsluznychOnline;
	}

	public ArrayList<Statistika> getPriemerVytazenostObsluznychOstatne() {
		return priemerVytazenostObsluznychOstatne;
	}


	public Statistika getPriemerPocetLudiCelkovy() {
		return priemerPocetLudiCelkovy;
	}

	public Statistika getPriemerCasVObchodeCelkovy() {
		return priemerCasVObchodeCelkovy;
	}

	public Statistika getPriemerCakanieVRadePredAutomatomCalkovy() {
		return priemerCakanieVRadePredAutomatomCalkovy;
	}

	public Statistika getPriemerPoslednyOdchod() {
		return priemerPoslednyOdchod;
	}

	public Statistika getPriemerVytazenieAutomatuCelkove() {
		return priemerVytazenieAutomatuCelkove;
	}

	public ArrayList<Statistika> getPriemerVytazenostPokladniCelkove() {
		return priemerVytazenostPokladniCelkove;
	}

	public ArrayList<Statistika> getPriemerVytazenostObsluznychOnlineCelkove() {
		return priemerVytazenostObsluznychOnlineCelkove;
	}

	public ArrayList<Statistika> getPriemerVytazenostObsluznychOstatneCelkove() {
		return priemerVytazenostObsluznychOstatneCelkove;
	}

	public ArrayList<Statistika> getPriemerDlzkaRadovPriPokladniachCelkove() {
		return priemerDlzkaRadovPriPokladniachCelkove;
	}


	public ArrayList<Statistika> getPriemerDlzkaRadovPriPokladniach() {
		return priemerDlzkaRadovPriPokladniach;
	}

	public Statistika getPriemerDlzkaRaduPredObsluzOnline() {
		return priemerDlzkaRaduPredObsluzOnline;
	}

	public Statistika getPriemerDlzkaRaduPredObsluzNormal() {
		return priemerDlzkaRaduPredObsluzNormal;
	}

	public Statistika getPriemerDlzkaRaduPredObsluzOnlineCelkove() {
		return priemerDlzkaRaduPredObsluzOnlineCelkove;
	}

	public Statistika getPriemerDlzkaRaduPredObsluzNormalCelkove() {
		return priemerDlzkaRaduPredObsluzNormalCelkove;
	}

	public int getPocetObsluzenychZakaznikov() {
		return pocetObsluzenychZakaznikov;
	}

	public void setPocetObsluzenychZakaznikov(int pocetObsluzenychZakaznikov) {
		this.pocetObsluzenychZakaznikov = pocetObsluzenychZakaznikov;
	}

	public Statistika getPocetObsluzenychZakaznikovCelkove() {
		return pocetObsluzenychZakaznikovCelkove;
	}

	public double getZaciatokCasu() {
		return zaciatokCasu;
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModelu(new AgentModelu(Id.agentModelu, this, null));
		setAgentOkolia(new AgentOkolia(Id.agentOkolia, this, agentModelu()));
		setAgentPredajna(new AgentPredajna(Id.agentPredajna, this, agentModelu()));
		setAgentAutomat(new AgentAutomat(Id.agentAutomat, this, agentPredajna()));
		setAgentPokladne(new AgentPokladne(Id.agentPokladne, this, agentPredajna()));
		setAgentObsluzneMiesta(new AgentObsluzneMiesta(Id.agentObsluzneMiesta, this, agentPredajna()));
	}

	private AgentModelu _agentModelu;

public AgentModelu agentModelu()
	{ return _agentModelu; }

	public void setAgentModelu(AgentModelu agentModelu)
	{_agentModelu = agentModelu; }

	private AgentOkolia _agentOkolia;

public AgentOkolia agentOkolia()
	{ return _agentOkolia; }

	public void setAgentOkolia(AgentOkolia agentOkolia)
	{_agentOkolia = agentOkolia; }

	private AgentPredajna _agentPredajna;

public AgentPredajna agentPredajna()
	{ return _agentPredajna; }

	public void setAgentPredajna(AgentPredajna agentPredajna)
	{_agentPredajna = agentPredajna; }

	private AgentAutomat _agentAutomat;

public AgentAutomat agentAutomat()
	{ return _agentAutomat; }

	public void setAgentAutomat(AgentAutomat agentAutomat)
	{_agentAutomat = agentAutomat; }

	private AgentPokladne _agentPokladne;

public AgentPokladne agentPokladne()
	{ return _agentPokladne; }

	public void setAgentPokladne(AgentPokladne agentPokladne)
	{_agentPokladne = agentPokladne; }

	private AgentObsluzneMiesta _agentObsluzneMiesta;

public AgentObsluzneMiesta agentObsluzneMiesta()
	{ return _agentObsluzneMiesta; }

	public void setAgentObsluzneMiesta(AgentObsluzneMiesta agentObsluzneMiesta)
	{_agentObsluzneMiesta = agentObsluzneMiesta; }
	//meta! tag="end"
}