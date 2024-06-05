package model;

import java.util.HashMap;
import java.util.Optional;

public class SpielSerien {
	private HashMap<SerienTyp, Integer> longestSeries;
	
	private SpielSerien() {
		
	}
	
	public int getLongestSeriesOfMatchesWith(SerienTyp seriesType) {
		return Optional.ofNullable(longestSeries.get(seriesType)).orElse(0);
	}
	
	public static SpielSerien of(HashMap<SerienTyp, Integer> longestSeries) {
		SpielSerien seriesDto = new SpielSerien();
		
		seriesDto.longestSeries = longestSeries;
		
		return seriesDto;
	}
}
