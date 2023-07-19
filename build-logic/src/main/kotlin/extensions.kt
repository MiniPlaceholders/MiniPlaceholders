import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

val Project.capitalizeName: String
    get() = this.name.substring(17).capitalized()

fun Project.simpleName(): String = this.name.substring(17)