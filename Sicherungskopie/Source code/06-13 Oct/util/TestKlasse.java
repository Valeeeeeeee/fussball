package util;

import static util.Utilities.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestKlasse {
	
	public static void main(String[] args) {
		neueNachricht("4 mal Weltmeister");
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

	public static void neueNachricht(String meineNeueNachricht) {
		String filename = "/Users/valentinschraub/Desktop/test.txt";
		String[] vorherigeNachrichten = ausDatei(filename);
		
		String[] nachrichtenNeu = new String[vorherigeNachrichten.length + 1];
		for (int i = 0; i < vorherigeNachrichten.length; i++) {
			nachrichtenNeu[i] = vorherigeNachrichten[i];
		}
		nachrichtenNeu[nachrichtenNeu.length - 1] = meineNeueNachricht;
		
		inDatei(filename, nachrichtenNeu);
	}
	
}
