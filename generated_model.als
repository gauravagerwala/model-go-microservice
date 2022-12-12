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

one sig serviceB extends Service{ }
one sig serviceA extends Service{ }
one sig serviceC extends Service{ }
fact { 
	Application.services = serviceB + serviceA + serviceC 
	serviceB.dependsOn = serviceC
	serviceA.dependsOn = serviceB + serviceC
	no serviceC.dependsOn 
}
