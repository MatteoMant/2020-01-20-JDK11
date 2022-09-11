package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private Graph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> idMap;
	
	// parametri della simulazione
	private List<Artist> best;
	
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
	
	public List<Adiacenza> getAllAdiacenze(){
		List<Adiacenza> result = new LinkedList<>();
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Artist a1 = this.grafo.getEdgeSource(e);
			Artist a2 = Graphs.getOppositeVertex(this.grafo, e, a1);
			int peso = (int)this.grafo.getEdgeWeight(e);
			result.add(new Adiacenza(a1, a2, peso));
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public List<Artist> ricercaCammino(Artist partenza){
		this.best = new LinkedList<>();
		List<Artist> parziale = new LinkedList<>();
		
		parziale.add(partenza);
		
		// facciamo partire la ricorsione
		cerca(parziale, -1);
		
		return this.best;
	}
	
	public void cerca(List<Artist> parziale, int pesoCammino) {
		Artist ultimo = parziale.get(parziale.size()-1);
				
		List<Artist> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		for (Artist v : vicini) {
			
			if (!parziale.contains(v) && pesoCammino == -1) {
				parziale.add(v);
				cerca(parziale, (int)this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, v)));
				parziale.remove(v);
			} else {
				if (!parziale.contains(v) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, v)) == pesoCammino) {
					parziale.add(v);
					cerca(parziale, pesoCammino);
					parziale.remove(v);
				}
			}
			
		}
		
		// Quando termina un livello della ricorsione mi salvo la soluzione migliore fino a quel momento
		if(parziale.size() > best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		
	}
	
	public Artist getArtist(int idArtista) {
		return idMap.get(idArtista);
	} 
	
	public List<String> getAllRoles(){
		return this.dao.getAllRoles();
	}
	
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
}
