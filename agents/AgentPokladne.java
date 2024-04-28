package agents;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import Objects.Statistika;
import Osoby.Osoba;
import Osoby.OsobaComparatorNoPriority;
import simulation.*;
import managers.*;
import continualAssistants.*;
import instantAssistants.*;

import java.util.ArrayList;
import java.util.PriorityQueue;

//meta! id="6"
public class AgentPokladne extends Agent
{
	private ArrayList<Stat> priemerVytazenostPokladni;

	private ArrayList<WStat> priemerDlzkaRadovPriPokladniach;
	private boolean[] pokladne;
	private PriorityQueue<Osoba>[] rady;

	public AgentPokladne(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
		priemerDlzkaRadovPriPokladniach = new ArrayList<>();
		priemerVytazenostPokladni = new ArrayList<>();
		for (int i = 0; i < Config.pocetPokladni; i++) {
			priemerVytazenostPokladni.add(new Stat());
			priemerDlzkaRadovPriPokladniach.add(new WStat(mySim()));
		}
		pokladne = new boolean[Config.pocetPokladni];
		rady = (PriorityQueue<Osoba>[]) new PriorityQueue[Config.pocetPokladni];
		for (int i = 0; i < Config.pocetPokladni; i++) {
			rady[i] = new PriorityQueue<>(new OsobaComparatorNoPriority());
		}
		for (int i = 0; i < Config.pocetPokladni; i++) {
			pokladne[i] = true;
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerPokladne(Id.managerPokladne, mySim(), this);
		new Scheduler2(Id.scheduler2, mySim(), this);
		new ProcesPlatenia(Id.procesPlatenia, mySim(), this);
		new DotazNaZaradenieDoRaduPriPokladniach(Id.dotazNaZaradenieDoRaduPriPokladniach, mySim(), this);
		new DotazNaZaradneieDoPokladne(Id.dotazNaZaradneieDoPokladne, mySim(), this);
		addOwnMessage(Mc.init);
		addOwnMessage(Mc.platenieHotove);
		addOwnMessage(Mc.startObednejPrestavky);
		addOwnMessage(Mc.platenieUPokoladne);
	}
	//meta! tag="end"


	public boolean[] getPokladne() {
		return pokladne;
	}

	public PriorityQueue<Osoba>[] getRady() {
		return rady;
	}

	public ArrayList<Stat> getPriemerVytazenostPokladni() {
		return priemerVytazenostPokladni;
	}

	public ArrayList<WStat> getPriemerDlzkaRadovPriPokladniach() {
		return priemerDlzkaRadovPriPokladniach;
	}
}