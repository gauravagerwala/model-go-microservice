# model-go-microservice
This repository contains source code to generate an Alloy model from go microservices. The generated model can be used to check interesting properties about the system including checking for circular references

# How to run the tool
Clone the repository

The built package is available target/Microservices-Modelling-1.0.jar

From the root directory run the command:

java -cp .\target\Microservices-Modelling-1.0.jar org.example.Main "sample-system"

sample-system is the path to the microservices application.

The generated model called generated_model.als will be written to the root directory of the project.

Open this generated model in the Alloy tool and check the "hasCircularDependency" assertion.
