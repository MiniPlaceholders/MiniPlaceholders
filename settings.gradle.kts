rootProject.name = "miniplaceholders-parent"


include("api", "paper", "velocity", "distribution")

project(":api").projectDir = file("api")
project(":paper").projectDir = file("paper")
project(":velocity").projectDir = file("velocity")
project(":distribution").projectDir = file("distribution")
