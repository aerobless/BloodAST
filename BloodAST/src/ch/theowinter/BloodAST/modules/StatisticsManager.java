package ch.theowinter.BloodAST.modules;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ch.theowinter.BloodAST.MainBloodAST;

public class StatisticsManager {
	private MainBloodAST main;
	
	//Loaded Parameters
	int statsUpdatePeriod = 4;

    public StatisticsManager(MainBloodAST mainClass) {
		super();
		main = mainClass;
		statsUpdatePeriod = main.getConfig().getInt("StatisticsUpdatePeriod");
	}
    
    public void StatsTracker(){
    	main.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
            	checkStats();
            	StatsTracker();
            }
        }, statsUpdatePeriod*60*20L  ); //20L = 1second
    }
    
	//Getting players from single bukkit server
    public void checkStats(){
    	Player[] currentlyOnlinePlayerArray = Bukkit.getServer().getOnlinePlayers();
    	
    	int currentPlayersOnline = currentlyOnlinePlayerArray.length;
    		updateDatabase(main.getServerName(), currentPlayersOnline);
    }
 
    public void updateDatabase(String servername, int i) {
		// SEND UPDATED VALUE
		Map<String, String> data = new HashMap<String, String>();
		data.put("timeStamp", getUnixTimeStamp());
		data.put(main.getServerName(), ""+i);
		try {
			submitToWeb(main.getWebServerURL(), data);
		} catch (Exception e) {
			main.logEvent("ERROR: Cought IO Exception from the webUploader. You're URL is probably bad.", false);
		}	
	}
    
    public void submitToWeb(String url, Map<String, String> data) throws IOException{
		URL siteUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) siteUrl.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);

		DataOutputStream out = new DataOutputStream(conn.getOutputStream());

		Set<String> keys = data.keySet();
		Iterator<String> keyIter = keys.iterator();
		String content = "";
		for (int i = 0; keyIter.hasNext(); i++) {
			Object key = keyIter.next();
			if (i != 0) {
				content += "&";
			}
			content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
		}
		out.writeBytes(content);
		out.flush();
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line = "";
		while ((line = in.readLine()) != null) {
		}
		in.close();
	}
    
    /**
     * Used to get "imprecise" Unix time. Needed to make sure that all servers report more or less the same time.
     * @return
     */
    public String getUnixTimeStamp(){
		long unixTime = System.currentTimeMillis() / 1000L;
		long makeUnixTimeImprecise = unixTime/100;
		makeUnixTimeImprecise = makeUnixTimeImprecise*100;
		return Long.toString(makeUnixTimeImprecise);
    }
}

