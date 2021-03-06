/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package dev.jchankchan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.kittinunf.fuel.*
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.json.responseJson
import com.google.gson.Gson
import dev.jchankchan.data.CreateDraft
import dev.jchankchan.data.Release

class App : CliktCommand() {
    val version: String by option(help = "The version of draft").default("")
    val path: String by option(help = "Your Releases path").default("")
    val assetname: String by option(help = "Your asset file name").default("")
    val draft by option("--draft").flag("--publish", default = true)
    val prerelease by option("--prerelease").flag("--release", default = false)
    val token: String by option(help = "Your token").default("")

    var isReady = false
    private set

    private val githubApiReleases: String
        get() {
            return "https://api.github.com/repos/$path/releases"
        }

    override fun run() {
        if (version.isEmpty() || path.isEmpty() || token.isEmpty()) {
            println("Your arguments may be invalid... Please check command-line arguments")
            println("version: $version")
            println("path: $path")
            println("assetname: $assetname")
            println("draft: $draft")
            println("prerelease: $prerelease")
            return
        } else {
            isReady = true;
        }
    }

    fun executeGitHubReleasesDrafter() {
        if (!isReady) {
            println("GitHubReleasesDrafter is not ready...")
            return
        }
        val releases = getListReleases(version)
        println(releases.toString())

        val releaseDraft: Release?
        if (releases.isNotEmpty()) {
            releaseDraft = releases.first()
            if (!draft) {
                changePublish(releaseDraft)
                return
            }
            releases.forEach {
                if (it.assets.isNotEmpty())
                deletePreviousAsset(it.assets.first().id)
            }
        } else {
            releaseDraft = createDraft()
        }

        releaseDraft?.let {
            uploadAsset(it)
        }
    }

    private fun getListReleases(tag: String): List<Release> {
        val (_, _, result) = githubApiReleases
                .httpGet().header(Headers.AUTHORIZATION, "token $token")
                .responseJson()
        val (data, error) = result
        if (error != null) {
            println(error.localizedMessage)
        }
        if (data == null) {
            return emptyList()
        }
        val gson = Gson()
        return data.array()
                .map { gson.fromJson(it.toString(), Release::class.java) }
                .filter { it.tag_name == tag }
    }

    private fun changePublish(release: Release) {
        print("changePublish")

        val url = "$githubApiReleases/" + release.id
        println("URL: $url")

        val createDraft = CreateDraft(
                tag_name = release.tag_name,
                target_commitish = release.target_commitish,
                name = release.name,
                body = release.body,
                draft = draft,
                prerelease = release.prerelease
        )

        val gson = Gson()
        val jsonString : String = gson.toJson(createDraft)

        val (_, _, result) = url.httpPatch()
                .header(Headers.AUTHORIZATION, "token $token")
                .jsonBody(jsonString)
                .responseJson()
        val (_, error) = result
        if (error != null) {
            println(error.localizedMessage)
        }
    }

    private fun deletePreviousAsset(id: Int) {
        println("deletePreviousAsset")

        val url = "$githubApiReleases/assets/$id"
        println("URL: $url")

        val (_, _, result) = url
                .httpDelete()
                .header(Headers.AUTHORIZATION, "token $token")
                .responseJson()
        val (_, error) = result
        if (error != null) {
            println(error.localizedMessage)
        }
    }

    private fun createDraft(): Release? {
        println("createDraft")

        val createDraft = CreateDraft(
                tag_name = version,
                target_commitish = "master",
                name = version,
                body = "Created by GitHub-Releases-Drafter",
                draft = draft,
                prerelease = prerelease
        )

        val gson = Gson()
        val jsonString : String = gson.toJson(createDraft)

        val (_, _, result) = githubApiReleases.httpPost()
                .header(Headers.AUTHORIZATION, "token $token")
                .jsonBody(jsonString)
                .responseObject(gsonDeserializerOf(Release::class.java))
        val (data, error) = result
        if (error != null) {
            println(error.localizedMessage)
            return null
        }
        return data
    }

    private fun uploadAsset(draft: Release) {
        if (assetname.isEmpty()) return

        println("uploadAsset")
        val url = draft.upload_url.replace("{?name,label}", "?name=$assetname")
        println("URL: $url")
        val (_, _, result) = url.httpUpload()
                .header(Headers.AUTHORIZATION, "token $token")
                .responseJson()
        val (_, error) = result
        if (error != null) {
            println(error.localizedMessage)
        }
    }
}

fun main(args: Array<String>) {
    val app = App()
    app.main(args)
    app.executeGitHubReleasesDrafter()
    //args.forEachIndexed { index, s -> println("args[${index}]: "+ s) }
}