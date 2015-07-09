package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Turnier {
	private Start start;
	private int id;
	private String name;
	private String shortName;
	
	private int aktuelleSaison;
	private ArrayList<TurnierSaison> saisons;
	
	private String workspace;
	
	private String dateiSaisonsDaten;
	private ArrayList<String> saisonsDatenFromFile;
	
	
	public Turnier(int id, Start start, String daten) {
		this.start = start;
		this.id = id;
		fromString(daten);
		saisonsLaden();
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getWorkspace(int season) {
		int seasonIndex = 0;
		for (seasonIndex = 0; seasonIndex < saisons.size(); seasonIndex++) {
			if (saisons.get(seasonIndex).getSeason() == season)	break;
		}
		return workspace + saisons.get(seasonIndex).getSeasonFull("_") + File.separator;
	}
	
	public String getWorkspace() {
		return workspace;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getShortName() {
		return this.shortName;
	}
	
	/**
	 * This method returns all available seasons in the format "2014/2015" if isSTSS, otherwise "2014".
	 * @return a String array containing all available seasons
	 */
	public String[] getAllSeasons() {
		String[] hilfsarray = new String[this.saisons.size()];
        for (int i = 0; i < saisons.size(); i++) {
            hilfsarray[i] = saisons.get(i).getSeasonFull("/");
        }
        return hilfsarray;
	}
	
	public int getCurrentSeason() {
		return this.saisons.get(this.aktuelleSaison).getSeason();
	}
	
	public TurnierSaison getAktuelleSaison() {
		return saisons.get(this.aktuelleSaison);
	}
	
	public boolean addNewSeason(String toString, ArrayList<String> qConfig, String[][] teamsQG, String[][] teamsQKO, ArrayList<String> grpConfig, String[][] teamsGrp, ArrayList<String> koConfig, String[][] teamsKO) {
		TurnierSaison neueSaison = new TurnierSaison(start, this, saisons.size(), toString);
		for (int i = 0; i < saisons.size(); i++) {
			if (saisons.get(i).getSeason() == neueSaison.getSeason()) {
				message("Eine Saison mit diesem Startjahr existiert bereits.");
				return false;
			}
		}
		saisons.add(neueSaison);
		
		String folder = workspace + neueSaison.getSeasonFull("_") + File.separator;
		(new File(folder)).mkdirs();
		
		return true;
	}
	
	private void saisonsLaden() {
		workspace = start.getWorkspace() + File.separator + name + File.separator;
		
		// SaisonsConfig.txt
		dateiSaisonsDaten = workspace + "SaisonsConfig.txt";
		saisonsDatenFromFile = ausDatei(dateiSaisonsDaten);
		
		// TurnierSaisons erstellen
		saisons = new ArrayList<>();
		for (int i = 0; i < saisonsDatenFromFile.size(); i++) {
			saisons.add(new TurnierSaison(start, this, i, saisonsDatenFromFile.get(i)));
		}
	}
	
	private void saisonsSpeichern() {
		saisonsDatenFromFile.clear();
		for (int i = 0; i < saisons.size(); i++) {
			saisons.get(i).speichern();
			saisonsDatenFromFile.add(saisons.get(i).toString());
		}
		
		inDatei(dateiSaisonsDaten, saisonsDatenFromFile);
	}
	
	public void laden(int index) {
		aktuelleSaison = index;
		saisons.get(aktuelleSaison).laden();
	}
	
	public void speichern() {
		saisonsSpeichern();
	}
	
	private void fromString(String daten) {
		String[] alleDaten = daten.split(";");
		
		this.name = alleDaten[0].substring(5);
		this.shortName = alleDaten[1].substring(4);
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		alles += "SHN*" + this.shortName + ";";
		return alles;
	}
}
