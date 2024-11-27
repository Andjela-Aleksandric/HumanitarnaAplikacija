import java.time.LocalDateTime;

public class Uplata {

	private String ime;
	private String prezime;
	private String adresa;
	private String brojKartice;
	private String cvv;
	private int iznos;
	private LocalDateTime datum;

	public LocalDateTime getDatum() {
		return datum;
	}

	public void setDatum(LocalDateTime datum) {
		this.datum = datum;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public void setIznos(int iznos) {
		this.iznos = iznos;
	}

	public Uplata(String ime, String prezime, String adresa, String brojKartice, String cvv, int iznos) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brojKartice = brojKartice;
		this.cvv = cvv;
		this.iznos = iznos;
		this.datum = LocalDateTime.now();
	}

	public Uplata(String ime, String prezime, String adresa, String brojKartice, String cvv, int iznos,
			LocalDateTime datum) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.adresa = adresa;
		this.brojKartice = brojKartice;
		this.cvv = cvv;
		this.iznos = iznos;
		this.datum = datum;
	}

	public int getIznos() {
		return iznos;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBrojKartice() {
		return brojKartice;
	}

	public void setBrojKartice(String brojKartice) {
		this.brojKartice = brojKartice;
	}

	public String getCvv() {
		return cvv;
	}

	@Override
	public String toString() {
		return ime + "," + prezime + "," + adresa + "," + brojKartice + "," + cvv + "," + iznos + "," + datum;
	}

}
