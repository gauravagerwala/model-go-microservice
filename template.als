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

