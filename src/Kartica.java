
public class Kartica {

	private String brojKartice;
	private String cvv;

	public Kartica() {
		super();
	}

	public Kartica(String brojKartice, String cvv) {
		super();
		this.brojKartice = brojKartice;
		this.cvv = cvv;
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

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	@Override
	public String toString() {
		return "Kartica [brojKartice=" + brojKartice + ", cvv=" + cvv + "]";
	}

}
