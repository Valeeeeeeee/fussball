package model;
import java.io.*;

import javax.swing.*;

import static util.Utilities.*;

public class Liga {
	private int id = -1;
    private Start start;
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
    String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Fussball";
    
    String workspace = workspaceMAC;
    
    String dateiTeams;
	String[] teamsFromFile;
    
	String dateiSpielplan;
	String[] spielplanFromFile;
    boolean[] spieltageEingetragen;
    /**
     * [spieltag][spiel][vorne/hinten]
     */
    int[][][] spieltage;
    
    String dateiErgebnisse;
    String[] ergebnisseFromFile;
    boolean[] ergebnisseEingetragen;
    /**
     * [spieltag][spiel]
     */
	private Ergebnis[][] ergebnisse;
    
    int[][] daten_uhrzeiten;
    
    int anzahl_spieltermine;
    String[] anstosszeiten;
    int[] tageseitfr;
    int default_starttag;
    
    private Spieltag spieltag;
    private Tabelle tabelle;
    
    
	public Liga(int id, Start start, String daten) {
		this.id = id;
		this.start = start;
		fromString(daten);
		this.mannschaften = new Mannschaft[0];
	}
	
	public Liga(int id, Start start, String name, int anz_MS, int anzCL, int anzCLQ, int anzEL, int anzREL, int anzABS) {
		this.id = id;
		this.start = start;
		this.name = name;
		this.mannschaften = new Mannschaft[0];
		
		this.ANZAHL_CL = anzCL;
		this.ANZAHL_CLQ = anzCLQ;
		this.ANZAHL_EL = anzEL;
		this.ANZAHL_REL = anzREL;
		this.ANZAHL_ABS = anzABS;
	}
	
	public Spieltag getSpieltag() {
		return this.spieltag;
	}
	
	public Tabelle getTable() {
		return this.tabelle;
	}
	
	public int getCurrentMatchday() {
		int matchday = -1;
		int today = MyDate.newMyDate() - 1; // damit erst mittwochs umgeschaltet wird, bzw. in englischen Wochen der Binnenspieltag am Montag + Donnerstag erscheint
		
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
			this.mannschaften[i] = new Mannschaft(this.start, i + 1, this);
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
	
	
	/**
	 * This is the new implementation, that starts with the matches and does not go through the teams. It therefore renounces on the positiononplan array.
	 */
	public void ergebnisseSichern() {
		int matchday = spieltag.currentMatchday;
		
		int counter = 0;
		for (int match = 0; match < spieltag.getNumberOfMatches(); match++) {
			Ergebnis result;
			if (!spieltag.tore[match].getText().equals("-1") && !spieltag.tore[match + anzahl_spiele].getText().equals("-1")) {
				result = new Ergebnis(spieltag.tore[match].getText() + ":" + spieltag.tore[match + anzahl_spiele].getText());
				counter += 2;
			} else {
				result = null;
			}
			
			setErgebnis(matchday, match, result);
			mannschaften[spieltage[matchday][match][0] - 1].setResult(matchday, result);
			mannschaften[spieltage[matchday][match][1] - 1].setResult(matchday, result);
		}
		
		ergebnisseEingetragen[matchday] = false;
		
		if (spieltageEingetragen[matchday]) {
			int spielfrei = 0;
			for (int i = 0; i < mannschaften.length; i++) {
				// falls die Anzahl an Mannschaften ungerade ist: hat die spielfreie Mannschaft (Gegner == 0) auch positiononplan == 0
				if (mannschaften[i].getMatch(matchday)[1] == 0) {
					spielfrei++; // eine spielfreie Mannschaft ist auch korrekt eingetragen
				}
			}
			
			if (spielfrei > 1) {
				// dann ist der Spieltag nicht oder nicht vollständig eingetragen
				return;
			}
			
			if (counter + spielfrei == mannschaften.length)		ergebnisseEingetragen[matchday] = true;
		}
	}
	
	public void laden(int index) {
		String saison;
		if (saisonbeginn > 7) {
			saison = "" + saisons[index] + "_" + (saisons[index] + 1);
		} else {
			saison = "" + saisons[index];
		}
		aktuelle_saison = index;
		
        dateiErgebnisse = workspace + File.separator + name + File.separator + saison + File.separator + "Ergebnisse.txt";
        dateiSpielplan = workspace + File.separator + name + File.separator + saison + File.separator + "Spielplan.txt";
    	dateiTeams = workspace + File.separator + name + File.separator + saison + File.separator + "Mannschaften.txt";
    	
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
        
        if (spieltag != null)	spieltag.setVisible(false);
        if (tabelle != null)	tabelle.setVisible(false);
        {
            spieltag = new Spieltag(this);
            spieltag.setLocation((start.WIDTH - spieltag.getSize().width) / 2, (start.HEIGHT - 28 - spieltag.getSize().height) / 2); //-124 kratzt oben, +68 kratzt unten
            spieltag.setVisible(false);
        }
        {
            tabelle = new Tabelle(start, this);
            tabelle.setLocation((1440 - tabelle.getSize().width) / 2, 50);
            tabelle.setVisible(false);
        }
    }
	
	public void speichern() {
		this.spielplan_schreiben();
		this.ergebnisse_schreiben();
		this.mannschaften_schreiben();
	}
	
	public Mannschaft[] getMannschaften() {
		return this.mannschaften;
	}

    public int getIndexOfMannschaft(String name) {
    	for (Mannschaft ms : this.mannschaften) {
    		if (ms.getName().equals(name)) {
    			return ms.getId();
    		}
    	}
    	return -1;
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
			return MyDate.datum(this.daten_uhrzeiten[matchday][0], this.tageseitfr[this.daten_uhrzeiten[matchday][spiel + 1]])
					+ " " + MyDate.uhrzeit(this.anstosszeiten[this.daten_uhrzeiten[matchday][spiel + 1]]);
		else
			return "nicht terminiert";
	}
	
	public Ergebnis getErgebnis(int matchday, int match) {
		return ergebnisse[matchday][match];
	}
	
	public void setErgebnis(int matchday, int match, int goalsLeft, int goalsRight) {
		ergebnisse[matchday][match] = new Ergebnis(goalsLeft, goalsRight);
	}
	
	public void setErgebnis(int matchday, int match, Ergebnis ergebnis) {
		ergebnisse[matchday][match] = ergebnis;
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
		
		log("Rest: " + daten.substring(daten.indexOf(";A_SAI*") + 1) + "\n");
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
        this.ergebnisse = new Ergebnis[this.anzahl_spieltage][this.anzahl_spiele];
        this.spieltageEingetragen = new boolean[this.anzahl_spieltage];
        this.ergebnisseEingetragen = new boolean[this.anzahl_spieltage];
    }
	
    private void mannschaften_laden() {
    	this.teamsFromFile = ausDatei(this.dateiTeams);
        
        if (this.teamsFromFile.length > 0) {
        	int counter = 0;
        	
        	// in der ersten Zeile steht die Anzahl der Mannschaften
            int anzahl_mannschaften = Integer.parseInt(this.teamsFromFile[counter]);
            counter++;
            
            this.anzahl_mannschaften = 0;
            for (int i = 0; i < anzahl_mannschaften; i++) {
                addNewTeam(this.teamsFromFile[i + counter], false);
            }
            counter += anzahl_mannschaften;
        } else {
        	this.anzahl_mannschaften = 0;
        }
    }
    
    private void mannschaften_schreiben() {
//    	this.jListConfigModel.clear();
    	
    	this.teamsFromFile = new String[this.anzahl_mannschaften + 1];
    	
    	this.teamsFromFile[0] = "" + this.anzahl_mannschaften;
		for (int i = 0; i < this.anzahl_mannschaften; i++) {
			this.teamsFromFile[i + 1] = this.mannschaften[i].toString();
		}
		inDatei(this.dateiTeams, this.teamsFromFile);
    }
    
	private void spielplan_laden() {
		try {
			this.spielplanFromFile = ausDatei(this.dateiSpielplan); 
		    
	        int counter = 0;
	        
	        // Anstosszeiten / Spieltermine
	        String as_zeiten = this.spielplanFromFile[0];
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
	            String e = this.spielplanFromFile[counter + 1];
	            this.spieltageEingetragen[counter] = Boolean.parseBoolean(e.substring(0, e.indexOf(";")));
	            e = e.substring(e.indexOf(";") + 1);
	            int count2 = 0;
	            if (this.spieltageEingetragen[counter]) {
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
        this.spielplanFromFile = new String[this.anzahl_spieltage + 1];
//		this.jListSpielplanModel.clear();
        
		String string = this.anzahl_spieltermine + ";";
		for (int i = 0; i < this.anzahl_spieltermine; i++) {
			string = string + this.tageseitfr[i] + "," + this.anstosszeiten[i] + ";";
        }
		this.spielplanFromFile[0] = string;
		
        for (int i = 0; i < this.anzahl_spieltage; i++) {
            string = this.spieltageEingetragen[i] + ";";
            if (this.spieltageEingetragen[i]) {
            	string = string + this.daten_uhrzeiten[i][0];
            	for (int j = 0; j < this.anzahl_spiele; j++) {
            		string = string + ":" + daten_uhrzeiten[i][j + 1];
            	}
            	string = string + ";";
                for (int j = 0; j < this.spieltage[i].length; j++) {
                    string = string + this.spieltage[i][j][0] + ":" + this.spieltage[i][j][1] + ";";
                }
            }
            
            this.spielplanFromFile[i + 1] = string;
        }
        
        inDatei(this.dateiSpielplan, this.spielplanFromFile);
    }
	
	private void ergebnisse_laden() {
		try {
			this.ergebnisseFromFile = ausDatei(this.dateiErgebnisse);
	        int counter;
	        
	        for (counter = 0; counter < this.anzahl_spieltage; counter++) {
	            String element = this.ergebnisseFromFile[counter];
	            this.ergebnisseEingetragen[counter] = Boolean.parseBoolean(element.substring(0, element.indexOf(";")));
	            element = element.substring(element.indexOf(";") + 1);
	            int match = 0;
	            if (this.spieltageEingetragen[counter] && this.ergebnisseEingetragen[counter]) {
	                while (element.indexOf(";") != -1) {
	                    Ergebnis ergebnis = new Ergebnis(element.substring(0, element.indexOf(";")));
		            	
		            	setErgebnis(counter, match, ergebnis);
	                    
	                    this.mannschaften[this.spieltage[counter][match][0] - 1].setResult(counter, ergebnis);
	                    this.mannschaften[this.spieltage[counter][match][1] - 1].setResult(counter, ergebnis);
	                    
	                    element = element.substring(element.indexOf(";") + 1);
	                    match++;
	                }
	            }
	            while (match < this.anzahl_spiele) {
	            	setErgebnis(counter, match, null);
	            	match++;
	            }
	        }
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Kein Ergebnisseplan", "", JOptionPane.ERROR_MESSAGE);
		}
    }
	
	private void ergebnisse_schreiben() {
        this.ergebnisseFromFile = new String[this.anzahl_spieltage];
        for (int i = 0; i < this.anzahl_spieltage; i++) {
            String string = this.ergebnisseEingetragen[i] + ";";
            if (this.ergebnisseEingetragen[i]) {
				for (int j = 0; j < ergebnisse[i].length; j++) {
                	string += getErgebnis(i, j) + ";";
				}
            }
            
            this.ergebnisseFromFile[i] = string;
        }
        
        inDatei(this.dateiErgebnisse, this.ergebnisseFromFile);
    }
	
}
