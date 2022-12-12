package org.example;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ModelGenerator {

    private final String serviceSingletonDef = "one sig %s extends Service{ }\n";
    private final String factDefinition = "fact { \n\tApplication.services = %s \n";
    private final String dependencyDef = "\t%s.dependsOn = %s\n";
    private final String islandServiceDef = "\tno %s.dependsOn \n";

    private final String closeFactDefinition = "}\n";

    public ModelGenerator(){

    }

    public void generateModel(Map<String, List<String>> dependencies) throws IOException {
        File template = new File("template.als");
        File generatedModel = new File("generated_model.als");

        try {
            if (generatedModel.createNewFile()) {
                System.out.println("File created: " + generatedModel.getName());
            } else {
                System.out.println("File already exists.");
            }

            //Files.write(Path.of(generatedModel.getPath()), strToBytes);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        copyTemplate(template, generatedModel);
        writeServiceSignatures(dependencies, generatedModel);
        writeFact(dependencies, generatedModel);


    }

    private void writeServiceSignatures(Map<String, List<String>> dependencies, File generatedModel) throws IOException {
        FileWriter model = new FileWriter(generatedModel, true);
        PrintWriter writer = new PrintWriter(model);
        for (Map.Entry<String,List<String>> service :
                dependencies.entrySet()) {
            writer.printf(serviceSingletonDef, service.getKey());
        }
        writer.close();
        model.close();
    }
    private void writeFact(Map<String, List<String>> dependencies, File generatedModel) throws IOException {
        FileWriter model = new FileWriter(generatedModel, true);
        PrintWriter writer = new PrintWriter(model);

        StringBuilder servicesListAdded = new StringBuilder();
        int count = 0;
        for (String serviceName :
                dependencies.keySet()) {
            servicesListAdded.append(serviceName);
            if(count < dependencies.size() - 1){
                servicesListAdded.append(" + ");
                count ++;
            }
        }
        writer.printf(factDefinition, servicesListAdded);

        for (Map.Entry<String,List<String>> service :
                dependencies.entrySet()) {

            if(service.getValue().size() == 0){
                writer.printf(islandServiceDef, service.getKey());
            } else{
                StringBuilder dependentServices = new StringBuilder();
                count = 0;
                for (String dependentService:
                     service.getValue()) {
                    dependentServices.append(dependentService);
                    if (count < service.getValue().size() - 1){
                        dependentServices.append(" + ");
                        count ++;
                    }
                }
                writer.printf(dependencyDef, service.getKey(), dependentServices);
            }
            //servicesListAdded.append(service.getKey()).append(" + ");
        }
        writer.printf(closeFactDefinition);

        writer.close();
        model.close();
    }

    private void copyTemplate(File template, File generatedModel) throws IOException {
        FileChannel src = null;
        FileChannel dest = null;
        try{
            src = new FileInputStream(template).getChannel();
            dest = new FileOutputStream(generatedModel).getChannel();
            dest.transferFrom(src, 0, src.size());

        } finally {
            if (src != null){
                src.close();
            }
            if (dest != null){
                dest.close();
            }
        }
    }
}
