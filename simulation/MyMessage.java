package simulation;

import OSPABA.*;
import Osoby.Osoba;

public class MyMessage extends MessageForm
{
	private Osoba zakaznik;
	public MyMessage(Simulation sim)
	{
		super(sim);
	}

	public MyMessage(MyMessage original)
	{
		super(original);
		// copy() is called in superclass
	}

	public MyMessage(Simulation sim, Osoba osoba)
	{
		super(sim);
		// copy() is called in superclass
		zakaznik = osoba;
	}

	@Override
	public MessageForm createCopy()
	{
		return new MyMessage(this);
	}

	@Override
	protected void copy(MessageForm message)
	{
		super.copy(message);
		MyMessage original = (MyMessage)message;
		// Copy attributes
	}

	public Osoba getZakaznik() {
		return zakaznik;
	}

	public void setZakaznik(Osoba zakaznik) {
		this.zakaznik = zakaznik;
	}
}
