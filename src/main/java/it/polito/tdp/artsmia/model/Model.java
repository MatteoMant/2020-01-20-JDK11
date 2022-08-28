package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<>();
		dao.getAllArtists(idMap);
	}
	
	public void creaGrafo(String ruolo) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// Aggiunta dei vertici
		Graphs.addAllVertices(this.grafo, this.dao.getAllArtistsWithRole(ruolo));		
		
		// Aggiunta degli archi
		for (Adiacenza a : this.dao.getAllAdiacenze(idMap, ruolo)) {
			Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<String> getAllRoles(){
		return this.dao.getAllRoles();
	}
}
