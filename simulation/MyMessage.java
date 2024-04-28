package simulation;

import OSPABA.*;
import Osoby.Osoba;

public class MyMessage extends MessageForm
{
	private Osoba zakaznik;
	private int cisloPokladne;
	private int cisloObsluzneho;
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
		cisloObsluzneho = -1;
		cisloPokladne = -1;
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
		zakaznik = original.zakaznik;
		cisloPokladne = original.cisloPokladne;
		cisloObsluzneho = original.cisloObsluzneho;
	}

	public Osoba getZakaznik() {
		return zakaznik;
	}

	public void setZakaznik(Osoba zakaznik) {
		this.zakaznik = zakaznik;
	}

	public int getCisloPokladne() {
		return cisloPokladne;
	}

	public void setCisloPokladne(int cisloPokladne) {
		this.cisloPokladne = cisloPokladne;
	}

	public int getCisloObsluzneho() {
		return cisloObsluzneho;
	}

	public void setCisloObsluzneho(int cisloObsluzneho) {
		this.cisloObsluzneho = cisloObsluzneho;
	}
}