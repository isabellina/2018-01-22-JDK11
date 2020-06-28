package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private Graph<Season, DefaultWeightedEdge> graph;
	private SerieADAO dao;
	
	private Map<Season, Integer> seasonsPunti;
	private Map<Integer, Season> idMap;
	
	private Season bestSeason;
	private Integer bestDiff;
	
	private List<Season> bestPath;
	private List<Season> seasons;
	
	public Model() {
		this.dao = new SerieADAO();
	}
	
	public List<Team> getTeams() {
		return this.dao.listTeams();
	}
	
	public Map<Season, Integer> getSeasonsPunti(Team team) {
		this.seasonsPunti = new TreeMap<>();
		this.idMap = new HashMap<>();
		for(Season s : this.dao.listAllSeasons(idMap, team)) {
			this.seasonsPunti.put(s, 0);
		}
		this.dao.puntiSeasons(seasonsPunti, team, idMap);
		return this.seasonsPunti;
	}
	
	public void buildGraph(Team team) {
		this.graph = new SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.graph, this.seasonsPunti.keySet());
		
		for(Season s1 : this.graph.vertexSet()) {
			for(Season s2 : this.graph.vertexSet()) {
				if(!s1.equals(s2)) {
					Integer peso = this.seasonsPunti.get(s1) - this.seasonsPunti.get(s2);
					if(peso > 0)
						Graphs.addEdge(this.graph, s1, s2, peso);
					else
						Graphs.addEdge(this.graph, s2, s1, peso*(-1));
				}
			}
		}
		System.out.println(this.graph.vertexSet().size()+" "+this.graph.edgeSet().size());
	}
	
	public Season getAnnataOro(Team team) {
		this.bestDiff = 0;
		for(Season season : this.graph.vertexSet()) {
			Integer diff = this.calcolaDiff(season);
			if(diff > this.bestDiff) {
				this.bestDiff = diff;
				this.bestSeason = season;
			}
		}
		
		return this.bestSeason;
	}
	
	public Integer getBestDiff() {
		return this.bestDiff;
	}
	
	private Integer calcolaDiff(Season source) {
		Integer sumIn = 0;
		Integer sumOut = 0;
		
		for(DefaultWeightedEdge edge : this.graph.incomingEdgesOf(source)) {
			sumIn += (int) this.graph.getEdgeWeight(edge);
		}
		for(DefaultWeightedEdge edge : this.graph.outgoingEdgesOf(source)) {
			sumOut += (int) this.graph.getEdgeWeight(edge);
		}
		
		return sumIn - sumOut;
	}
	
	
	public List<Season> trovaPercorsoVirtuoso() {
		this.bestPath = new ArrayList<>();
		this.seasons = new ArrayList<Season>(this.seasonsPunti.keySet());
		
		List<Season> parziale = new ArrayList<Season>();
		this.ricorsiva(parziale, 0);
		
		return this.bestPath;
	}

	private void ricorsiva(List<Season> parziale, Integer livello) {
		
		if(livello > this.bestPath.size()) {
			this.bestPath = new ArrayList<>(parziale);
			return;
		}
		
		if(livello == 0) {
			for(Season s : this.graph.vertexSet()) {
				parziale.add(s);
				this.ricorsiva(parziale, livello+1);
				parziale.remove(parziale.size()-1);
			}
		} else {
			Season last = parziale.get(parziale.size()-1);
			for(Season vicino : Graphs.successorListOf(this.graph, last)) {
				if(!parziale.contains(vicino) && this.seasonsCorrette(last, vicino)) {
					parziale.add(vicino);
					this.ricorsiva(parziale, livello+1);
					parziale.remove(parziale.size()-1);
				}
			}
		}
		
		
	}

	private boolean seasonsCorrette(Season last, Season vicino) {
		for(int i = 0; i < this.seasons.size()-1; i++) {
			if(this.seasons.get(i).getSeason() == last.getSeason()) {
				if(this.seasons.get(i+1).getSeason() == vicino.getSeason())
					return true;
				else
					return false;
			}
		}
		
		return false;
	}

}
