package org.example;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ParseDependency {
    public ParseDependency(){}

    public Map<String, List<String>> parseConfigurations(String dir) throws IOException {

        Map<String, List<String>> dependencies = new HashMap<>();
        File[] directories = new File(dir).listFiles(File::isDirectory);

        assert directories != null;
        for (File directory: directories) {
            File[] modFile = directory.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.getName().equals("go.mod");
                }
            });
            FileReader reader = null;
            try{
                assert modFile != null;
                reader = new FileReader(modFile[0], StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line;
                String foundService = "";
                while((line = bufferedReader.readLine()) != null){
                    if (line.contains("module")){
                        foundService = line.substring(6);
                        break;
                    }
                }
                List<String> dependentServices = new ArrayList<String>();
                while((line = bufferedReader.readLine()) != null){
                    if (line.contains("require")){
                        String moduleLine;
                        while((moduleLine = bufferedReader.readLine()) != null){
                            if(moduleLine.equals(")")){
                                break;
                            }

                            dependentServices.add(moduleLine);
                        }
                    }
                }

                dependencies.put(foundService, dependentServices);

            } catch (IOException e){
                e.printStackTrace();
            } finally {
                if(reader != null){
                    reader.close();
                }
            }
        }
        System.out.println(dependencies);

        return dependencies;
    }
}
