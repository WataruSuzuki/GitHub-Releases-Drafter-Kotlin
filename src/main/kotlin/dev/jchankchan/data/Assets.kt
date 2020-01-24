package dev.jchankchan.data

// https://developer.github.com/v3/repos/releases/#get-a-single-release-asset
data class Assets(
        var url: String = "",
        var browser_download_url: String = "",
        var id: Int = 0,
        var node_id: String = "",
        var name: String = "",
        var label: String = "",
        var state: String = "",
        var content_type: String = "",
        var size: Int = 0
)
