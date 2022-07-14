package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private Map<String, Business> idMap;
	private Map<String, Business> idMapName;
	
	public Model() {
		dao = new YelpDao();
	}
	
	public List<String> getAllCities(){
		return this.dao.getAllCities();
	}
	
	public void creaGrafo(String c) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap = new HashMap<>();
		this.idMapName = new HashMap<>();
		
		for (Business b : this.dao.getAllVertici(c))
			idMap.put(b.getBusinessId(), b);
		for (Business b : this.dao.getAllVertici(c))
			idMapName.put(b.getBusinessName(), b);
		
		Graphs.addAllVertices(this.grafo, this.dao.getAllVertici(c));
		
		for (Adiacenza a : this.dao.getAllAdiacenze(c, idMap)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getB1(), a.getB2(), a.getPeso());
		}
	}
	
	public String doDistante(String b){
		double max = 0;
		String nomeBusiness = "";
		for (Business btemp : Graphs.neighborListOf(this.grafo, idMapName.get(b))) {
			DefaultWeightedEdge e = this.grafo.getEdge(idMapName.get(b), btemp);
			double peso = this.grafo.getEdgeWeight(e);
			if (peso>=max) {
				max=peso;
				nomeBusiness = btemp.getBusinessName();
			}
		}
		return nomeBusiness + " " + max;
	}
	
	public Set<Business> getAllVertici(){
		return this.grafo.vertexSet();
	}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
}
