/**
 * This extension should return the substring after the first '_' in a string.
 * This is useful to compute short names of reddit objects from their full names which
 * is a combination of the type followed by '_' and then the name
 */
fun String.asShortName(): String {
    return this.substring(indexOfFirst { it == '_' } + 1, length)
}


/**
 * This extension cleans up a user inputted sub reddit name
 */
fun String.toSubRedditName(): String {
    return run {
        if (startsWith("r/")) substring(indexOf("r/") + 2, length) else this
    }
}
