package simulation;

import OSPABA.*;
import OSPABA.concurrent.ConcurrentSimulation;
import OSPABA.concurrent.ResultsSynchronizer;
import OSPStat.Stat;
import Objects.Statistika;
import agents.*;

import java.util.*;
import java.util.function.Supplier;

public class MySimulation extends ConcurrentSimulation
{

	private Stat priemerDlzkaRaduAutomatCelkove;
	private Stat priemerPocetLudiCelkovy;
	private Stat priemerCasVObchodeCelkovy;
	private Stat priemerCakanieVRadePredAutomatomCalkovy;
	private Stat pocetObsluzenychZakaznikovCelkove;
	private Stat priemerPoslednyOdchod;


	private Stat priemerVytazenieAutomatuCelkove;
	private ArrayList<Stat> priemerVytazenostPokladniCelkove;
	private ArrayList<Stat> priemerVytazenostObsluznychOnlineCelkove;
	private ArrayList<Stat> priemerVytazenostObsluznychOstatneCelkove;
	private ArrayList<Stat> priemerDlzkaRadovPriPokladniachCelkove;

	private Stat priemerDlzkaRaduPredObsluzOnlineCelkove;
	private Stat priemerDlzkaRaduPredObsluzNormalCelkove;

	private int pocetObsluzenychZakaznikov;

	private double zaciatokCasu;
	private double koniecCasu;
	private int pocetObsluznych;
	private ArrayList<String> stavyOsob;

	public MySimulation()
	{
		init();
	}

	@Override
	protected int getThreadCount() {
		return Config.threadCount;
	}

	@Override
	protected Supplier<ResultsSynchronizer> getResultSynchronizerSupplier() {
		return null;
	}

	@Override
	protected ConcurrentSimulation createCopy() {
		return new MySimulation();
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis

		priemerPocetLudiCelkovy = new Stat();
		priemerCasVObchodeCelkovy = new Stat();
		priemerDlzkaRaduAutomatCelkove = new Stat();
		priemerCakanieVRadePredAutomatomCalkovy = new Stat();
		pocetObsluzenychZakaznikovCelkove = new Stat();
		priemerPoslednyOdchod = new Stat();
		priemerVytazenostObsluznychOnlineCelkove = new ArrayList<>();
		priemerVytazenostPokladniCelkove = new ArrayList<>();
		priemerDlzkaRadovPriPokladniachCelkove = new ArrayList<>();
		priemerVytazenostObsluznychOstatneCelkove = new ArrayList<>();
		priemerDlzkaRaduPredObsluzOnlineCelkove = new Stat();
		priemerDlzkaRaduPredObsluzNormalCelkove = new Stat();


		for (int i = 0; i < Config.pocetPokladni; i++) {
			priemerVytazenostPokladniCelkove.add(new Stat());
			priemerDlzkaRadovPriPokladniachCelkove.add(new Stat());
		}
		for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
			priemerVytazenostObsluznychOnlineCelkove.add(new Stat());
		}
		for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
			priemerVytazenostObsluznychOstatneCelkove.add(new Stat());
		}

		priemerVytazenieAutomatuCelkove = new Stat();

	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...
		stavyOsob = new ArrayList<>();
		pocetObsluzenychZakaznikov = 0;
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();

		priemerPocetLudiCelkovy.addSample(agentOkolia().pocetZakaznikov());
		priemerDlzkaRaduAutomatCelkove.addSample(agentAutomat().getPriemerDlzkaRaduAutomat().mean());
		priemerCasVObchodeCelkovy.addSample(agentOkolia().getPriemerCasVObchode().mean());
		priemerCakanieVRadePredAutomatomCalkovy.addSample(agentAutomat().getPriemerCakanieVRadePredAutomatom().mean());
		priemerPoslednyOdchod.addSample(currentTime());
		priemerVytazenieAutomatuCelkove.addSample(agentAutomat().getPriemerVytazenieAutomatu().sampleSize());
		priemerDlzkaRaduPredObsluzOnlineCelkove.addSample(agentObsluzneMiesta().getPriemerDlzkaRaduPredObsluzOnline().mean());
		priemerDlzkaRaduPredObsluzNormalCelkove.addSample(agentObsluzneMiesta().getPriemerDlzkaRaduPredObsluzNormal().mean());
		pocetObsluzenychZakaznikovCelkove.addSample(agentOkolia().pocetZakaznikov());
		//treba rozoznat celkove a iba co sa dostali do modelu statistiky

		for (int i = 0; i < Config.pocetPokladni; i++) {
			priemerVytazenostPokladniCelkove.get(i).addSample(agentPokladne().getPriemerVytazenostPokladni().get(i).sampleSize());
			priemerDlzkaRadovPriPokladniachCelkove.get(i).addSample(agentPokladne().getPriemerDlzkaRadovPriPokladniach().get(i).mean());
		}
		for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
			priemerVytazenostObsluznychOnlineCelkove.get(i).addSample(agentObsluzneMiesta().getPriemerVytazenostObsluznychOnline().get(i).sampleSize());
		}
		for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
			priemerVytazenostObsluznychOstatneCelkove.get(i).addSample(agentObsluzneMiesta().getPriemerVytazenostObsluznychOstatne().get(i).sampleSize());
		}

	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
		System.out.println("Primer pocet ludi: " + priemerPocetLudiCelkovy.mean());
		System.out.println("Primer dlzka radu: " + priemerDlzkaRaduAutomatCelkove.mean());
		System.out.println("Primer cas v obchode: " + priemerCasVObchodeCelkovy.mean());
		System.out.println("Priemer cakanie pred automatom: " + priemerCakanieVRadePredAutomatomCalkovy.mean());
		System.out.println("Posledny cas odchodu: " + priemerPoslednyOdchod.mean());
		System.out.println("Vytazenie automatau:" + priemerVytazenieAutomatuCelkove.mean()*100);
		for (int i = 0; i < Config.pocetPokladni; i++) {
			System.out.println(priemerVytazenostPokladniCelkove.get(i).mean()*100);
			System.out.println(priemerDlzkaRadovPriPokladniachCelkove.get(i).mean());
		}
		for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
			System.out.println(priemerVytazenostObsluznychOnlineCelkove.get(i).mean());
		}
		for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
			System.out.println(priemerVytazenostObsluznychOstatneCelkove.get(i).mean());
		}
		System.out.println(Arrays.toString(priemerCasVObchodeCelkovy.confidenceInterval_95()));
		System.out.println(priemerDlzkaRaduPredObsluzOnlineCelkove.mean());
		System.out.println(priemerDlzkaRaduPredObsluzNormalCelkove.mean());

	}


	public Stat getPriemerDlzkaRaduAutomatCelkove() {
		return priemerDlzkaRaduAutomatCelkove;
	}

	public Stat getPriemerPocetLudiCelkovy() {
		return priemerPocetLudiCelkovy;
	}

	public Stat getPriemerCasVObchodeCelkovy() {
		return priemerCasVObchodeCelkovy;
	}

	public Stat getPriemerCakanieVRadePredAutomatomCalkovy() {
		return priemerCakanieVRadePredAutomatomCalkovy;
	}

	public Stat getPocetObsluzenychZakaznikovCelkove() {
		return pocetObsluzenychZakaznikovCelkove;
	}

	public Stat getPriemerPoslednyOdchod() {
		return priemerPoslednyOdchod;
	}

	public Stat getPriemerVytazenieAutomatuCelkove() {
		return priemerVytazenieAutomatuCelkove;
	}

	public ArrayList<Stat> getPriemerVytazenostPokladniCelkove() {
		return priemerVytazenostPokladniCelkove;
	}

	public ArrayList<Stat> getPriemerVytazenostObsluznychOnlineCelkove() {
		return priemerVytazenostObsluznychOnlineCelkove;
	}

	public ArrayList<Stat> getPriemerVytazenostObsluznychOstatneCelkove() {
		return priemerVytazenostObsluznychOstatneCelkove;
	}

	public ArrayList<Stat> getPriemerDlzkaRadovPriPokladniachCelkove() {
		return priemerDlzkaRadovPriPokladniachCelkove;
	}

	public Stat getPriemerDlzkaRaduPredObsluzOnlineCelkove() {
		return priemerDlzkaRaduPredObsluzOnlineCelkove;
	}

	public Stat getPriemerDlzkaRaduPredObsluzNormalCelkove() {
		return priemerDlzkaRaduPredObsluzNormalCelkove;
	}

	public int getPocetObsluzenychZakaznikov() {
		return pocetObsluzenychZakaznikov;
	}

	public void setStavyOsob(ArrayList<String> stavyOsob) {
		this.stavyOsob.clear();
		this.stavyOsob = stavyOsob;
	}
	public ArrayList<String> getStavyOsob() {
		return stavyOsob;
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