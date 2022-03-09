rootProject.name = "miniplaceholders"


include("api")
include("common")
include("paper")
include("velocity")


project(":api").projectDir = file("api")
project(":common").projectDir = file("common")
project(":velocity").projectDir = file("velocity")
project(":paper").projectDir = file("paper")
