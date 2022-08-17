package main.Linux3000.manage;

import java.io.File;

public class FileManager {

    public static void create(){
        File rsc = new File("./rsc");
        File join = new File("./rsc/join");
        if(!rsc.exists())
            rsc.mkdir();
        if(!join.exists()) {
            join.mkdir();
        }
    }

}
