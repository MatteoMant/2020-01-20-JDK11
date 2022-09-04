package it.polito.tdp.artsmia.model;

public class Artist {
	
	private int artist_id; 
	private String name;
	
	public Artist(int artist_id, String name) {
		super();
		this.artist_id = artist_id;
		this.name = name;
	}

	public int getArtist_id() {
		return artist_id;
	}

	public void setArtist_id(int artist_id) {
		this.artist_id = artist_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + artist_id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artist other = (Artist) obj;
		if (artist_id != other.artist_id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return artist_id + "";
	}
	
}
