package com.kdy_soft.jetnewsclone.presentation.screen.article

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kdy_soft.jetnewsclone.R
import com.kdy_soft.jetnewsclone.model.MarkUp
import com.kdy_soft.jetnewsclone.model.Metadata
import com.kdy_soft.jetnewsclone.model.Paragraph
import com.kdy_soft.jetnewsclone.model.Post

private val defaultSpaceSize = 16.dp

@Composable
fun PostContent(
    post: Post,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    //detailScreen
    //LazyColumn
    LazyColumn(
        state = state,
        modifier = modifier,
        contentPadding = PaddingValues(defaultSpaceSize)
    ) {
        postContentItems(post)
    }
}

fun LazyListScope.postContentItems(post: Post) {
    item {
        //HeaderImage
        //Title
        //Subtitle
        PostHeaderImage(post)
        Spacer(modifier = Modifier.height(defaultSpaceSize))
        Text(text = post.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(8.dp))
        post.subtitle?.let { subtitle ->
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(defaultSpaceSize))
        }
    }
    //Metadata
    //Paragraphs
    item { PostMetadata(post.metadata, Modifier.padding(bottom = 24.dp)) }
    items(post.paragraphs) { paragraph -> ParagraphItem(paragraph) }
}

@Composable
private fun PostHeaderImage(post: Post) {
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)

    Image(
        painter = painterResource(id = post.imageId),
        contentDescription = null,
        modifier = imageModifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PostMetadata(
    metadata: Metadata,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.semantics(mergeDescendants = true) { }) {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = metadata.author.name,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = stringResource(
                    id = R.string.home_post_min_read,
                    formatArgs = arrayOf(
                        metadata.date,
                        metadata.readTimeMinutes
                    )
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ParagraphItem(paragraph: Paragraph) {
    //여기가 디테일 페이지의 핵심!!
    val (textStyle, paragraphStyle, trailingPadding) = paragraph.type.getTextAndParagraphTypeStyle()
    val annotatedString = paragraphToAnnotatedString(
        paragraph,
        MaterialTheme.typography,
        MaterialTheme.colorScheme.codeBlockBackground
    )

    Box(modifier = Modifier.padding(trailingPadding)) {
        when (paragraph.type) {
            Paragraph.ParagraphType.Header -> {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = annotatedString,
                    style = textStyle.merge(paragraphStyle)
                )
            }
            Paragraph.ParagraphType.CodeBlock -> {
                CodeBlockParagraph(
                    text = annotatedString,
                    textStyle = textStyle,
                    paragraphStyle = paragraphStyle
                )
            }
            Paragraph.ParagraphType.Bullet -> {
                BulletParagraph(
                    text = annotatedString,
                    textStyle = textStyle,
                    paragraphStyle = paragraphStyle
                )
            }
            else -> {
                Text(
                    text = annotatedString,
                    style = textStyle,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun BulletParagraph(
    text: AnnotatedString,
    textStyle: TextStyle,
    paragraphStyle: ParagraphStyle
) {
    Row {
        with(LocalDensity.current) {
            Box(modifier = Modifier
                .size(8.sp.toDp(), 8.sp.toDp())
                .alignBy {
                    //1sp below
                    9.sp.roundToPx()
                }
                .background(LocalContentColor.current, CircleShape)
            ) {/* no content */ }
        }

        Text(
            modifier = Modifier
                .weight(1f)
                .alignBy(FirstBaseline),
            text = text,
            style = textStyle.merge(paragraphStyle)
        )
    }
}

@Composable
fun CodeBlockParagraph(
    text: AnnotatedString,
    textStyle: TextStyle,
    paragraphStyle: ParagraphStyle
) {
    Surface(
        color = MaterialTheme.colorScheme.codeBlockBackground,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text, style = textStyle.merge(paragraphStyle))
    }
}

private fun paragraphToAnnotatedString(
    paragraph: Paragraph,
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString {
    val styles: List<AnnotatedString.Range<SpanStyle>> = paragraph.markUps.map {
        it.toAnnotatedString(typography = typography, codeBlockBackground = codeBlockBackground)
    }
    return AnnotatedString(text = paragraph.text, spanStyles = styles)
}

private fun MarkUp.toAnnotatedString(
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString.Range<SpanStyle> {
    return when (this.type) {
        MarkUp.MarkupType.Link -> {
            AnnotatedString.Range(
                typography.bodyLarge.copy(fontStyle = FontStyle.Italic).toSpanStyle(),
                start,
                end
            )
        }
        MarkUp.MarkupType.Code -> {
            AnnotatedString.Range(
                typography.bodyLarge.copy(
                    background = codeBlockBackground,
                    fontFamily = FontFamily.Monospace
                ).toSpanStyle(),
                start,
                end
            )
        }
        MarkUp.MarkupType.Italic -> {
            AnnotatedString.Range(
                typography.bodyLarge.copy(
                    fontStyle = FontStyle.Italic
                ).toSpanStyle(),
                start,
                end
            )
        }
        MarkUp.MarkupType.Bold -> {
            AnnotatedString.Range(
                typography.bodyLarge.copy(fontWeight = FontWeight.Normal).toSpanStyle(),
                start,
                end
            )
        }
    }
}

private data class ParagraphStyling(
    val textStyle: TextStyle,
    val paragraphStyle: ParagraphStyle,
    val trailingPadding: Dp
)

@Composable
private fun Paragraph.ParagraphType.getTextAndParagraphTypeStyle(): ParagraphStyling {
    val typography = MaterialTheme.typography
    var textStyle = typography.bodyLarge
    var paragraphStyle = ParagraphStyle()
    var trailingPadding: Dp = 24.dp

    when (this) {
        Paragraph.ParagraphType.Title -> {
            textStyle = typography.headlineLarge
        }
        Paragraph.ParagraphType.Caption -> {
            textStyle = typography.labelMedium
        }
        Paragraph.ParagraphType.Header -> {}
        Paragraph.ParagraphType.Subhead -> {
            textStyle = typography.headlineSmall
            trailingPadding = 16.dp
        }
        Paragraph.ParagraphType.Text -> {
            textStyle = typography.bodyLarge.copy(lineHeight = 28.sp)
        }
        Paragraph.ParagraphType.CodeBlock -> textStyle = typography.bodyLarge.copy(
            fontFamily = FontFamily.Monospace
        )
        Paragraph.ParagraphType.Quote -> textStyle = typography.bodyLarge
        Paragraph.ParagraphType.Bullet -> {
            paragraphStyle = ParagraphStyle(textIndent = TextIndent(firstLine = 8.sp))
        }
    }

    return ParagraphStyling(textStyle, paragraphStyle, trailingPadding)
}

private val ColorScheme.codeBlockBackground: Color
    get() = onSurface.copy(alpha = 0.15f)