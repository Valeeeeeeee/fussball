package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import model.MyDate;
import model.Spieler;
import model.Start;

public class Utilities {
	
	public static final Color colorCategory1 = new Color(0, 200, 0);
	public static final Color colorCategory2 = new Color(128, 255, 0);
	public static final Color colorCategory3 = new Color(255, 255, 0);
	public static final Color colorCategory4 = new Color(255, 128, 0);
	public static final Color colorCategory5 = new Color(255, 0, 0);
	public static final Color colorHomescreen = new Color(255, 255, 255);
	
	public static final int STARTX = 0;
	public static final int STARTY = 1;
	public static final int GAPX = 2;
	public static final int GAPY = 3;
	public static final int SIZEX = 4;
	public static final int SIZEY = 5;
	private static boolean osX = System.getProperty("os.name").startsWith("Mac OS X");
	
	public static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	public static String[] wochentage = {"Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag"};
	
	
	public static void alignLeft(JLabel label) {
		label.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	public static void alignCenter(JLabel label) {
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static void alignRight(JLabel label) {
		label.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	public static void alignLeft(JTextField textField) {
		textField.setHorizontalAlignment(SwingConstants.LEFT);
	}
	
	public static void alignCenter(JTextField textField) {
		textField.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static void alignRight(JTextField textField) {
		textField.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	public static Image resizeImage(Image image, int width, int height) {
	    BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = resizedImage.createGraphics();

	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    
	    g.drawImage(image, 0, 0, width, height, null);
	    g.dispose();
	    
	    return resizedImage;
	}
	
	public static void removeAllMouseListeners(Component comp) {
		MouseListener[] mls = comp.getMouseListeners();
		for (int i = 0; i < mls.length; i++) {
			comp.removeMouseListener(mls[0]);
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
	
	public static String arrowDown() {
		return osX ? "\u2b07" : "\u2193";
	}
	
	public static String arrowUp() {
		return osX ? "\u2b06" : "\u2191";
	}
	
	public static int numberOfDaysInMonth(int month, int year) {
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				if (year % 4 != 0)	return 28;
				if (year % 100 == 0 && year % 400 != 0)	return 28;
				return 29;
			default:
				return 0;
		}
	}
	
	public static String removeUmlaute(String name) {
		for (int i = 0; i < name.length(); i++) {
			int chAt = name.charAt(i);
			
			if (chAt == 196 || chAt == 198)			name = name.substring(0, i) + "Ae" + name.substring(i + 1);
			else if (192 <= chAt && chAt <= 197)	name = name.substring(0, i) + "A" + name.substring(i + 1);
			else if (chAt == 199)					name = name.substring(0, i) + "C" + name.substring(i + 1);
			else if (200 <= chAt && chAt <= 203)	name = name.substring(0, i) + "E" + name.substring(i + 1);
			else if (204 <= chAt && chAt <= 207)	name = name.substring(0, i) + "I" + name.substring(i + 1);
			else if (chAt == 209)					name = name.substring(0, i) + "N" + name.substring(i + 1);
			else if (210 <= chAt && chAt <= 213)	name = name.substring(0, i) + "O" + name.substring(i + 1);
			else if (chAt == 214 || chAt == 216)	name = name.substring(0, i) + "Oe" + name.substring(i + 1);
			else if (217 <= chAt && chAt <= 219)	name = name.substring(0, i) + "U" + name.substring(i + 1);
			else if (chAt == 220)					name = name.substring(0, i) + "Ue" + name.substring(i + 1);
			else if (chAt == 223)					name = name.substring(0, i) + "ss" + name.substring(i + 1);
			else if (chAt == 228 || chAt == 230)	name = name.substring(0, i) + "ae" + name.substring(i + 1);
			else if (224 <= chAt && chAt <= 229)	name = name.substring(0, i) + "a" + name.substring(i + 1);
			else if (chAt == 231)					name = name.substring(0, i) + "c" + name.substring(i + 1);
			else if (232 <= chAt && chAt <= 235)	name = name.substring(0, i) + "e" + name.substring(i + 1);
			else if (236 <= chAt && chAt <= 239)	name = name.substring(0, i) + "i" + name.substring(i + 1);
			else if (chAt == 241)					name = name.substring(0, i) + "n" + name.substring(i + 1);
			else if (242 <= chAt && chAt <= 245)	name = name.substring(0, i) + "o" + name.substring(i + 1);
			else if (chAt == 246 || chAt == 248)	name = name.substring(0, i) + "oe" + name.substring(i + 1);
			else if (249 <= chAt && chAt <= 251)	name = name.substring(0, i) + "u" + name.substring(i + 1);
			else if (chAt == 252)					name = name.substring(0, i) + "ue" + name.substring(i + 1);
		}
		
		return name;
	}
	
	public static <E> boolean isIn(E obj, E[] array) {
		for (int i = 0; i < array.length; i++) {
			if (obj.equals(array[i]))	return true;
		}
		return false;
	}
	
	public static <E> boolean isElementOf(E obj, ArrayList<E> list) {
		for (int i = 0; i < list.size(); i++) {
			if (obj.equals(list.get(i)))	return true;
		}
		return false;
	}
	
	public static boolean inThePast(int date, int time, int timeDifference) {
		time += timeDifference + (time % 100 < 15 ? 0 : 40);
		if (time > 2359) {
			date = MyDate.verschoben(date, 1);
			time -= 2400;
		}
		return inThePast(date, time);
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
		return ausDatei(dateiname, true);
	}
	
	public static ArrayList<String> ausDatei(String dateiname, boolean createIfNotExists) {
		ArrayList<String> arraylist = new ArrayList<String>();
		try {
			File datei = new File(dateiname);
			BufferedReader in = null;
			if (!datei.exists()) {
				if (createIfNotExists) {
					datei.createNewFile();
					log(" >>> File did not exist but was created! --> " + datei.getAbsolutePath());
				}
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
