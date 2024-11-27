import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	private static Socket socket;

	public static void main(String[] args) {
		String serverAddress = "localhost";
		int serverPort = 12345;

		try {
			socket = new Socket(serverAddress, serverPort);
			System.out.println("Uspesno povezan na server!");

			BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			int option = -1;

			do {
				System.out.println("\nIzaberi opciju:");
				System.out.println("1. Uplata humanitarne pomoci");
				System.out.println("2. Registrovanje");
				System.out.println("3. Prijavljivanje");
				System.out.println("4. Pregled ukupno prikupljenih sredstava");
				System.out.println("0. Izlaz");
				System.out.print("Unesi odgovarajuci broj(0-5): ");

				option = Integer.parseInt(consoleInput.readLine());

				switch (option) {
				case 0:
					System.exit(0);
				case 1:
					uplatiPomoc(consoleInput, out);
					break;
				case 2:
					registrovanje(consoleInput, out);
					break;
				case 3:
					prijavljivanje(consoleInput, out);
					break;
				case 4:
					prikazUkupnihSredstava(consoleInput, out);
					break;
				case 5:
					break;
				default:
					System.out.println("Nije odabrana validna opcija. Izlaz iz programa...");
					System.exit(0);
				}

			} while (option != 0);

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void registrovanje(BufferedReader consoleInput, PrintWriter out) throws IOException {
		System.out.println("=== Registracija ===");
		System.out.print("Ime: ");
		String ime = consoleInput.readLine();
		System.out.print("Prezime: ");
		String prezime = consoleInput.readLine();
		System.out.print("Username: ");
		String username = consoleInput.readLine();
		System.out.print("Sifra: ");
		String sifra = consoleInput.readLine();
		System.out.print("JMBG: ");
		String jmbg = consoleInput.readLine();
		System.out.print("Broj kartice: ");
		String brojKartice = consoleInput.readLine();
		System.out.print("Email: ");
		String email = consoleInput.readLine();

		out.println("registracija," + ime + "," + prezime + "," + username + "," + sifra + "," + jmbg + ","
				+ brojKartice + "," + email);
		String response = waitForResponse();
		System.out.println(response);

		if ("uspesno".equals(response)) {
			System.out.println("Korisnik uspesno registrovan.");
		} else {
			System.out.println("Korisnik nije uspesno registrovan!");
		}
	}

	private static void prijavljivanje(BufferedReader consoleInput, PrintWriter out) throws IOException {
		System.out.println("\n=== User Sign In ===");
		System.out.print("Username: ");
		String username = consoleInput.readLine();
		System.out.print("Password: ");
		String password = consoleInput.readLine();

		// Send sign-in data to the server
		out.println("prijava," + username + "," + password);
		String response = waitForResponse();

		if ("uspesno".equals(response)) {
			System.out.println("Korisnik uspesno ulogovan");
			out.println("korisnik," + username);
			response = waitForResponse();

			prikazMenu(response, consoleInput, out);
		} else {
			System.out.println("Username ili sifra nije dobra");
		}
	}

	private static void prikazMenu(String response, BufferedReader consoleInput, PrintWriter out) throws IOException {
		int newOption = -1;

		do {
			System.out.println("\nIzaberite opciju:");
			System.out.println("1. Uplata humanitarne pomoci");
			System.out.println("2. Prikaz poslednjih 10 transakcija");
			System.out.println("3. Prikaz ukupno skupljenog");
			System.out.println("0. Izlaz");
			System.out.print("Unesite odgovarajuci broj(0-3): ");

			newOption = Integer.parseInt(consoleInput.readLine());

			switch (newOption) {
			case 0:
				System.exit(0);
				break;
			case 1:
				uplatiPomocKorisnik(consoleInput, out, response);
				break;
			case 2:
				prikazPoslednjih10Transakcija(consoleInput, out);
				break;
			case 3:
				prikazUkupnihSredstava(consoleInput, out);
				break;
			default:
				System.out.println("Nije odabrana validna opcija. Izlaz");
			}

		} while (newOption != 0);

	}

	private static void prikazPoslednjih10Transakcija(BufferedReader consoleInput, PrintWriter out) throws IOException {
		System.out.println("=== Prikaz poslednjih 10 transakcija ===");
		out.println("prikaz,");
		String response = waitForResponse();

		System.out.println(response);
	}

	private static void uplatiPomocKorisnik(BufferedReader consoleInput, PrintWriter out, String res)
			throws IOException {
		System.out.println("\n=== Uplata pomoci ===");
		System.out.print("CVV: ");
		String cvv = consoleInput.readLine();
		System.out.print("Iznos: ");
		String iznos = consoleInput.readLine();

		String[] data = res.split(",");
		
		out.println("uplata," + data[0] + "," + data[1] + "," + "-" + "," + data[2] + "," + cvv
				+ "," + iznos);
		String response = waitForResponse();

		if ("uspesno".equals(response)) {
			System.out.println("Uspesno uplacen novac. Upisano u fajl.");
		} else {
			System.out.println("Neuspesno uplacen novac.");
		}
	}

	private static String waitForResponse() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		return in.readLine();
	}

	private static void uplatiPomoc(BufferedReader consoleInput, PrintWriter out) throws IOException {
		System.out.println("\n=== Uplata pomoci ===");
		System.out.print("Ime: ");
		String ime = consoleInput.readLine();
		System.out.print("Prezime: ");
		String prezime = consoleInput.readLine();
		System.out.print("Adresa: ");
		String adresa = consoleInput.readLine();
		System.out.print("Broj platne kartice(sa -): ");
		String kartica = consoleInput.readLine();
		System.out.print("CVV: ");
		String cvv = consoleInput.readLine();
		System.out.print("Iznos: ");
		String iznos = consoleInput.readLine();

		out.println("uplata," + ime + "," + prezime + "," + adresa + "," + kartica + "," + cvv + "," + iznos);
		String response = waitForResponse();

		if ("uspesno".equals(response)) {
			System.out.println("Uspesno uplacen novac. Upisano u fajl.");
		} else {
			System.out.println("Neuspesno uplacen novac.");
		}
	}

	private static void prikazUkupnihSredstava(BufferedReader consoleInput, PrintWriter out) throws IOException {
		System.out.println("\n=== Ukupno prikupljeno novca ===");
		out.println("ukupno,");
		String response = waitForResponse();

		System.out.println(response + " dinara");
	}
}
