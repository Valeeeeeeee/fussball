package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

import model.AnstossZeit;
import model.Datum;
import model.RelativeAnstossZeit;
import model.Spiel;
import model.TeamAffiliation;
import model.Uhrzeit;
import model.Zeitpunkt;

public class Utilities {
	
	public static final Color colorCategory1 = new Color(0, 200, 0);
	public static final Color colorCategory2 = new Color(128, 255, 0);
	public static final Color colorCategory3 = new Color(255, 255, 0);
	public static final Color colorCategory4 = new Color(255, 128, 0);
	public static final Color colorCategory5 = new Color(255, 0, 0);
	public static final Color colorCategory6 = new Color(128, 128, 128);
	public static final Color colorHomescreen = new Color(255, 255, 255);
	
	public static final Color colorOverviewBackground = new Color(255, 128, 128);
	
	public static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	
	public static final int STARTX = 0;
	public static final int STARTY = 1;
	public static final int GAPX = 2;
	public static final int GAPY = 3;
	public static final int SIZEX = 4;
	public static final int SIZEY = 5;
	private static boolean macOS = System.getProperty("os.name").startsWith("Mac OS X");
	
	public static char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
	
	public static final Datum UNIX_EPOCH = new Datum(1, 1, 1970);
	public static final Datum MIN_DATE = new Datum(1, 1, 0);
	public static final Datum DATE_UNDEFINED = new Datum(0, 0, 9999);
	public static final Datum today = new Datum();
	
	public static final int UNDEFINED = -1;
	
	public static final Uhrzeit TIME_UNDEFINED = new Uhrzeit(UNDEFINED);
	public static final Uhrzeit MIDNIGHT = new Uhrzeit(0, 0);
	public static final Uhrzeit END_OF_DAY = new Uhrzeit(23, 59);
	
	public static final AnstossZeit KICK_OFF_TIME_UNDEFINED = new AnstossZeit(RelativeAnstossZeit.of(0, 0, TIME_UNDEFINED), DATE_UNDEFINED);
	
	public static final String TO_BE_DATED = "TBD";
	public static final String SPIELFREI = "spielfrei";
	public static final String NOT_AVAILABLE = "n.a.";
	public static final String MAIN_CATEGORY = "MAIN_CATEGORY";
	public static final String SUB_CATEGORY = "SUB_CATEGORY";

	public static final String VERSUS = " vs. ";
	
	public static final String NO_PLAYER_FOR_SHIRTNUMBER = "Es konnte der Rückennummer %d von %s kein spielberechtigter Spieler zugeordnet werden.";
	
	public static String twoDigit(int number) {
		number = number % 100;
		return number / 10 + "" + number % 10;
	}
	
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
	
	public static BufferedImage resizeImage(Image image, int width, int height) {
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
	
	public static int optionsDialog(String message, String[] options) {
		return JOptionPane.showOptionDialog(null, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[options.length - 1]);
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
		return macOS ? "\u2b07" : "\u2193";
	}
	
	public static String arrowUp() {
		return macOS ? "\u2b06" : "\u2191";
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
			else if (chAt == 208)					name = name.substring(0, i) + "D" + name.substring(i + 1);
			else if (chAt == 209)					name = name.substring(0, i) + "N" + name.substring(i + 1);
			else if (210 <= chAt && chAt <= 213)	name = name.substring(0, i) + "O" + name.substring(i + 1);
			else if (chAt == 214 || chAt == 216)	name = name.substring(0, i) + "Oe" + name.substring(i + 1);
			else if (217 <= chAt && chAt <= 219)	name = name.substring(0, i) + "U" + name.substring(i + 1);
			else if (chAt == 220)					name = name.substring(0, i) + "Ue" + name.substring(i + 1);
			else if (chAt == 221)					name = name.substring(0, i) + "Y" + name.substring(i + 1);
			else if (chAt == 222)					name = name.substring(0, i) + "Th" + name.substring(i + 1);
			else if (chAt == 223)					name = name.substring(0, i) + "ss" + name.substring(i + 1);
			else if (chAt == 228 || chAt == 230)	name = name.substring(0, i) + "ae" + name.substring(i + 1);
			else if (224 <= chAt && chAt <= 229)	name = name.substring(0, i) + "a" + name.substring(i + 1);
			else if (chAt == 231)					name = name.substring(0, i) + "c" + name.substring(i + 1);
			else if (232 <= chAt && chAt <= 235)	name = name.substring(0, i) + "e" + name.substring(i + 1);
			else if (236 <= chAt && chAt <= 239)	name = name.substring(0, i) + "i" + name.substring(i + 1);
			else if (chAt == 240)					name = name.substring(0, i) + "d" + name.substring(i + 1);
			else if (chAt == 241)					name = name.substring(0, i) + "n" + name.substring(i + 1);
			else if (242 <= chAt && chAt <= 245)	name = name.substring(0, i) + "o" + name.substring(i + 1);
			else if (chAt == 246 || chAt == 248)	name = name.substring(0, i) + "oe" + name.substring(i + 1);
			else if (249 <= chAt && chAt <= 251)	name = name.substring(0, i) + "u" + name.substring(i + 1);
			else if (chAt == 252)					name = name.substring(0, i) + "ue" + name.substring(i + 1);
			else if (chAt == 253 || chAt == 255)	name = name.substring(0, i) + "y" + name.substring(i + 1);
			else if (chAt == 254)					name = name.substring(0, i) + "th" + name.substring(i + 1);
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
	
	public static <E> boolean containsSameElements(ArrayList<E> firstList, ArrayList<E> secondList) {
		if (firstList == null || secondList == null || firstList.size() != secondList.size())	return false;
		for (int i = 0; i < firstList.size(); i++) {
			if (!isElementOf(firstList.get(i), secondList))	return false;	
		}
		return true;
	}
	
	public static void addInOrder(ArrayList<AnstossZeit> list, AnstossZeit kickOffTime) {
		int index = 0;
		for (index = 0; index < list.size(); index++) {
			if (kickOffTime.isBefore(list.get(index)))	break;
		}
		list.add(index, kickOffTime);
	}
	
	public static void addInOrder(ArrayList<Spiel> list, Spiel match) {
		int index = 0;
		for (index = 0; index < list.size(); index++) {
			if (match.isInOrderBefore(list.get(index)))	break;
		}
		list.add(index, match);
	}
	
	public static void addAscending(ArrayList<String> list, String element) {
		if (list.contains(element))	return;
		int index;
		for (index = 0; index < list.size(); index++) {
			if (list.get(index).compareTo(element) > 0)	break;
		}
		list.add(index, element);
	}
	
	public static void addDescending(ArrayList<Integer> list, int number) {
		int index;
		for (index = 0; index < list.size(); index++) {
			if (list.get(index) < number)	break;
		}
		list.add(index, number);
	}
	
	public static boolean inThePast(Zeitpunkt moment) {
		return moment.isBefore(Zeitpunkt.now());
	}
	
	public static ArrayList<TeamAffiliation> cloneList(ArrayList<TeamAffiliation> list) {
		ArrayList<TeamAffiliation> clone = new ArrayList<>();
		
		for (TeamAffiliation affiliation : list) {
			clone.add(affiliation);
		}
		
		return clone;
	}
	
	public static ArrayList<String> readFile(String fileName) {
		return readFile(fileName, true);
	}
	
	public static ArrayList<String> readFile(String fileName, boolean createIfNotExists) {
		ArrayList<String> arrayList = new ArrayList<String>();
		if (fileName == null) {
			message("Der übergebene Dateiname ist null.");
			return arrayList;
		}
		try {
			File file = new File(fileName);
			BufferedReader in = null;
			if (!file.exists()) {
				if (createIfNotExists) {
					file.createNewFile();
					log(" >>> File did not exist but was created! --> " + file.getAbsolutePath());
				}
			} else {
				String element;
				try {
					in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
					while ((element = in.readLine()) != null) {
						if (!element.isEmpty()) {
							arrayList.add(element.replace("" + (char) 65279, ""));
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
			log("No such file or directory: " + fileName);
		}
		
		return arrayList;
	}
	
	public static void writeFile(String fileName, String[] strings) {
		if (fileName == null) {
			message("Der übergebene Dateiname ist null.");
			return;
		}
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
				for (int i = 0; i < strings.length; i++) {
					out.write(strings[i]);
					out.newLine();
				}
			} catch (Exception e) {
				log(e.getClass().getName() + " while writing in file " + fileName);
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
			log(" >> inDatei >> No such file or directory: " + fileName + "\n");
		}
	}
	
	public static void writeFile(String fileName, ArrayList<String> strings) {
		if (fileName == null) {
			message("Der übergebene Dateiname ist null.");
			return;
		}
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
				for (int i = 0; i < strings.size(); i++) {
					out.write(strings.get(i));
					out.newLine();
				}
			} catch (Exception e) {
				log(e.getClass().getName() + " while writing in file " + fileName);
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
			log(" >> inDatei >> No such file or directory: " + fileName + "\n");
		}
	}
	
	public static int getWindowDecorationWidth() {
		if (macOS)	return 0;
		else		return 6;
	}
	
	public static int getWindowDecorationHeight() {
		if (macOS)	return 22;
		else		return 28;
	}
}
