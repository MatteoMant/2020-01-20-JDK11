package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Exhibition;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects() {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
				result.add(artObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Exhibition> listExhibitions() {
		
		String sql = "SELECT * from exhibitions";
		List<Exhibition> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Exhibition exObj = new Exhibition(res.getInt("exhibition_id"), res.getString("exhibition_department"), res.getString("exhibition_title"), 
						res.getInt("begin"), res.getInt("end"));
				
				result.add(exObj);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getAllRoles() {
		
		String sql = "SELECT DISTINCT role FROM authorship";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(res.getString("role"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getAllArtists(Map<Integer, Artist> idMap) {
		
		String sql = "SELECT * FROM artists";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("artist_id"))) {
					Artist a = new Artist(res.getInt("artist_id"), res.getString("name"));
					idMap.put(a.getArtist_id(), a);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Artist> getAllArtistsWithRole(String role) {
		
		String sql = "SELECT * "
				   + "FROM artists "
				   + "WHERE artist_id IN (SELECT artist_id FROM authorship WHERE role = ?)";
		List<Artist> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, role);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				result.add(new Artist(res.getInt("artist_id"), res.getString("name")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public List<Adiacenza> getAllAdiacenze(Map<Integer, Artist> idMap, String ruolo){
		String sql = "SELECT a1.artist_id AS a1, a2.artist_id AS a2, COUNT(DISTINCT eo1.exhibition_id) AS peso "
				+ "FROM exhibition_objects eo1, exhibition_objects eo2, authorship a1, authorship a2 "
				+ "WHERE eo1.exhibition_id = eo2.exhibition_id AND eo1.object_id > eo2.object_id AND eo1.object_id = a1.object_id AND eo2.object_id = a2.object_id AND a1.role = ? AND a2.role = ? "
				+ "AND a1.artist_id > a2.artist_id "
				+ "GROUP BY a1.artist_id, a2.artist_id";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, ruolo);
			st.setString(2, ruolo);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Artist a1 = idMap.get(res.getInt("a1"));
				Artist a2 = idMap.get(res.getInt("a2"));
				int peso = res.getInt("peso");
				Adiacenza a = new Adiacenza(a1, a2, peso);
				result.add(a);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
