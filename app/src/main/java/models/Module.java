package models;

import java.util.ArrayList;

public class Module {
    public int id;
    public String name;
    public String code;

    public Module(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public static int findIdIn(ArrayList<Module> modulesList, String moduleName){
        for(Module module : modulesList){
            if(module.name.equals(moduleName)){
                return  module.id;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
