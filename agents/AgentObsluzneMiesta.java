package agents;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import Objects.Statistika;
import Osoby.Osoba;
import Osoby.OsobaComparatorNoPriority;
import Osoby.OsobaComparatorPriority;
import Osoby.TypZakaznika;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

//meta! id="7"
public class AgentObsluzneMiesta extends Agent
{
	private ArrayList<Stat> priemerVytazenostObsluznychOnline;
	private ArrayList<Stat> priemerVytazenostObsluznychOstatne;
	private WStat priemerDlzkaRaduPredObsluzOnline;
	private WStat priemerDlzkaRaduPredObsluzNormal;
	private Queue<Osoba> osobyQueue;
	private Queue<Osoba> onlineQueue;
	private boolean[] normalneObsluzne;
	private boolean[] onlineObsluzne;

	public AgentObsluzneMiesta(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		priemerVytazenostObsluznychOnline = new ArrayList<>();
		priemerVytazenostObsluznychOstatne = new ArrayList<>();
		for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
			priemerVytazenostObsluznychOnline.add(new Stat());
		}
		for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
			priemerVytazenostObsluznychOstatne.add(new Stat());
		}
		priemerDlzkaRaduPredObsluzOnline = new WStat(mySim());
		priemerDlzkaRaduPredObsluzNormal = new WStat(mySim());
		normalneObsluzne = new boolean[Config.pocetNormalObsluznych];
		onlineObsluzne = new boolean[Config.pocetOnlineObsluznych];
		for (int i = 0; i < Config.pocetNormalObsluznych; i++) {
			normalneObsluzne[i] = true;
		}
		for (int i = 0; i < Config.pocetOnlineObsluznych; i++) {
			onlineObsluzne[i] = true;
		}
		osobyQueue = new PriorityQueue<>(new OsobaComparatorPriority());
		onlineQueue = new PriorityQueue<>(new OsobaComparatorNoPriority());
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerObsluzneMiesta(Id.managerObsluzneMiesta, mySim(), this);
		new Scheduler1(Id.scheduler1, mySim(), this);
		new ProcesObsluhy(Id.procesObsluhy, mySim(), this);
		new DotazZaradNaObsluzne(Id.dotazZaradNaObsluzne, mySim(), this);
		new DotazZmestiSaDoObsluznych(Id.dotazZmestiSaDoObsluznych, mySim(), this);
		addOwnMessage(Mc.uvolneniePredajne);
		addOwnMessage(Mc.obsluhaZakaznika);
		addOwnMessage(Mc.startObednejPrestavky);
		addOwnMessage(Mc.obsluhaHotova);
	}
	//meta! tag="end"


	public Queue<Osoba> getOsobyQueue() {
		return osobyQueue;
	}

	public Queue<Osoba> getOnlineQueue() {
		return onlineQueue;
	}

	public boolean[] getNormalneObsluzne() {
		return normalneObsluzne;
	}

	public boolean[] getOnlineObsluzne() {
		return onlineObsluzne;
	}

	public ArrayList<Stat> getPriemerVytazenostObsluznychOnline() {
		return priemerVytazenostObsluznychOnline;
	}

	public ArrayList<Stat> getPriemerVytazenostObsluznychOstatne() {
		return priemerVytazenostObsluznychOstatne;
	}

	public WStat getPriemerDlzkaRaduPredObsluzOnline() {
		return priemerDlzkaRaduPredObsluzOnline;
	}

	public WStat getPriemerDlzkaRaduPredObsluzNormal() {
		return priemerDlzkaRaduPredObsluzNormal;
	}
	public void zaradDoRadu(Osoba osoba) {
		if (osoba.getTypZakaznika() == TypZakaznika.ONLINE) {
			onlineQueue.add(osoba);
		} else {
			osobyQueue.add(osoba);
		}
	}
}