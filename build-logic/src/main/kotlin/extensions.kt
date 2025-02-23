import org.gradle.api.Project

val Project.capitalizeName: String
    get() = this.name.substring(17)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

fun Project.simpleName(): String = this.name.substring(17)