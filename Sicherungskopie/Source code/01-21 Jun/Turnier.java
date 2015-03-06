import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Turnier {
	
	private int id;
	
	private String name;
	
	private Mannschaft[] mannschaften;
	private Gruppe[] gruppen;
	
	private int numberOfTeams;
	private int numberOfGroups;
	private int numberOfTeamsPerGroup;
	
	String workspaceWIN = "C:\\Users\\vsh\\inspectit2\\vshs-inspectit-sample\\samples\\Fussball";
	String workspaceMAC = "/Users/valentinschraub/Documents/workspace/Bundesliga";
	
	// TODO a method that checks the operating system
	String workspace = workspaceMAC;
	
	String ergebnisse;
	String spielplan;
	String teams;
	
	DefaultListModel jListConfigModel = new DefaultListModel();
	DefaultListModel jListSpielplanModel = new DefaultListModel();
	DefaultListModel jListErgebnisseModel = new DefaultListModel();
	
	boolean[] spieltage_eingetragen;
	int[][][] spieltage;
	
	int[][] auslosung;
	
	
	public Turnier(int id, String daten) {
		fromString(daten);
		
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getNumberOfGroups() {
		return this.numberOfGroups;
	}
	
	public Gruppe[] getGruppen() {
		return this.gruppen;
	}
	
	public void laden() {
		ergebnisse = workspace + File.separator + name + File.separator + "Ergebnisse.txt";
		spielplan = workspace + File.separator + name + File.separator + "Spielplan.txt";
    	teams = workspace + File.separator + name + File.separator + "Mannschaften.txt";
		
    	mannschaftenLaden();
    	spielplanLaden();
    	ergebnisseLaden();
	}
	
	public void speichern() {
		
	}
	
	public void mannschaftenLaden() {
    	aus_datei(teams, jListConfigModel);
		
    	this.numberOfTeams = Integer.parseInt((String) jListConfigModel.getElementAt(0));
		this.numberOfGroups = 8;
		this.numberOfTeamsPerGroup = this.numberOfTeams / this.numberOfGroups;
    	jListConfigModel.remove(0);
    	
    	mannschaften = new Mannschaft[numberOfTeams];
    	for (int i = 0; i < mannschaften.length; i++) {
			mannschaften[i] = new Mannschaft(i, numberOfTeamsPerGroup - 1);
			mannschaften[i].setName((String) jListConfigModel.getElementAt(i));
		}
    	
    	auslosung = new int[numberOfGroups][numberOfTeamsPerGroup];
    	for (int i = 0; i < mannschaften.length; i++) {
    		auslosung[i / numberOfTeamsPerGroup][i % numberOfTeamsPerGroup] = Integer.parseInt((String) jListConfigModel.getElementAt(numberOfTeams + i));
    	}
    	
    	gruppen = new Gruppe[8];
    	for (int i = 0; i < 8; i++) {
    		Mannschaft[] teams = new Mannschaft[] {mannschaften[auslosung[i][0]], mannschaften[auslosung[i][1]], mannschaften[auslosung[i][2]], mannschaften[auslosung[i][3]]};
    		gruppen[i] = new Gruppe(i, teams, this);
    	}
    	System.out.println("\n\n\n\n\n\n\n");
	}
	
	public void spielplanLaden() {
    	aus_datei(spielplan, jListSpielplanModel);
    	// TODO Spielplan laden
	}
	
	public void ergebnisseLaden() {
    	aus_datei(ergebnisse, jListErgebnisseModel);
    	// TODO Ergebnisse laden
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
	
	private void fromString(String daten) {
		this.name = "FIFA Weltmeisterschaft 2014";
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		return alles;
	}
	
}