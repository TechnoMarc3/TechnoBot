package main.Linux3000.stats.zon;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONReader {

    private long firstLevelUp;
    private long secondLevelUp;
    private long thirdLevelUp;

    private long textChannelId;

    private long cooldown;

    private String selectedJoinPicturePath;

    private long roleId1;
    private long roleId2;
    private long roleId3;
    private File file;
    JSONParser jsonParser = new JSONParser();
    private JSONObject jsonObject;
    public JSONReader() {
    }



    public JSONObject read() {

        try {
            Object bla = jsonParser.parse(new FileReader(file));
            jsonObject = (JSONObject) bla;
            this.firstLevelUp = (long) jsonObject.get("first");
            this.secondLevelUp = (long) jsonObject.get("second");
            this.thirdLevelUp = (long) jsonObject.get("third");
            this.selectedJoinPicturePath = (String) jsonObject.get("selectedJoinPicture") ;
            this.textChannelId = (long) jsonObject.get("lvlUpChannel");

            this.cooldown = (long) jsonObject.get("cooldown");

            this.roleId1 = (long) jsonObject.get("id1");
            this.roleId2 = (long) jsonObject.get("id2");
            this.roleId3 = (long) jsonObject.get("id3");


        } catch (IOException | ParseException ioException) {
            ioException.printStackTrace();
        }
        return jsonObject;
    }
    public void setData(Object key, Object value) {
        JSONObject obj = read();
        obj.put(key, value);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(obj.toJSONString());
            writer.flush();
        } catch (IOException eio) {
            eio.printStackTrace();
        }
    }

    public long getFirstLevelUp() {
        return firstLevelUp;
    }

    public long getSecondLevelUp() {
        return secondLevelUp;
    }

    public long getThirdLevelUp() {
        return thirdLevelUp;
    }

    public long getRoleId1() {
        return roleId1;
    }

    public long getRoleId2() {
        return roleId2;
    }

    public long getRoleId3() {
        return roleId3;
    }

    public long getCooldown() {
        return cooldown;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public String getSelectedJoinPicturePath() {
        return selectedJoinPicturePath;
    }
}
