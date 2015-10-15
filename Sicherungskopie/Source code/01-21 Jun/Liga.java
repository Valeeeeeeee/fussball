
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Liga {
	private int id;
	private String name;
	
	private int saisonbeginn;
	public int[] saisons;
	public int aktuelle_saison;
	
	private int anzahl_mannschaften;
	private int halbeanzMSAuf;
	private int anzahl_spiele;
	private int anzahl_spieltage;
	
	private int ANZAHL_CL;
	private int ANZAHL_CLQ;
	private int ANZAHL_EL;
	private int ANZAHL_REL;
	private int ANZAHL_ABS;
	
	private Mannschaft[] mannschaften;
	
    String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
    String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
    
    String workspace = workspaceMAC;
    
    String teams;
	DefaultListModel jListConfigModel = new DefaultListModel();
    
	String spielplan;
	DefaultListModel jListSpielplanModel = new DefaultListModel();
    boolean[] spieltage_eingetragen;
    int[][][] spieltage;
    
    String ergebnisse;
    DefaultListModel jListErgebnisseModel = new DefaultListModel();
    boolean[] ergebnisse_eingetragen;
    int[][][] ergebnis;
    
    int[][] daten_uhrzeiten;
    
    int anzahl_spieltermine;
    String[] anstosszeiten;
    int[] tageseitfr;
    int default_starttag;
    
    
    
	public Liga(int id, String daten) {
		fromString(daten);
		this.mannschaften = new Mannschaft[0];
	}
	
	public Liga(int id, String name, int anz_MS, int anzCL, int anzCLQ, int anzEL, int anzREL, int anzABS) {
		this.name = name;
		this.mannschaften = new Mannschaft[0];
		
		this.ANZAHL_CL = anzCL;
		this.ANZAHL_CLQ = anzCLQ;
		this.ANZAHL_EL = anzEL;
		this.ANZAHL_REL = anzREL;
		this.ANZAHL_ABS = anzABS;
	}
	
	
	
	
	public String datum(int myformat) { 
		int d1 = (myformat % 100) / 10;
		int d2 = myformat % 10;
		int mo1 = (myformat % 10000) / 1000;
		int mo2 = ((myformat % 10000) / 100) % 10;
		int y = myformat / 10000;
		
		return d1 + "" + d2 + "." + mo1 + "" + mo2 + "." + y;
	}
	
	public static int newMyDate() {
		int datum = 0;
		Calendar gc = new GregorianCalendar();
		int dd = gc.get(Calendar.DAY_OF_MONTH);
		int mm = gc.get(Calendar.MONTH) + 1;
		int yyyy = gc.get(Calendar.YEAR);
		
		datum = 10000 * yyyy + 100 * mm + dd;
		
		return datum;
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
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = newMyDate() - 1; // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
		if (today < this.daten_uhrzeiten[0][0]) {
			matchday = 0;
		} else if (today > this.daten_uhrzeiten[this.anzahl_spieltage - 1][0]) {
			matchday = this.anzahl_spieltage - 1;
		} else {
			matchday = 0;
			while (today > this.daten_uhrzeiten[matchday][0]) {
				matchday++;
			}
			if (matchday != 0 && (today - this.daten_uhrzeiten[matchday - 1][0]) < (this.daten_uhrzeiten[matchday][0] - today)) {
				matchday--;
			}
		}
		
		return matchday;
	}
	
	public void addNewTeam(String str, boolean fromOptions) {
		
		// Speicherung der Daten in String-Arrays // TODO andere Daten auch
		String[] namen = new String[this.anzahl_mannschaften + 1];
		String[] gruendungsdaten = new String[this.anzahl_mannschaften + 1];
		if (this.mannschaften != null) {
			for (int i = 0; i < this.anzahl_mannschaften; i++) {
				namen[i] = this.mannschaften[i].getName();
				gruendungsdaten[i] = this.mannschaften[i].getGruendungsdatum();
			}
		}
		
		if (str.indexOf(";") != -1) {
			namen[namen.length - 1] = str.substring(0, str.indexOf(";"));
			gruendungsdaten[namen.length - 1] = str.substring(str.indexOf(";") + 1, str.lastIndexOf(";"));
		} else {
			namen[namen.length - 1] = str;
			gruendungsdaten[namen.length - 1] = "09.09.1893";
		}
		
		// Erhöhung der Anzahlen
		this.anzahl_mannschaften++;
		halbeanzMSAuf = (int) Math.round((double) anzahl_mannschaften / 2);				// liefert die (aufgerundete) Hälfte zurück
		anzahl_spiele = anzahl_mannschaften / 2;										// liefert die (abgerundete) Hälfte zurück
		if (anzahl_mannschaften >= 2)		anzahl_spieltage = 4 * halbeanzMSAuf - 2;
		else								anzahl_spieltage = 0;
		
		// Neu-Erstellung der Mannschaften
		this.mannschaften = new Mannschaft[anzahl_mannschaften];
		for (int i = 0; i < namen.length; i++) {
			this.mannschaften[i] = new Mannschaft(i + 1, anzahl_spieltage);
			this.mannschaften[i].setName(namen[i]);
			this.mannschaften[i].setGruendungsdatum((gruendungsdaten[i]));
		}
		
		if (fromOptions) {
			arraygroessen_initialisieren();
			this.speichern();
			
	        spielplan_laden();
	        ergebnisse_laden();
		}
	}
	
	public void ergebnisse_sichern() {
		
		for (int i = 0; i < Start.Spieltage.tore.length; i++) {
			ergebnis[Start.Spieltage.current_matchday][i % anzahl_spiele][i / anzahl_spiele] = Integer.parseInt(Start.Spieltage.tore[i].getText());
		}
		int counter = 0;
		for (int i = 0; i < mannschaften.length; i++) {
			// falls die Anzahl an Mannschaften ungerade ist: hat die spielfreie Mannschaft (Gegner == 0) auch positiononplan == 0 und darf nicht gezählt werden
			if (mannschaften[i].getMatch(Start.Spieltage.current_matchday)[1] != 0) { 
				int pos = mannschaften[i].positiononplan[Start.Spieltage.current_matchday];
				int tore = Integer.parseInt(Start.Spieltage.tore[pos].getText());
				int gegentore = Integer.parseInt(Start.Spieltage.tore[(pos + anzahl_spiele) % Start.Spieltage.tore.length].getText());
				mannschaften[i].setResult(Start.Spieltage.current_matchday, tore, gegentore);
				
				if(tore >= 0 && gegentore >= 0) {
					counter++; // z�hlt diejenigen Spiele die komplett mit Ergebnis eingetragen sind
				}
			} else {
				counter++; // eine spielfreie Mannschaft ist auch korrekt eingetragen
			}
		}
		if (counter == mannschaften.length)		ergebnisse_eingetragen[Start.Spieltage.current_matchday] = true;
		else									ergebnisse_eingetragen[Start.Spieltage.current_matchday] = false;
	}
	
	public void laden(int index) {
		String saison;
		if (saisonbeginn > 7) {
			saison = "" + saisons[index] + "_" + (saisons[index] + 1);
		} else {
			saison = "" + saisons[index];
		}
		aktuelle_saison = index;
		
        ergebnisse = workspace + File.separator + name + File.separator + saison + File.separator + "Ergebnisse.txt";
        spielplan = workspace + File.separator + name + File.separator + saison + File.separator + "Spielplan.txt";
    	teams = workspace + File.separator + name + File.separator + saison + File.separator + "Mannschaften.txt";
    	
    	File file = new File(workspace + File.separator + name + File.separator + saison);
    	
    	try {
    		file.mkdir();
    	} catch (Exception e) {
    		System.out.println("Error while creating directory!");
    	}
    	
    	mannschaften_laden();
        
        arraygroessen_initialisieren();
        
        spielplan_laden();
        ergebnisse_laden();
    }
	
	public void speichern() {
		this.spielplan_schreiben();
		this.ergebnisse_schreiben();
		this.mannschaften_schreiben();
	}
	
	public Mannschaft[] getMannschaften() {
		return this.mannschaften;
	}
	
	public String teamsToString() {
		String alles = "";
		
		for (int i = 0; i < anzahl_mannschaften; i++) {
			alles = alles + mannschaften[i].toString() + "\n";
		}
		
		return alles;
	}
	
	public int getAnzahlTeams() {
		return this.anzahl_mannschaften;
	}
	
	public int getAnzahlSpiele() {
		return this.anzahl_spiele;
	}
	
	public int getHalbeAnzMSAuf() {
		return this.halbeanzMSAuf;
	}
	
	public int getAnzahlSpieltage() {
		return this.anzahl_spieltage;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getAnzahlCL() {
		return this.ANZAHL_CL;
	}
	
	public int getAnzahlCLQ() {
		return this.ANZAHL_CLQ;
	}
	
	public int getAnzahlEL() {
		return this.ANZAHL_EL;
	}
	
	public int getAnzahlREL() {
		return this.ANZAHL_REL;
	}
	
	public int getAnzahlABS() {
		return this.ANZAHL_ABS;
	}
	
	public String getDateOf(int matchday, int spiel) {
		if (matchday >= 0 && matchday < this.anzahl_spieltage && spiel >= 0 && spiel < this.anzahl_spiele)
			return datum(myDateVerschoben(this.daten_uhrzeiten[matchday][0], this.tageseitfr[this.daten_uhrzeiten[matchday][spiel + 1]])) + " " + this.anstosszeiten[this.daten_uhrzeiten[matchday][spiel + 1]];
		else
			return "nicht terminiert";
	}
	
	public String toString() {
		String rueckgabe = "NAME*" + this.name + ";";
		rueckgabe = rueckgabe + "A_MS*" + this.anzahl_mannschaften + ";";
		rueckgabe = rueckgabe + "A_CL*" + this.ANZAHL_CL + ";";
		rueckgabe = rueckgabe + "A_CLQ*" + this.ANZAHL_CLQ + ";";
		rueckgabe = rueckgabe + "A_EL*" + this.ANZAHL_EL + ";";
		rueckgabe = rueckgabe + "A_REL*" + this.ANZAHL_REL + ";";
		rueckgabe = rueckgabe + "A_ABS*" + this.ANZAHL_ABS + ";";
		rueckgabe = rueckgabe + "A_SAI*" + this.saisons.length + ";";
		for (int i = 0; i < this.saisons.length; i++) {
			String trenn = "S" + i;
			rueckgabe = rueckgabe + trenn + "*" + this.saisons[i] + "*" + trenn + ";";
		}
		return rueckgabe;
	}
	
	private void fromString(String daten) {
		this.name = daten.substring(daten.indexOf("NAME*") + 5, daten.indexOf(";A_MS*"));
		this.anzahl_mannschaften = Integer.parseInt(daten.substring(daten.indexOf("A_MS*") + 5, daten.indexOf(";A_CL*")));
		this.ANZAHL_CL = Integer.parseInt(daten.substring(daten.indexOf("A_CL*") + 5, daten.indexOf(";A_CLQ*")));
		this.ANZAHL_CLQ = Integer.parseInt(daten.substring(daten.indexOf("A_CLQ*") + 6, daten.indexOf(";A_EL*")));
		this.ANZAHL_EL = Integer.parseInt(daten.substring(daten.indexOf("A_EL*") + 5, daten.indexOf(";A_REL*")));
		this.ANZAHL_REL = Integer.parseInt(daten.substring(daten.indexOf("A_REL*") + 6, daten.indexOf(";A_ABS*")));
		this.ANZAHL_ABS = Integer.parseInt(daten.substring(daten.indexOf("A_ABS*") + 6, daten.indexOf(";A_SAI*")));
		
		System.out.println("Rest: " + daten.substring(daten.indexOf(";A_SAI*") + 1) + "\n");
		this.saisons = new int[Integer.parseInt(daten.substring(daten.indexOf(";A_SAI*") + 7, daten.indexOf(";S0*")))];
		for (int i = 0; i < saisons.length; i++) {
			String trenn = "S" + i;
			saisons[i] = Integer.parseInt(daten.substring(daten.indexOf(trenn + "*") + (trenn + "*").length(), daten.indexOf("*" + trenn + ";")));
		}
		
		// TODO
		this.saisonbeginn = 8;
	    this.default_starttag = 4;
	}
	
	private void arraygroessen_initialisieren() {
    	// Alle Array werden initialisiert
		this.daten_uhrzeiten = new int[this.anzahl_spieltage][this.anzahl_spiele + 1];
        this.spieltage = new int[this.anzahl_spieltage][this.anzahl_spiele][2];
        this.ergebnis = new int[this.anzahl_spieltage][this.anzahl_spiele][2];
        this.spieltage_eingetragen = new boolean[this.anzahl_spieltage];
        this.ergebnisse_eingetragen = new boolean[this.anzahl_spieltage];
    }
	
	private void aus_datei(String dateiname, DefaultListModel model) {
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
    
    private void in_datei(String dateiname, DefaultListModel model) {
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
	
    private void mannschaften_laden() {
    	aus_datei(this.teams, this.jListConfigModel);
        
        if (this.jListConfigModel.getSize() > 0) {
        	int counter = 0;
        	
        	// in der ersten Zeile steht die Anzahl der Mannschaften
            int anzahl_mannschaften = Integer.parseInt((String) this.jListConfigModel.get(counter));
            counter++;
            
            this.anzahl_mannschaften = 0;
            for (int i = 0; i < anzahl_mannschaften; i++) {
                addNewTeam((String) this.jListConfigModel.get(i + counter), false);
            }
            counter += anzahl_mannschaften;
        } else {
        	this.anzahl_mannschaften = 0;
        }
    }
    
    private void mannschaften_schreiben() {
    	this.jListConfigModel.clear();
    	this.jListConfigModel.addElement("" + this.anzahl_mannschaften);
		for (int i = 0; i < this.anzahl_mannschaften; i++) {
			this.jListConfigModel.addElement(this.mannschaften[i].toString());
		}
		in_datei(this.teams, this.jListConfigModel);
    }
    
	private void spielplan_laden() {
		try {
			aus_datei(this.spielplan, this.jListSpielplanModel); 
		    
	        int counter = 0;
	        
	        String as_zeiten = (String) this.jListSpielplanModel.get(counter);
	        this.jListSpielplanModel.remove(counter);
	        this.anzahl_spieltermine = Integer.parseInt(as_zeiten.substring(0, as_zeiten.indexOf(";")));
	        this.tageseitfr = new int[this.anzahl_spieltermine];
	        this.anstosszeiten = new String[this.anzahl_spieltermine];
	        as_zeiten = as_zeiten.substring(as_zeiten.indexOf(";") + 1);
	        for (counter = 0; counter < this.anzahl_spieltermine; counter++) {
	        	String asz = as_zeiten.substring(0, as_zeiten.indexOf(";"));
	            as_zeiten = as_zeiten.substring(as_zeiten.indexOf(";") + 1);
	        	tageseitfr[counter] = Integer.parseInt(asz.substring(0, asz.indexOf(",")));
	        	anstosszeiten[counter] = asz.substring(asz.indexOf(",") + 1);
	        }
	        
	        for (counter = 0; counter < this.anzahl_spieltage; counter++) {
	            String e = (String) this.jListSpielplanModel.get(counter);
	            this.spieltage_eingetragen[counter] = Boolean.parseBoolean(e.substring(0, e.indexOf(";")));
	            e = e.substring(e.indexOf(";") + 1);
	            int count2 = 0;
	            if (this.spieltage_eingetragen[counter]) {
	            	int neu = 0;
	            	String datum = e.substring(0, e.indexOf(";"));
	            	while (datum.indexOf(":") != -1) {
	                	daten_uhrzeiten[counter][neu] = Integer.parseInt(datum.substring(0, datum.indexOf(":")));
	                	datum = datum.substring(datum.indexOf(":") + 1);
	                	neu++;
	            	}
	            	daten_uhrzeiten[counter][neu] = Integer.parseInt(datum);
	            	
	            	e = e.substring(e.indexOf(";") + 1);
	                while (e.indexOf(";") != -1) {
	                    String r = e.substring(0, e.indexOf(";"));
	                    int teamHOME = Integer.parseInt(r.substring(0,r.indexOf(":")));
	                    int teamAWAY = Integer.parseInt(r.substring(r.indexOf(":") + 1));
	                    this.spieltage[counter][count2][0] = teamHOME;
	                    this.spieltage[counter][count2][1] = teamAWAY;

	                    this.mannschaften[teamHOME - 1].setGegner(counter, Mannschaft.HOME, teamAWAY, count2);
	                    this.mannschaften[teamAWAY - 1].setGegner(counter, Mannschaft.AWAY, teamHOME, count2);
	                    
	                    e = e.substring(e.indexOf(";") + 1);
	                    count2++;
	                }
	            }
	            for (int k = count2; k < this.anzahl_spiele; k++) {
	            	this.spieltage[counter][k][0] = -1;
	            	this.spieltage[counter][k][1] = -1;
	            }
	        }
	        
	        while(counter < this.anzahl_spieltage) {
	            for (int k = 0; k < this.anzahl_spiele; k++) {
	            	this.spieltage[counter][k][0] = -1;
	            	this.spieltage[counter][k][1] = -1;
	            }
	            counter++;
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Kein Spielplan", "", JOptionPane.ERROR_MESSAGE);
		}
    }
	
	private void spielplan_schreiben() {
		this.jListSpielplanModel.clear();
        
		String string = this.anzahl_spieltermine + ";";
		for (int i = 0; i < this.anzahl_spieltermine; i++) {
			string = string + this.tageseitfr[i] + "," + this.anstosszeiten[i] + ";";
        }
		this.jListSpielplanModel.addElement(string);
		
        for (int i = 0; i < this.anzahl_spieltage; i++) {
            string = this.spieltage_eingetragen[i] + ";";
            if (this.spieltage_eingetragen[i]) {
            	string = string + this.daten_uhrzeiten[i][0];
            	for (int j = 0; j < this.anzahl_spiele; j++) {
            		string = string + ":" + daten_uhrzeiten[i][j + 1];
            	}
            	string = string + ";";
                for (int j = 0; j < this.spieltage[i].length; j++) {
                    string = string + this.spieltage[i][j][0] + ":" + this.spieltage[i][j][1] + ";";
                }
            }
            
            this.jListSpielplanModel.addElement(string);
        }
        
        in_datei(this.spielplan, this.jListSpielplanModel);
    }
	
	private void ergebnisse_laden() {
		try {
			aus_datei(this.ergebnisse, this.jListErgebnisseModel);
	        int counter;
	        
	        for (counter = 0; counter < this.anzahl_spieltage; counter++) {
	            String e = (String) this.jListErgebnisseModel.get(counter);
	            this.ergebnisse_eingetragen[counter] = Boolean.parseBoolean(e.substring(0, e.indexOf(";")));
	            e = e.substring(e.indexOf(";") + 1);
	            int count2 = 0;
	            if (this.spieltage_eingetragen[counter] && this.ergebnisse_eingetragen[counter]) {
	                while (e.indexOf(";") != -1) {
	                    String r = e.substring(0, e.indexOf(";"));
	                    int toreHOME = Integer.parseInt(r.substring(0,r.indexOf(":")));
	                    int toreAWAY = Integer.parseInt(r.substring(r.indexOf(":") + 1));

	                    int teamHOME = this.spieltage[counter][count2][0];
	                    int teamAWAY = this.spieltage[counter][count2][1];
	                    
	                    this.ergebnis[counter][count2][0] = toreHOME;
	                    this.ergebnis[counter][count2][1] = toreAWAY;
	                    
	                    this.mannschaften[teamHOME - 1].setResult(counter, toreHOME, toreAWAY);
	                    this.mannschaften[teamAWAY - 1].setResult(counter, toreAWAY, toreHOME);
	                    
	                    e = e.substring(e.indexOf(";") + 1);
	                    count2++;
	                }
	            }
	            for (int k = count2; k < this.anzahl_spiele; k++) {
	            	this.ergebnis[counter][k][0] = -1;
	            	this.ergebnis[counter][k][1] = -1;
	            }
	        }
	        
	        while(counter < this.anzahl_spieltage) {
	            for (int k = 0; k < this.anzahl_spiele; k++) {
	            	this.ergebnis[counter][k][0] = -1;
	            	this.ergebnis[counter][k][1] = -1;
	            }
	            counter++;
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Kein Ergebnisseplan", "", JOptionPane.ERROR_MESSAGE);
		}
    }
	
	private void ergebnisse_schreiben() {
        this.jListErgebnisseModel.clear();
        for (int i = 0; i < this.anzahl_spieltage; i++) {
            String string = this.ergebnisse_eingetragen[i] + ";";
            if (this.ergebnisse_eingetragen[i]) {
                for (int j = 0; j < this.ergebnis[i].length; j++) {
                    String teil2 = this.ergebnis[i][j][0] + ":" + this.ergebnis[i][j][1] + ";";
                    
                    string = string + teil2;
                }
            }
            
            this.jListErgebnisseModel.addElement(string);
        }
        
        in_datei(this.ergebnisse, this.jListErgebnisseModel);
    }
	
}
