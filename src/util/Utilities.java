package util;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import model.Ergebnis;
import model.MyDate;
import model.Spieler;
import model.Start;

public class Utilities {
	
	public static final Color colorCategory1 = new Color(0, 200, 0);
	public static final Color colorCategory2 = new Color(128, 255, 0);
	public static final Color colorCategory3 = new Color(255, 255, 0);
	public static final Color colorCategory4 = new Color(255, 128, 0);
	public static final Color colorCategory5 = new Color(255, 0, 0);
	
	public static final int STARTX = 0;
	public static final int STARTY = 1;
	public static final int GAPX = 2;
	public static final int GAPY = 3;
	public static final int SIZEX = 4;
	public static final int SIZEY = 5;
	private static boolean osX = true;
	
	public static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	
	public static void main(String[] args) {
		
		boolean testing = false;
		if (testing) {
			testAusDatei();
			testErgebnisse();
		}
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
		ArrayList<String> strings = new ArrayList<>();
		String dateiname = "/Users/valentinschraub/Documents/workspace/Fussball/config.txt";
		strings = ausDatei(dateiname);
		for (int i = 0; i < strings.size(); i++) {
			log(strings.get(i));
		}
	}
	
	public static void repaintImmediately(JComponent component) {
		component.paintImmediately(0, 0, component.getWidth(), component.getHeight());
	}
	
	public static void message(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public static void message(String message, String title) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	public static void errorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public static String inputDialog(String message) {
		return JOptionPane.showInputDialog(message);
	}
	
	public static String inputDialog(String message, String initialValue) {
		return JOptionPane.showInputDialog(message, initialValue);
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
	
	public static void logXcharacters(String text, int x) {
		log();
		for (int i = 0; i < text.length(); i += x) {
			log(text.substring(i, (i + x < text.length() ? i + x : text.length())));
		}
		log();
	}
	
	public static String removeUmlaute(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == 196)				name = name.substring(0, i) + "Ae" + name.substring(i + 1);
			else if (name.charAt(i) == 214)			name = name.substring(0, i) + "Oe" + name.substring(i + 1);
			else if (name.charAt(i) == 220)			name = name.substring(0, i) + "Ue" + name.substring(i + 1);
			else if (name.charAt(i) == 228)			name = name.substring(0, i) + "ae" + name.substring(i + 1);
			else if (name.charAt(i) == 246)			name = name.substring(0, i) + "oe" + name.substring(i + 1);
			else if (name.charAt(i) == 252)			name = name.substring(0, i) + "ue" + name.substring(i + 1);
			else if (name.charAt(i) == 223)			name = name.substring(0, i) + "ss" + name.substring(i + 1);
			else if (name.charAt(i) == 193)			name = name.substring(0, i) + "A" + name.substring(i + 1);
			else if (name.charAt(i) == 201)			name = name.substring(0, i) + "E" + name.substring(i + 1);
			else if (name.charAt(i) == 205)			name = name.substring(0, i) + "I" + name.substring(i + 1);
			else if (name.charAt(i) == 209)			name = name.substring(0, i) + "N" + name.substring(i + 1);
			else if (name.charAt(i) == 211)			name = name.substring(0, i) + "O" + name.substring(i + 1);
			else if (name.charAt(i) == 225)			name = name.substring(0, i) + "a" + name.substring(i + 1);
			else if (name.charAt(i) == 233)			name = name.substring(0, i) + "e" + name.substring(i + 1);
			else if (name.charAt(i) == 237)			name = name.substring(0, i) + "i" + name.substring(i + 1);
			else if (name.charAt(i) == 241)			name = name.substring(0, i) + "n" + name.substring(i + 1);
			else if (name.charAt(i) == 243)			name = name.substring(0, i) + "o" + name.substring(i + 1);
		}
		
		return name;
	}
	
	public static boolean inThePast(int date, int time) {
		if (date < Start.today())	return true;
		if (date > Start.today())	return false;
		return time < MyDate.newMyTime();
	}
	
	public static ArrayList<Spieler> cloneList(ArrayList<Spieler> list) {
		ArrayList<Spieler> clone = new ArrayList<>();
		
		for (Spieler spieler : list) {
			clone.add(spieler);
		}
		
		return clone;
	}
	
	public static String[] ausDateiArray(String dateiname) {
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
					in = new BufferedReader(new InputStreamReader(new FileInputStream(dateiname), "UTF-8"));
					while ((element = in.readLine()) != null) {
						if (!element.isEmpty()) {
							arraylist.add(element);
						}
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
		} catch (IOException ioe) {
			log("No such file or directory: " + dateiname);
//			ioe.printStackTrace();
		}
		String[] zielarray = new String[arraylist.size()];
		for (int i = 0; i < arraylist.size(); i++) {
			zielarray[i] = arraylist.get(i);
		}
		
		return zielarray;
	}
	
	public static ArrayList<String> ausDatei(String dateiname) {
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
					in = new BufferedReader(new InputStreamReader(new FileInputStream(dateiname), "UTF-8"));
					while ((element = in.readLine()) != null) {
						if (!element.isEmpty()) {
							arraylist.add(element.replace("" + (char) 65279, ""));
						}
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
		} catch (IOException ioe) {
			log("No such file or directory: " + dateiname);
//			ioe.printStackTrace();
		}
		
		return arraylist;
	}
	
	public static void inDatei(String dateiname, String[] strings) {
		try {
			File file = new File(dateiname);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dateiname), "UTF-8"));
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
		} catch (IOException ioe) {
			log(" >> inDatei >> No such file or directory: " + dateiname + "\n");
//			ioe.printStackTrace();
		}
	}
	
	public static void inDatei(String dateiname, ArrayList<String> strings) {
		try {
			File file = new File(dateiname);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dateiname), "UTF-8"));
				for (int i = 0; i < strings.size(); i++) {
					out.write(strings.get(i));
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
		} catch (IOException ioe) {
			log(" >> inDatei >> No such file or directory: " + dateiname + "\n");
//			ioe.printStackTrace();
		}
	}
	
	public static int getWindowDecorationWidth() {
		if (osX)	return 0;
		else		return 6;
	}
	
	public static int getWindowDecorationHeight() {
		if (osX)	return 22;
		else		return 28;
	}
}
