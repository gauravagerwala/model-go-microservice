package org.example;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.printf("Looking for services in folder %s", args[0]);

        ParseDependency parser = new ParseDependency();
        Map<String, List<String>> dependencies = new HashMap<>();
        try{
            dependencies = parser.parseConfigurations(args[0]);
        } catch (IOException e){
            e.printStackTrace();
        }

//        dependencies = new HashMap<>();
//        dependencies.put("serviceA", new String[]{"serviceB", "serviceC"});
//        dependencies.put("serviceB", new String[]{"serviceC"});
//        dependencies.put("serviceC", new String[]{});

        ModelGenerator generator = new ModelGenerator();
        try {
            generator.generateModel(dependencies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}