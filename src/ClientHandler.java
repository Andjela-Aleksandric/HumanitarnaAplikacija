import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientHandler implements Runnable {

	private Socket clientSocket;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			while (true) {
				String request = in.readLine();
				if (request == null) {
					break;
				}

				String[] requestData = request.split(",");
				String command = requestData[0];

				switch (command) {
				case "uplata":
					handleUplata(out, requestData);
				case "registracija":
					handleRegistration(out, requestData);
					break;
				case "prijava":
					handleSignIn(out, requestData);
					break;
				case "ukupno":
					prikazUkupno(out);
					break;
				case "korisnik":
					getKorisnik(out, requestData);
					break;
				case "prikaz":
					prikazPoslednjihTransakcija(out);
					break;
				default:
					System.out.println("Nepoznata komanda: " + command);
					break;
				}
			}

			System.out.println("Client diskonektovan: " + clientSocket.getInetAddress());
			clientSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getKorisnik(PrintWriter out, String[] requestData) {
		String username = requestData[1];
		Korisnik k = Server.registrovaniKorisnici.get(username);
		out.println(k.getIme() + "," + k.getPrezime() + "," + k.getBrojKartice());
	}

	private void prikazPoslednjihTransakcija(PrintWriter out) {
		Uplata[] uplate = Server.prikazPoslednjih();
		if (uplate == null) {
			out.println("Nema uplata!");
		} else {
			if (uplate.length <= 10) {
				String result = "";
				for (Uplata u : uplate) {
					String s = u.getIme() + " " + u.getPrezime() + " " + u.getDatum() + " " + u.getIznos() + " | ";
					result += s;
				}
				out.println(result);
			} else {
				int startIndex = Math.max(0, uplate.length - 10);

				String result = "";
				for (int i = startIndex; i < uplate.length; i++) {
					Uplata u = uplate[i];
					String s = u.getIme() + " " + u.getPrezime() + " " + u.getDatum() + " " + u.getIznos() + " | ";
					result += s;
				}
				out.println(result);
			}
		}
	}

	private void handleRegistration(PrintWriter out, String[] requestData) {
		String ime = requestData[1];
		String prezime = requestData[2];
		String username = requestData[3];
		String sifra = requestData[4];
		String jmbg = requestData[5];
		String brojKartice = requestData[6];
		String email = requestData[7];

		Korisnik noviKorisnik = new Korisnik(ime, prezime, username, sifra, jmbg, brojKartice, email);

		if (Server.registrovaniKorisnici.containsKey(username)) {
			out.println("Korisnik sa vec postojim pod username: " + username);
		} else {
			Server.registrovaniKorisnici.put(username, noviKorisnik);
			Server.sacuvajKorisnika(noviKorisnik);
			out.println("uspesno");
		}
	}

	private void prikazUkupno(PrintWriter out) {
		out.println(Server.ukupnoPrihoda + "");
	}

	private void handleSignIn(PrintWriter out, String[] requestData) {
		String username = requestData[1];
		String password = requestData[2];

		if (Server.validirajKorisnika(username, password)) {
			out.println("uspesno");
		} else {
			out.println("neuspesno");
		}
	}

	private void handleUplata(PrintWriter out, String[] data) {
		String ime = data[1];
		String prezime = data[2];
		String adresa = data[3];
		String brojKartice = data[4];
		String cvv = data[5];
		int iznos = Integer.parseInt(data[6]);

		if (Server.validirajKarticu(brojKartice, cvv) && validirajUplatu(iznos, cvv)) {
			Uplata uplata = new Uplata(ime, prezime, adresa, brojKartice, cvv, iznos, LocalDateTime.now());
			Server.uplate.put(iznos, uplata);
			Server.ukupnoPrihoda += Server.ukupnoPrihoda + iznos;
			Server.sacuvajUplatu(uplata);
			out.println("uspesno");
		} else {
			out.println("neuspesno");
		}
	}

	private static boolean validirajUplatu(int iznos, String cvv) {
		return cvv.matches("\\d{3}") && iznos >= 200;
	}
}
