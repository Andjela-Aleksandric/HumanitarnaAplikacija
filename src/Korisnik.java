public class Korisnik {

	private String ime;
	private String prezime;
	private String username;
	private String sifra;
	private String jmbg;
	private String brojKartice;
	private String email;

	public Korisnik(String ime, String prezime, String username, String sifra, String jmbg, String brojKartice,
			String email) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.sifra = sifra;
		this.jmbg = jmbg;
		this.brojKartice = brojKartice;
		this.email = email;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSifra() {
		return sifra;
	}

	public void setSifra(String sifra) {
		this.sifra = sifra;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
        if (jmbg.matches("\\d{13}")) {
            this.jmbg = jmbg;
        } else {
            throw new IllegalArgumentException("JMBG nije u dobrom formatu!");
        }
	}

	public String getBrojKartice() {
		return brojKartice;
	}

	public void setBrojKartice(String brojKartice) {
		String kartica = brojKartice.replaceAll("-", "");

        if (kartica.matches("\\d{16}")) {
            this.brojKartice = kartica.substring(0, 4) + "-"
                             + kartica.substring(4, 8) + "-"
                             + kartica.substring(8, 12) + "-"
                             + kartica.substring(12);
        } else {
            throw new IllegalArgumentException("Kartica nije dobrog formata!");
        }
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return ime + "," + prezime + "," + username + "," + sifra + "," + jmbg + "," + brojKartice + "," + email;
	}
}
