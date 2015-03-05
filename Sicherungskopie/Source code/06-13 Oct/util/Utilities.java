package util;

import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import model.Ergebnis;

public class Utilities {
	
	public static void main(String[] args) {
//		testAusDatei();
		
		testErgebnisse();
	}
	
	private static void testErgebnisse() {
		String[] ergebnisStrings = new String[] {"2:4", "3:1nV (1:1)", "6:4nE (:2,1:1)", "5:5nE (3:3)", "2:1nV", "6:5nE (2:4,1:2)", "6:5nE (1:2,1:2)"};
		Ergebnis[] ergebnisse = new Ergebnis[ergebnisStrings.length];
		
		for (int i = 0; i < ergebnisStrings.length; i++) {
			ergebnisse[i] = new Ergebnis(ergebnisStrings[i]);
			log(ergebnisStrings[i] + " oder " + ergebnisse[i].toString());
			log();
		}
		
		
	}
	
	private static void testAusDatei() {
		String[] strings = new String[0];
		String dateiname = "/Users/valentinschraub/Documents/workspace/Fussball/config.txt";
		strings = ausDatei(dateiname);
		for (int i = 0; i < strings.length; i++) {
			log(strings[i]);
		}
	}
	
	public static void message(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public static void errorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String inputDialog(String message) {
		return JOptionPane.showInputDialog(message);
	}
	
	public static int yesNoDialog(String message) {
		return JOptionPane.showConfirmDialog(null, message, "", JOptionPane.YES_NO_OPTION);
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
			
			File file = new File(dateiname);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new FileWriter(dateiname));
				for (int i = 0; i < strings.length; i++) {
					out.write(strings[i]);
					out.newLine();
				}
			} catch (Exception e) {
				log(e.getClass().getName() + " while writing in file " + dateiname);
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
			log(" >> inDatei >> Exception caught at file: " + dateiname + "\n");
			e.printStackTrace();
		}
	}
}

