package dev.jchankchan.data

// https://developer.github.com/v3/repos/releases/#create-a-release
data class CreateDraft(
        var tag_name: String = "",
        var target_commitish: String = "",
        var name: String = "",
        var body: String = "",
        var draft: Boolean = true,
        var prerelease: Boolean = false
)