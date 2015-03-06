import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.DefaultListModel;

@SuppressWarnings("rawtypes")
public class Gruppe {
	private int id;
	
	int countteams;
	public Mannschaft[] mannschaften;
	private Turnier turnier;
	
	/**
	 * [Spieltag][Spiel][Mannschaft]
	 */
	private int[][][] spiele;
	private int[][][] ergebnisse;
	
	private boolean[] spieleEingetragen;
	private boolean[] ergebnisseEingetragen;
	
	private String workspace;
	private String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	private String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
	
	private String dateiMannschaft;
	private String dateiSpielplan;
	private String dateiErgebnisse;
	private DefaultListModel jListConfigModel = new DefaultListModel();
	private DefaultListModel jListSpielplanModel = new DefaultListModel();
	private DefaultListModel jListErgebnisseModel = new DefaultListModel();
	
	int[][] datesAndTimes;
	String[] anstosszeiten;
    int[] daysSinceFirstDay;
	
	private Spieltag spieltag;
	private Tabelle tabelle;
	
	public Gruppe(int id, Mannschaft[] teams, Turnier turnier) {
		checkOS();
		
		this.id = id;
		this.countteams = teams.length;
		this.spiele = new int[countteams - 1][countteams / 2][2];
		this.ergebnisse = new int[countteams - 1][countteams / 2][2];
		
		this.spieleEingetragen = new boolean[countteams - 1];
		this.ergebnisseEingetragen = new boolean[countteams - 1];
		
		this.mannschaften = teams;
		this.turnier = turnier;
		
		System.out.println("\nGruppe " + (this.id + 1) + ":");
		
		for (int i = 0; i < mannschaften.length; i++) {
			System.out.println("  Mannschaft: " + mannschaften[i].getName());
		}
		System.out.println();
		
		this.laden();
		
		{
            spieltag = new Spieltag(this, turnier);
//            getContentPane().add(Spieltage);
            spieltag.setLocation((1440 - spieltag.getSize().width) / 2, (874 - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getNumberOfMatchesPerMatchday() {
		return this.countteams / 2;
	}
	
	public int getNumberOfTeams() {
		return this.countteams;
	}
	
	public int getNumberOfMatchdays() {
		return this.countteams - 1;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public Mannschaft[] getMannschaften() {
		return this.mannschaften;
	}
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.getNumberOfMatchdays() && spiel >= 0 && spiel < this.getNumberOfMatchdays())
			return datum(myDateVerschoben(this.datesAndTimes[matchday][0], this.daysSinceFirstDay[this.datesAndTimes[matchday][spiel + 1]])) + " " + this.anstosszeiten[this.datesAndTimes[matchday][spiel + 1]];
		else
			return "nicht terminiert";
	}
	
	public static int myDateVerschoben(int startdatum, int tageplus) {
		int dd = startdatum % 100;
		int mm = (startdatum % 10000) / 100;
		int yyyy = startdatum / 10000;
		
		GregorianCalendar greg = new GregorianCalendar(yyyy, mm - 1, dd);
		greg.add(Calendar.DAY_OF_YEAR, tageplus);
		
		int datum = 0;
		
		dd = greg.get(Calendar.DAY_OF_MONTH);
		mm = greg.get(Calendar.MONTH) + 1;
		yyyy = greg.get(Calendar.YEAR);
		
		datum = 10000 * yyyy + 100 * mm + dd;
		
		return datum;
	}
	
	public String datum(int myformat) { 
		int d1 = (myformat % 100) / 10;
		int d2 = myformat % 10;
		int mo1 = (myformat % 10000) / 1000;
		int mo2 = ((myformat % 10000) / 100) % 10;
		int y = myformat / 10000;
		
		return d1 + "" + d2 + "." + mo1 + "" + mo2 + "." + y;
	}
	
	public void laden() {
		dateiErgebnisse = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Ergebnisse.txt";
		dateiSpielplan = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Spielplan.txt";
		dateiMannschaft = workspace + File.separator + turnier.getName() + File.separator + "Gruppe " + (this.id + 1) + File.separator + "Mannschaften.txt";
		
    	mannschaftenLaden();
    	spielplanLaden();
    	ergebnisseLaden();
		
		
//		System.out.println("Anfang");
//		for (Mannschaft ms: mannschaften) {
//			System.out.println("  " + ms.getName());			
//		}
//		System.out.println("Ende \n\n");
		
	}
	
	public void mannschaftenLaden() {
		ausDatei(dateiMannschaft, jListConfigModel);
		
		mannschaften = new Mannschaft[Integer.parseInt((String) jListConfigModel.getElementAt(0))];
    	jListConfigModel.remove(0);
    	
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = new Mannschaft(i + 1, mannschaften.length - 1);
			mannschaften[i].setName((String) jListConfigModel.getElementAt(i));
		}
	}
	
	public void spielplanLaden() {
		ausDatei(dateiSpielplan, jListSpielplanModel);
		
		for (int spieltag = 0; spieltag < jListSpielplanModel.getSize(); spieltag++) {
			System.out.print("  Spieltag " + (spieltag + 1));
			String[] spielstrings = ((String) jListSpielplanModel.getElementAt(spieltag)).split(";");
			spieleEingetragen[spieltag] = Boolean.parseBoolean(spielstrings[0]);
			System.out.println("\t(" + spielstrings[0] + ")");
			if (spieleEingetragen[spieltag]) {
				for (int spiel = 1; spiel < spielstrings.length; spiel++) {
					String[] indMannschaften = spielstrings[spiel].split(":");
					int[] indizes = new int[indMannschaften.length];
					for (int team = 0; team < indizes.length; team++) {
						indizes[team] = Integer.parseInt(indMannschaften[team]);
						spiele[spieltag][spiel - 1][team] = indizes[team];
					}
					System.out.println("    Neues Spiel: " + mannschaften[spiele[spieltag][spiel - 1][0] - 1].getName() + " : " + mannschaften[spiele[spieltag][spiel - 1][1] - 1].getName());
				}
			}
		}
    	// TODO Spielplan laden
	}
	
	public void ergebnisseLaden() {
		ausDatei(dateiErgebnisse, jListErgebnisseModel);
		
		for (int spieltag = 0; spieltag < jListErgebnisseModel.getSize(); spieltag++) {
			System.out.print("  Spieltag " + (spieltag + 1));
			String[] ergebnisstrings = ((String) jListErgebnisseModel.getElementAt(spieltag)).split(";");
			ergebnisseEingetragen[spieltag] = Boolean.parseBoolean(ergebnisstrings[0]);
			System.out.println("\t(" + ergebnisstrings[0] + ")");
			if (ergebnisseEingetragen[spieltag]) {
				for (int spiel = 1; spiel < ergebnisstrings.length; spiel++) {
					String[] indMannschaften = ergebnisstrings[spiel].split(":");
					int[] indizes = new int[indMannschaften.length];
					for (int ergebnis = 0; ergebnis < indizes.length; ergebnis++) {
						indizes[ergebnis] = Integer.parseInt(indMannschaften[ergebnis]);
						ergebnisse[spieltag][spiel - 1][ergebnis] = indizes[ergebnis];
					}
					System.out.println("    Neues Spiel: "
							+ mannschaften[spiele[spieltag][spiel - 1][0] - 1].getName() + " "
							+ ergebnisse[spieltag][spiel - 1][0] + ":"
							+ ergebnisse[spieltag][spiel - 1][1] + " "
							+ mannschaften[spiele[spieltag][spiel - 1][1] - 1].getName());
				}
			}
		}
		// TODO Ergebnisse laden
	}
	
	public void checkOS() {
		if (new File(workspaceWIN).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You are running Windows.");
			workspace = workspaceWIN;
		} else if (new File(workspaceMAC).isDirectory()) {
//			JOptionPane.showMessageDialog(null, "You have a Mac.");
			workspace = workspaceMAC;
		} else {
//			JOptionPane.showMessageDialog(null, "You are running neither OS X nor Windows, probably Linux!");
			workspace = null;
		}
	}

	private void ausDatei(String dateiname, DefaultListModel model) {
        try {
            File datei = new File(dateiname);
            BufferedReader in = null;
            if (!datei.exists()) {
                datei.createNewFile();
                System.out.println(" >>> File did not exist but was created! --> " + datei.getAbsolutePath());
            } else {
                String element;
                try {
                    in = new BufferedReader(new FileReader(datei));
                    model.clear();
                    while ((element = in.readLine()) != null) {
                        model.addElement(element);
                    }
                } catch (Exception e) {
                    System.out.println("Fehler beim Laden!");
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
            System.out.println("Programmfehler!");
            e.printStackTrace();
        }
    } 
    
    private void inDatei(String dateiname, DefaultListModel model) {
        try {
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new FileWriter(dateiname));
                for (int i = 0; i < model.getSize(); i++) {
                    out.write(model.get(i).toString());
                    out.newLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            System.out.println(" >> in_datei >> Exception caught at file: " + dateiname + "\n");
        }
    }
	
}