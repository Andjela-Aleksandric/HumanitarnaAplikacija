import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final String KORISNICI_FILE = "users.txt";
    private static final String KARTICE_FILE = "kartice.txt";
    private static final String UPLATE_FILE = "uplate.txt";
    public static Map<String, Korisnik> registrovaniKorisnici = new HashMap<>();
    public static Map<String, Kartica> kartice = new HashMap<>();
    public static Map<Integer, Uplata> uplate = new HashMap<>();
    public static int ukupnoPrihoda;
    public static String poslednjeTransakcije;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10); 

    public static void main(String[] args) {
        ucitajRegistrovaneKorisnike();
        ucitajKartice();
        ucitajUplate();

        int port = 12345;

        try {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Server je na portu: " + port);

				while (true) {
					Socket clientSocket = serverSocket.accept();
				    System.out.println("Client povezan: " + clientSocket.getInetAddress());

				    executorService.submit(new ClientHandler(clientSocket));
				}
			}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void ucitajRegistrovaneKorisnike() {
        try (BufferedReader reader = new BufferedReader(new FileReader(KORISNICI_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Korisnik korisnik = new Korisnik(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
                registrovaniKorisnici.put(korisnik.getUsername(), korisnik);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Uplata[] prikazPoslednjih() {
    	return uplate.values().toArray(new Uplata[uplate.size()]);
    }
    
    private static void ucitajUplate() {
    	try (BufferedReader reader = new BufferedReader(new FileReader(UPLATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Uplata uplata = new Uplata(data[0], data[1], data[2], data[3], data[4], Integer.parseInt(data[5]), LocalDateTime.parse(data[6]));
                uplate.put(uplata.getIznos(), uplata);
            }
            ukupnoPrihoda();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void ukupnoPrihoda() {
    	ukupnoPrihoda = 0;
    	
    	for (Integer iznos: uplate.keySet()) {
    		ukupnoPrihoda += iznos;
    	}
    }
    
    private static void ucitajKartice() {
    	try (BufferedReader reader = new BufferedReader(new FileReader(KARTICE_FILE))){
    		String line;
            while ((line = reader.readLine()) != null) {
                String[] karticeData = line.split(",");
                Kartica kartica = new Kartica(karticeData[0], karticeData[1]);
                kartice.put(kartica.getBrojKartice(), kartica);
            }
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void sacuvajKorisnika(Korisnik korisnik) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KORISNICI_FILE, true))) {
            writer.write(korisnik.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sacuvajUplatu(Uplata uplata) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(UPLATE_FILE, true))) {
            writer.write(uplata.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean validirajKorisnika(String username, String sifra) {
        Korisnik korisnik = registrovaniKorisnici.get(username);
        return korisnik != null && korisnik.getSifra().equals(sifra);
    }
    
    public static boolean validirajKarticu(String brojKartice, String cvv) {
    	Kartica kartica = kartice.get(brojKartice);
    	return kartica != null && kartica.getCvv().equals(cvv);
    }
}
