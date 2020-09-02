package it.polito.tdp.seriea.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao ;
	private Map<Integer,Integer> mappaPunti ;
	private Graph<Integer,DefaultWeightedEdge> grafo;
	
	
	 public Model() {
		 this.dao = new SerieADAO();
		 this.mappaPunti = new TreeMap<Integer,Integer>();
		 
		 
	 }
	 
	 
	public List<String> getSquadra(){
		
		List<String> s = new LinkedList<String>(this.dao.getTeam());
		return s;
	}
	
	public String getClassifica(String sq){
		this.dao.getStagioni(mappaPunti, sq);
		this.dao.getResult(mappaPunti, sq);
		String s = "";
		for(Integer i : this.mappaPunti.keySet()) {
			s+= "anno: " + i + " punteggio: " + this.mappaPunti.get(i) + "\n"; 
		}
		return s;
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.mappaPunti.keySet()) ;
		for(Integer i : this.mappaPunti.keySet()) {
			for(Integer j : this.mappaPunti.keySet()) {
				if(i!=j) {
					int diff = this.mappaPunti.get(i)-this.mappaPunti.get(j);
					if(diff>0) {
						Graphs.addEdgeWithVertices(this.grafo, j, i, diff) ;
					}
					else if(diff<0) {
						Graphs.addEdgeWithVertices(this.grafo, i, j, Math.abs(diff)) ;
					}
				}
			}
		}
		
		System.out.println(this.grafo);
		
		
	}
	
	public String getAnnataDoro() {
		int sumEntranti = 0;
		int sumUscenti = 0;
		int max = 0;
		int stagione = 0;
		String s = "" ;
		for(Integer i : this.grafo.vertexSet()) {
			for(DefaultWeightedEdge d : this.grafo.incomingEdgesOf(i)) {
				sumEntranti += this.grafo.getEdgeWeight(d) ;
			}
			for(DefaultWeightedEdge f : this.grafo.outgoingEdgesOf(i)) {
				sumUscenti += this.grafo.getEdgeWeight(f);
			}
			int diff = sumEntranti -sumUscenti ;
			if(diff>max) {
				max = diff;
				stagione = i;
			}
		}
		s = "Stagione: " + stagione + " diff: "+ max ;
		return s;
	}
	
	

}
