import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

val Project.capitalizeName: String
    get() = this.name.substring(17).capitalized()

val Project.simpleName: String
    get() = this.name.substring(17)