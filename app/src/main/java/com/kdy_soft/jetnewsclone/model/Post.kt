package com.kdy_soft.jetnewsclone.model

import androidx.annotation.DrawableRes

data class Post(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val metadata: Metadata,
    val url: String,
    val publication: Publication? = null,
    val paragraphs: List<Paragraph> = emptyList(),
    @DrawableRes val imageId: Int,
    @DrawableRes val imageThumbId: Int
)

data class Publication(
    val name: String,
    val logoUrl: String
)

data class Metadata(
    val author: PostAuthor,
    val date: String,
    val readTimeMinutes: Int
)

data class PostAuthor(
    val name: String,
    val url: String? = null
)

data class Paragraph(
    val type: ParagraphType,
    val text: String,
    val markUps: List<MarkUp> = emptyList()
) {
    enum class ParagraphType {
        Title,
        Caption,
        Header,
        Subhead,
        Text,
        CodeBlock,
        Quote,
        Bullet
    }
}

data class MarkUp(
    val type: MarkupType,
    val start: Int,
    val end: Int,
    val href: String? = null
) {
    enum class MarkupType {
        Link,
        Code,
        Italic,
        Bold
    }

}


