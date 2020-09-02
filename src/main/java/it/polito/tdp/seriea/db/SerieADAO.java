package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "select team from teams order by team ASC ";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> getTeam(){
		String sql = "select team from teams order by team ASC ";
		List<String> result = new ArrayList<String>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("team"));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public void getStagioni(Map<Integer,Integer> m, String s) {
		String sql = "select distinct m.`Season` as sea " + 
				"from matches as m " + 
				"where m.`AwayTeam` = ? or m.`HomeTeam`=?";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s);
			st.setString(2, s);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				if(!m.containsKey(res.getInt("sea"))) {
					m.put(res.getInt("sea"), 0) ;
				}
			}

			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	
	public void getResult(Map<Integer,Integer> m, String s) {
		String sql = "select m.`HomeTeam` as ht, m.`AwayTeam` as at , m.`FTR` as ftr, m.`Season` as sea " + 
				"from  matches as m " + 
				"where m.`HomeTeam` = ? or m.`AwayTeam`=? " ;
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s);
			st.setString(2, s);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				for(Integer i : m.keySet()) {
					if(i==res.getInt("sea") && res.getString("ht").compareTo(s)==0 && res.getString("ftr").compareTo("H")==0) {
						int val = m.get(i) + 3;
						m.put(i,val ) ;
					}
					else if(i==res.getInt("sea") && res.getString("at").compareTo(s)==0 && res.getString("ftr").compareTo("A")==0) {
						int val2 = m.get(i) + 3;
					    m.put(i,val2 ) ;
					}
					else if(i==res.getInt("sea") && res.getString("ftr").compareTo("D")==0 ) {
						int val3 = m.get(i) + 1;
						 m.put(i,val3 ) ;
					}
				}
			}

			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
		
	}


