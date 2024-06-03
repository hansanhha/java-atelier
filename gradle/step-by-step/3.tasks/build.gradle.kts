import org.apache.tools.ant.filters.ReplaceTokens

tasks.register<Copy>("copyOSFile") {
    from("your-os.txt")
    into(layout.buildDirectory)
    val os = System.getProperty("os.name");
    filter<ReplaceTokens>("tokens" to mapOf("MESSAGE" to os))
}