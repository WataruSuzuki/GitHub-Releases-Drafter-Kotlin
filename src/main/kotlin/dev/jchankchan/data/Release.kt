package dev.jchankchan.data

// https://developer.github.com/v3/repos/releases/#get-a-single-release
data class Release(
        var url: String = "",
        var assets_url: String = "",
        var upload_url: String = "",
        var id: Int = 0,
        var node_id: String = "",
        var tag_name: String = "",
        var target_commitish: String = "",
        var name: String = "",
        var body: String = "",
        var draft: Boolean = true,
        var prerelease: Boolean = false,
        var assets: List<Assets> = listOf()
)