package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(Match m) {
		//iseriamo nel grafo solo i giocatori che hanno preso parte al match
		grafo = new SimpleDirectedWeightedGraph(DefaultWeightedEdge.class);
		
		//aggiungiamo i vertitci
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(m, idMap));
		
		//aggiungiamo gli archi
		for (Adiacenza a : dao.getAdiacenze(m, idMap)) {
			if (a.getPeso()>=0) {
				//p1 meglio di p2
				if (this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				} else {
					if (this.grafo.containsVertex(a.getP1()) && this.grafo.containsVertex(a.getP2())) {
						Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(),(-1)* a.getPeso());
				}
			}	
		}
		}
		

		System.out.println("Vertici " + this.grafo.vertexSet().size());
		System.out.println("Archi " + this.grafo.edgeSet().size());
	}
	

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		// TODO Auto-generated method stub
		return this.grafo;
	}

	public class Comparatore implements Comparator<Match>{

		@Override
		public int compare(Match o1, Match o2) {
			// TODO Auto-generated method stub
			return o1.getMatchID().compareTo(o2.getMatchID());
		}
		
	}
	public List<Match> getTuttiMatch(){
		List<Match> matches = dao.listAllMatches();
		
		Collections.sort(matches, new Comparatore());
		
		return matches;
	}
	
	public GiocatoreMigliore getMigliore() {
		if (this.grafo ==  null) {
			return null;
		}
		Player best = null;
		Double maxDelta = -999900000.0;
		
		for (Player p : this.grafo.vertexSet()) {
			//calcolo la somma dei pesi degli archi uscenti
			double pesoUscente =0.0;
			for(DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente += this.grafo.getEdgeWeight(edge);
			}
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante += this.grafo.getEdgeWeight(edge);
			}
			double delta = pesoUscente -pesoEntrante;
			
			if (maxDelta< delta) {
				maxDelta = delta;
				best = p;
			}
			
		}
		
		return new GiocatoreMigliore (best, maxDelta);
	}

}

