package main.Linux3000.manage;

import com.sun.xml.internal.ws.addressing.WsaActionUtil;

import java.sql.*;
import java.util.OptionalLong;

public class SQLManager {

	public static void onCreate() {

		Connection connection = null;
		try {
			connection = LiteSQL.getConnection();
			Statement stmt = connection.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS expsystem(guildid BIGINT, memberid BIGINT, xp INTEGER)");
			stmt.execute("CREATE TABLE IF NOT EXISTS guilds(guildid BIGINT, premium TEXT DEFAULT 'false')");
			stmt.execute("CREATE TABLE IF NOT EXISTS level_stats(guildid BIGINT, countdown INTEGER, level_up_channel VARCHAR(256))");
			stmt.execute("CREATE TABLE IF NOT EXISTS level_roles(guildid BIGINT, role_id INTEGER, level INTEGER)");
			stmt.execute("CREATE TABLE IF NOT EXISTS level(guildid BIGINT, level INTEGER, xp INTEGER)");
			stmt.execute("CREATE TABLE IF NOT EXISTS pictures(guildid BIGINT, on_join TEXT,join_string TEXT, on_leave INTEGER, leave_string TEXT)");
			stmt.execute("CREATE TABLE IF NOT EXISTS youtube(guildid BIGINT, youtube_channel_id TEXT, channel_id TEXT, yt_text TEXT)");
			stmt.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}



		//TODO: Reaction Roles, Temp Channels

	}

	public static void setupGuilds(long guildid) throws SQLException {
		Connection connection = LiteSQL.getConnection();
		Statement statement = connection.createStatement();
		ResultSet set = statement.executeQuery("SELECT * FROM guilds WHERE guildid = "+ guildid);
		System.out.println(set);
		if(!set.next()) {
			LiteSQL.onUpdate("INSERT INTO guilds(guildid) VALUES (" + guildid + ")");
		}
			statement.close();
		connection.close();

	}

	public static String readJoinPathData(long guildid) throws SQLException {
		ResultSet set = LiteSQL.onQuery("SELECT on_join FROM pictures WHERE guildid = "+ guildid);
		if(!set.next()) {
			return set.getString(1);
		}
		else {
			return null;
		}
	}

	public static String getYTChannelID(String guildid) throws SQLException {
		ResultSet resultSet = LiteSQL.onQuery("SELECT * FROM youtube WHERE guildid =" + guildid );
		if (resultSet.next()) {
			return resultSet.getString("youtube_channel_id");
		}
		else {
			return null;
		}


	}

	public static String getChannelID(String guildid) throws SQLException {

		ResultSet resultSet = LiteSQL.onQuery("SELECT * FROM youtube WHERE guildid =" + guildid );
			if (resultSet.next()) {
				return resultSet.getString("channel_id");
			}
			else {
				return null;
			}



	}




}
	

