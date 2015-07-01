package model;

import java.io.File;
import java.util.ArrayList;

import static util.Utilities.*;

public class Turnier {
	private Start start;
	private int id;
	private String name;
	private String shortName;
	
	private boolean isSummerToSpringSeason;
	
	private ArrayList<Integer> seasons;
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
		for (seasonIndex = 0; seasonIndex < seasons.size(); seasonIndex++) {
			if (seasons.get(seasonIndex) == season)	break;
		}
		return workspace + getSeason(seasonIndex) + File.separator;
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
		String[] hilfsarray = new String[this.seasons.size()];
        for (int i = 0; i < this.seasons.size(); i++) {
            hilfsarray[i] = this.seasons.get(i) + (this.isSummerToSpringSeason ? "/" + (this.seasons.get(i) + 1) : "");
        }
        return hilfsarray;
	}
	
	public String getSeasonsRepresentation() {
		String representation = "";
		for (int i = 0; i < this.seasons.size(); i++) {
			String trenn = "S" + i;
			representation += trenn + "*" + this.seasons.get(i) + "*" + trenn + ",";
		}
		return representation.substring(0, representation.length() - 1);
	}
	
	public ArrayList<Integer> getSeasonsFromRepresentation(String representation) {
		String[] seasonsReps = representation.split(",");
		ArrayList<Integer> seasons = new ArrayList<>();
		
		for (int i = 0; i < seasonsReps.length; i++) {
			String rep = seasonsReps[i];
			seasons.add(Integer.parseInt(rep.substring(rep.indexOf("*") + 1, rep.lastIndexOf("*"))));
		}
		
		return seasons;
	}
	
	public String getSeason(int seasonIndex) {
		return this.seasons.get(seasonIndex) + (this.isSummerToSpringSeason ? "_" + (this.seasons.get(seasonIndex) + 1) : "");
	}
	
	public int getCurrentSeason() {
		return this.seasons.get(this.aktuelleSaison);
	}
	
	public TurnierSaison getAktuelleSaison() {
		return saisons.get(this.aktuelleSaison);
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
		this.seasons = getSeasonsFromRepresentation(alleDaten[2]);
	}
	
	public String toString() {
		String alles = "NAME*" + this.name + ";";
		alles += "SHN*" + this.shortName + ";";
		alles += getSeasonsRepresentation() + ";";
		return alles;
	}
}
