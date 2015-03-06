package util;

import java.io.*;
import java.util.ArrayList;

import model.Ergebnis;

public class Utilities {
	
	public static void main(String[] args) {
//		testAusDatei();
		
		testErgebnisse();
	}
	
	private static void testErgebnisse() {
		String[] ergebnisStrings = new String[] {"2:4", "3:1nV (1:1)", "6:4nE (:2,1:1)"};
		Ergebnis[] ergebnisse = new Ergebnis[ergebnisStrings.length];
		
		for (int i = 0; i < ergebnisStrings.length; i++) {
			ergebnisse[i] = new Ergebnis(ergebnisStrings[i]);
			log(ergebnisStrings[i] + " oder " + ergebnisse[i].toString());
		}
		
		
	}
	
	private static void testAusDatei() {
		String[] strings = new String[0];
		String dateiname = "/Users/valentinschraub/Documents/workspace/Bundesliga/config.txt";
		strings = ausDatei(dateiname);
		for (int i = 0; i < strings.length; i++) {
			log(strings[i]);
		}
	}
	
	public static void log() {
		log("");
	}
	
	public static void log(Object object) {
		System.out.println(object);
	}
	
	public static void error(Object object) {
		System.err.println(object);
	}
	
	public static void logWONL(Object object) {
		System.out.print(object);
	}
	
	public static String[] ausDatei(String dateiname) {
		ArrayList<String> arraylist = new ArrayList<String>();
		try {
			File datei = new File(dateiname);
			BufferedReader in = null;
			if (!datei.exists()) {
				datei.createNewFile();
				log(" >>> File did not exist but was created! --> " + datei.getAbsolutePath());
			} else {
				String element;
				try {
					in = new BufferedReader(new FileReader(datei));
					while ((element = in.readLine()) != null) {
						arraylist.add(element);
					}
				} catch (Exception e) {
					log("Fehler beim Laden!");
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			log("Programmfehler!");
			e.printStackTrace();
		}
		String[] zielarray = new String[arraylist.size()];
		for (int i = 0; i < arraylist.size(); i++) {
			zielarray[i] = arraylist.get(i);
		}

		return zielarray;
	}
	
	public static void inDatei(String dateiname, String[] strings) {
		try {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(dateiname));
				for (int i = 0; i < strings.length; i++) {
					out.write(strings[i]);
					out.newLine();
				}
			} catch (Exception e) {
//				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			log(" >> in_datei >> Exception caught at file: " + dateiname + "\n");
		}
	}
}
