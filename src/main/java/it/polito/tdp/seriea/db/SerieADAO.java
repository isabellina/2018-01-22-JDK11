package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons(Map<Integer, Season> idMap, Team team) {
		String sql = "SELECT m.Season AS r, s.description AS d " + 
				"FROM matches AS m, seasons AS s " + 
				"WHERE (m.HomeTeam = ? OR m.AwayTeam = ?) AND m.Season = s.season " + 
				"GROUP BY m.Season";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = new Season(res.getInt("r"), res.getString("d"));
				result.add(s);
				idMap.put(res.getInt("r"), s);
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
		String sql = "SELECT team FROM teams ORDER BY team";
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
	
	
	public void puntiSeasons(Map<Season, Integer> punti, Team team, Map<Integer, Season> idMap) {
		String sql = "SELECT m.Season AS id, m.HomeTeam AS home, m.AwayTeam AS away, m.FTR AS r " + 
				"FROM matches AS m " + 
				"WHERE m.HomeTeam = ? OR m.AwayTeam = ? " + 
				"ORDER BY m.Season";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Integer s = res.getInt("id");
				if(idMap.containsKey(s)) {
					Integer p = punti.get(idMap.get(s));
					String home = res.getString("home");
					String away = res.getString("away");
					String result = res.getString("r");
					if((home.equals(team.getTeam()) && result.equals("H")) || (away.equals(team.getTeam()) && result.equals("A"))) {
						p += 3;
					} else if(result.equals("D")) {
						p += 1;
					}
					punti.put(idMap.get(s), p);
				}
			}

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
