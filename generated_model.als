one sig Application{
	services: set Service
}

abstract sig Service {
	dependsOn: set Service
}

assert hasCircularDependency{
	all s: Service | s not in s.^dependsOn
}

check hasCircularDependency

one sig  serviceC extends Service{ }
one sig  serviceD extends Service{ }
one sig  serviceA extends Service{ }
one sig  serviceB extends Service{ }
fact { 
	Application.services =  serviceC +  serviceD +  serviceA +  serviceB 
	no  serviceC.dependsOn 
	 serviceD.dependsOn =     serviceA +     serviceB
	 serviceA.dependsOn =     serviceC +     serviceB
	 serviceB.dependsOn =     serviceC
}
