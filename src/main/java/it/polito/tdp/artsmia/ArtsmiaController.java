package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	
    	List<Adiacenza> connessi = this.model.getAllAdiacenze();
    	txtResult.appendText("ARTISTI CONNESSI: \n");
    	for (Adiacenza a : connessi) {
    		txtResult.appendText(a + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	
    	try {
    		int artistId = Integer.parseInt(txtArtista.getText());
    		Artist artista = this.model.getArtist(artistId);
    		if (artista == null) {
    			txtResult.appendText("Artista inesistente!\n");
    			return;
    		}
    		
    		List<Artist> percorso = this.model.ricercaCammino(artista);
    		if (percorso == null) {
    			txtResult.appendText("NON esiste un cammino!\n");
    			return;
    		} else {
    			txtResult.appendText("Il cammino trovato è: \n");
    			for (Artist a : percorso) {
    				txtResult.appendText(a + "\n");
    			}
    		}
    		
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Per favore inserire un id artista valido!\n");
    		return;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String ruolo = boxRuolo.getValue();
    	if (ruolo == null) {
    		txtResult.setText("Per favore selezionare un ruolo!\n");
    		return;
    	}
    	this.model.creaGrafo(ruolo);
    	
    	txtResult.setText("Grafo creato!\n");
    	txtResult.appendText("# Vertici : " + this.model.getNumVertici() + "\n");
    	txtResult.appendText("# Archi : " + this.model.getNumArchi() + "\n");
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	
    	// Adesso posso popolare il menu a tendina dei ruoli
    	List<String> ruoli = this.model.getAllRoles();
    	Collections.sort(ruoli);
    	boxRuolo.getItems().addAll(ruoli);
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";
    }
}
