package com.itgates.co.pulpo.ultra.uiComponents

/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.lerp


import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.*
//import com.itgates.co.pulpo.ultra.uiComponents.Strings.Companion.DefaultErrorMessage
import kotlin.math.max
import kotlin.math.roundToInt

//@Immutable
//@JvmInline
//internal value class Strings private constructor(@Suppress("unused") private val value: Int) {
//    companion object {
//        val NavigationMenu = Strings(0)
//        val CloseDrawer = Strings(1)
//        val CloseSheet = Strings(2)
//        val DefaultErrorMessage = Strings(3)
//        val ExposedDropdownMenu = Strings(4)
//        val SliderRangeStart = Strings(5)
//        val SliderRangeEnd = Strings(6)
//    }
//}
//
//@Composable
//internal expect fun getString(string: Strings): String

internal enum class TextFieldType {
    Filled, Outlined
}

/**
 * Implementation of the [TextField] and [OutlinedTextField]
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun CommonDecorationBox(
    type: TextFieldType,
    value: String,
    innerTextField: @Composable () -> Unit,
    visualTransformation: VisualTransformation,
    label: @Composable (() -> Unit)?,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    isError: Boolean = false,
    interactionSource: InteractionSource,
    contentPadding: PaddingValues,
    colors: TextFieldColors,
    border: @Composable (() -> Unit)? = null
) {
    val transformedText = remember(value, visualTransformation) {
        visualTransformation.filter(AnnotatedString(value))
    }.text.text

//    val isFocused = interactionSource.collectIsFocusedAsState().value
    val isFocused = true
    val inputState = when {
        isFocused -> InputPhase.Focused
        transformedText.isEmpty() -> InputPhase.UnfocusedEmpty
        else -> InputPhase.UnfocusedNotEmpty
    }

    val labelColor: @Composable (InputPhase) -> Color = {
        colors.labelColor(
            enabled,
            // if label is used as a placeholder (aka not as a small header
            // at the top), we don't use an error color
            if (it == InputPhase.UnfocusedEmpty) false else isError,
            interactionSource
        ).value
    }

    val typography = MaterialTheme.typography
    val subtitle1 = typography.subtitle1
    val caption = typography.caption
    val shouldOverrideTextStyleColor =
        (subtitle1.color == Color.Unspecified && caption.color != Color.Unspecified) ||
                (subtitle1.color != Color.Unspecified && caption.color == Color.Unspecified)

    TextFieldTransitionScope.Transition(
        inputState = inputState,
        focusedTextStyleColor = with(MaterialTheme.typography.caption.color) {
            if (shouldOverrideTextStyleColor) this.takeOrElse { labelColor(inputState) } else this
        },
        unfocusedTextStyleColor = with(MaterialTheme.typography.subtitle1.color) {
            if (shouldOverrideTextStyleColor) this.takeOrElse { labelColor(inputState) } else this
        },
        contentColor = labelColor,
        showLabel = label != null
    ) { labelProgress, labelTextStyleColor, labelContentColor, placeholderAlphaProgress ->

        val decoratedLabel: @Composable (() -> Unit)? = label?.let {
            @Composable {
                val labelTextStyle = lerp(
                    MaterialTheme.typography.subtitle1,
                    MaterialTheme.typography.caption,
                    labelProgress
                ).let {
                    if (shouldOverrideTextStyleColor) it.copy(color = labelTextStyleColor) else it
                }
                Decoration(labelContentColor, labelTextStyle, null, it)
            }
        }

        val decoratedPlaceholder: @Composable ((Modifier) -> Unit)? =
            if (placeholder != null && transformedText.isEmpty()) {
                @Composable { modifier ->
                    Box(modifier.alpha(placeholderAlphaProgress)) {
                        Decoration(
                            contentColor = colors.placeholderColor(enabled).value,
                            typography = MaterialTheme.typography.subtitle1,
                            content = placeholder
                        )
                    }
                }
            } else null

        // Developers need to handle invalid input manually. But since we don't provide error
        // message slot API, we can set the default error message in case developers forget about
        // it.
//        val defaultErrorMessage = getString(DefaultErrorMessage)
        val defaultErrorMessage = "getString(DefaultErrorMessage)"
        val decorationBoxModifier = Modifier.semantics { if (isError) error(defaultErrorMessage) }

        val leadingIconColor = if (colors is TextFieldColorsWithIcons) {
            colors.leadingIconColor(enabled, isError, interactionSource).value
        } else {
            colors.leadingIconColor(enabled, isError).value
        }
        val decoratedLeading: @Composable (() -> Unit)? = leadingIcon?.let {
            @Composable {
                Decoration(contentColor = leadingIconColor, content = it)
            }
        }

        val trailingIconColor = if (colors is TextFieldColorsWithIcons) {
            colors.trailingIconColor(enabled, isError, interactionSource).value
        } else {
            colors.trailingIconColor(enabled, isError).value
        }
        val decoratedTrailing: @Composable (() -> Unit)? = trailingIcon?.let {
            @Composable {
                Decoration(contentColor = trailingIconColor, content = it)
            }
        }

        when (type) {
            TextFieldType.Filled -> {
                TextFieldLayout(
                    modifier = decorationBoxModifier,
                    textField = innerTextField,
                    placeholder = decoratedPlaceholder,
                    label = decoratedLabel,
                    leading = decoratedLeading,
                    trailing = decoratedTrailing,
                    singleLine = singleLine,
                    animationProgress = labelProgress,
                    paddingValues = contentPadding
                )
            }
            TextFieldType.Outlined -> {
                // Outlined cutout
                val labelSize = remember { mutableStateOf(Size.Zero) }
                val drawBorder: @Composable () -> Unit = {
                    Box(
                        Modifier.layoutId(BorderId).outlineCutout(labelSize.value, contentPadding),
                        propagateMinConstraints = true
                    ) {
                        border?.invoke()
                    }
                }

                OutlinedTextFieldLayout(
                    modifier = decorationBoxModifier,
                    textField = innerTextField,
                    placeholder = decoratedPlaceholder,
                    label = decoratedLabel,
                    leading = decoratedLeading,
                    trailing = decoratedTrailing,
                    singleLine = singleLine,
                    onLabelMeasured = {
                        val labelWidth = it.width * labelProgress
                        val labelHeight = it.height * labelProgress
                        if (labelSize.value.width != labelWidth ||
                            labelSize.value.height != labelHeight
                        ) {
                            labelSize.value = Size(labelWidth, labelHeight)
                        }
                    },
                    animationProgress = labelProgress,
                    border = drawBorder,
                    paddingValues = contentPadding
                )
            }
        }
    }
}

/**
 * Set content color, typography and emphasis for [content] composable
 */
@Composable
@ComposableOpenTarget(index = 0)
internal fun Decoration(
    contentColor: Color,
    typography: TextStyle? = null,
    contentAlpha: Float? = null,
    content: @Composable @ComposableOpenTarget(index = 0) () -> Unit
) {
    val colorAndEmphasis: @Composable () -> Unit = @Composable {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            if (contentAlpha != null) {
                CompositionLocalProvider(
                    LocalContentAlpha provides contentAlpha,
                    content = content
                )
            } else {
                CompositionLocalProvider(
                    LocalContentAlpha provides contentColor.alpha,
                    content = content
                )
            }
        }
    }
    if (typography != null) ProvideTextStyle(typography, colorAndEmphasis) else colorAndEmphasis()
}

internal fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
internal fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0

private object TextFieldTransitionScope {
    @Composable
    fun Transition(
        inputState: InputPhase,
        focusedTextStyleColor: Color,
        unfocusedTextStyleColor: Color,
        contentColor: @Composable (InputPhase) -> Color,
        showLabel: Boolean,
        content: @Composable (
            labelProgress: Float,
            labelTextStyleColor: Color,
            labelContentColor: Color,
            placeholderOpacity: Float
        ) -> Unit
    ) {
        // Transitions from/to InputPhase.Focused are the most critical in the transition below.
        // UnfocusedEmpty <-> UnfocusedNotEmpty are needed when a single state is used to control
        // multiple text fields.
        val transition = updateTransition(inputState, label = "TextFieldInputState")

        val labelProgress by transition.animateFloat(
            label = "LabelProgress",
            transitionSpec = { tween(durationMillis = AnimationDuration) }
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> 0f
                InputPhase.UnfocusedNotEmpty -> 1f
            }
        }

        val placeholderOpacity by transition.animateFloat(
            label = "PlaceholderOpacity",
            transitionSpec = {
                if (InputPhase.Focused isTransitioningTo InputPhase.UnfocusedEmpty) {
                    tween(
                        durationMillis = PlaceholderAnimationDelayOrDuration,
                        easing = LinearEasing
                    )
                } else if (InputPhase.UnfocusedEmpty isTransitioningTo InputPhase.Focused ||
                    InputPhase.UnfocusedNotEmpty isTransitioningTo InputPhase.UnfocusedEmpty
                ) {
                    tween(
                        durationMillis = PlaceholderAnimationDuration,
                        delayMillis = PlaceholderAnimationDelayOrDuration,
                        easing = LinearEasing
                    )
                } else {
                    spring()
                }
            }
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> if (showLabel) 0f else 1f
                InputPhase.UnfocusedNotEmpty -> 0f
            }
        }

        val labelTextStyleColor by transition.animateColor(
            transitionSpec = { tween(durationMillis = AnimationDuration) },
            label = "LabelTextStyleColor"
        ) {
            when (it) {
                InputPhase.Focused -> focusedTextStyleColor
                else -> unfocusedTextStyleColor
            }
        }

        val labelContentColor by transition.animateColor(
            transitionSpec = { tween(durationMillis = AnimationDuration) },
            label = "LabelContentColor",
            targetValueByState = contentColor
        )

        content(
            labelProgress,
            labelTextStyleColor,
            labelContentColor,
            placeholderOpacity
        )
    }
}

/**
 * An internal state used to animate a label and an indicator.
 */
private enum class InputPhase {
    // Text field is focused
    Focused,

    // Text field is not focused and input text is empty
    UnfocusedEmpty,

    // Text field is not focused but input text is not empty
    UnfocusedNotEmpty
}

@Composable
internal fun OutlinedTextFieldLayout(
    modifier: Modifier,
    textField: @Composable () -> Unit,
    placeholder: @Composable ((Modifier) -> Unit)?,
    label: @Composable (() -> Unit)?,
    leading: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    singleLine: Boolean,
    animationProgress: Float,
    onLabelMeasured: (Size) -> Unit,
    border: @Composable () -> Unit,
    paddingValues: PaddingValues
) {
    val measurePolicy = remember(onLabelMeasured, singleLine, animationProgress, paddingValues) {
        OutlinedTextFieldMeasurePolicy(
            onLabelMeasured,
            singleLine,
            animationProgress,
            paddingValues
        )
    }
    val layoutDirection = LocalLayoutDirection.current
    Layout(
        modifier = modifier,
        content = {
            // We use additional box here to place an outlined cutout border as a sibling after the
            // rest of UI. This allows us to use Modifier.border to draw an outline on top of the
            // text field. We can't use the border modifier directly on the IconsWithTextFieldLayout
            // as we also need to do the clipping (to form the cutout) which should not affect
            // the rest of text field UI
            border()

            if (leading != null) {
                Box(
                    modifier = Modifier.layoutId(LeadingId).then(IconDefaultSizeModifier),
                    contentAlignment = Alignment.Center
                ) {
                    leading()
                }
            }
            if (trailing != null) {
                Box(
                    modifier = Modifier.layoutId(TrailingId).then(IconDefaultSizeModifier),
                    contentAlignment = Alignment.Center
                ) {
                    trailing()
                }
            }

            val startTextFieldPadding = paddingValues.calculateStartPadding(layoutDirection)
            val endTextFieldPadding = paddingValues.calculateEndPadding(layoutDirection)
            val padding = Modifier.padding(
                start = if (leading != null) {
                    (startTextFieldPadding - HorizontalIconPadding).coerceAtLeast(
                        0.dp
                    )
                } else {
                    startTextFieldPadding
                },
                end = if (trailing != null) {
                    (endTextFieldPadding - HorizontalIconPadding).coerceAtLeast(0.dp)
                } else {
                    endTextFieldPadding
                }
            )
            if (placeholder != null) {
                placeholder(Modifier.layoutId(PlaceholderId).then(padding))
            }

            Box(
                modifier = Modifier.layoutId(TextFieldId).then(padding),
                propagateMinConstraints = true
            ) {
                textField()
            }

            if (label != null) {
                Box(modifier = Modifier.layoutId(LabelId)) { label() }
            }
        },
        measurePolicy = measurePolicy
    )
}

private class OutlinedTextFieldMeasurePolicy(
    private val onLabelMeasured: (Size) -> Unit,
    private val singleLine: Boolean,
    private val animationProgress: Float,
    private val paddingValues: PaddingValues
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        // used to calculate the constraints for measuring elements that will be placed in a row
        var occupiedSpaceHorizontally = 0
        val bottomPadding = paddingValues.calculateBottomPadding().roundToPx()

        // measure leading icon
        val relaxedConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val leadingPlaceable = measurables.find {
            it.layoutId == LeadingId
        }?.measure(relaxedConstraints)
        occupiedSpaceHorizontally += widthOrZero(
            leadingPlaceable
        )

        // measure trailing icon
        val trailingPlaceable = measurables.find { it.layoutId == TrailingId }
            ?.measure(relaxedConstraints.offset(horizontal = -occupiedSpaceHorizontally))
        occupiedSpaceHorizontally += widthOrZero(
            trailingPlaceable
        )

        // measure label
        val isLabelInMiddleSection = animationProgress < 1f
        val labelHorizontalPaddingOffset =
            paddingValues.calculateLeftPadding(layoutDirection).roundToPx() +
                    paddingValues.calculateRightPadding(layoutDirection).roundToPx()
        val labelConstraints = relaxedConstraints.offset(
            horizontal = if (isLabelInMiddleSection) {
                -occupiedSpaceHorizontally - labelHorizontalPaddingOffset
            } else {
                -labelHorizontalPaddingOffset
            },
            vertical = -bottomPadding
        )
        val labelPlaceable =
            measurables.find { it.layoutId == LabelId }?.measure(labelConstraints)
        labelPlaceable?.let {
            onLabelMeasured(Size(it.width.toFloat(), it.height.toFloat()))
        }

        // measure text field
        // on top we offset either by default padding or by label's half height if its too big
        // minHeight must not be set to 0 due to how foundation TextField treats zero minHeight
        val topPadding = max(
            heightOrZero(labelPlaceable) / 2,
            paddingValues.calculateTopPadding().roundToPx()
        )
        val textConstraints = constraints.offset(
            horizontal = -occupiedSpaceHorizontally,
            vertical = -bottomPadding - topPadding
        ).copy(minHeight = 0)
        val textFieldPlaceable =
            measurables.first { it.layoutId == TextFieldId }.measure(textConstraints)

        // measure placeholder
        val placeholderConstraints = textConstraints.copy(minWidth = 0)
        val placeholderPlaceable =
            measurables.find { it.layoutId == PlaceholderId }?.measure(placeholderConstraints)

        val width =
            calculateWidth(
                leadingPlaceableWidth = widthOrZero(leadingPlaceable),
                trailingPlaceableWidth = widthOrZero(trailingPlaceable),
                textFieldPlaceableWidth = textFieldPlaceable.width,
                labelPlaceableWidth = widthOrZero(labelPlaceable),
                placeholderPlaceableWidth = widthOrZero(
                    placeholderPlaceable
                ),
                isLabelInMiddleSection = isLabelInMiddleSection,
                constraints = constraints,
                density = density,
                paddingValues = paddingValues,
            )
        val height =
            calculateHeight(
                heightOrZero(leadingPlaceable),
                heightOrZero(trailingPlaceable),
                textFieldPlaceable.height,
                heightOrZero(labelPlaceable),
                heightOrZero(placeholderPlaceable),
                constraints,
                density,
                paddingValues
            )

        val borderPlaceable = measurables.first { it.layoutId == BorderId }.measure(
            Constraints(
                minWidth = if (width != Constraints.Infinity) width else 0,
                maxWidth = width,
                minHeight = if (height != Constraints.Infinity) height else 0,
                maxHeight = height
            )
        )
        return layout(width, height) {
            place(
                height,
                width,
                leadingPlaceable,
                trailingPlaceable,
                textFieldPlaceable,
                labelPlaceable,
                placeholderPlaceable,
                borderPlaceable,
                animationProgress,
                singleLine,
                density,
                layoutDirection,
                paddingValues
            )
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
            intrinsicMeasurable.maxIntrinsicHeight(w)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
            intrinsicMeasurable.minIntrinsicHeight(w)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
            intrinsicMeasurable.maxIntrinsicWidth(h)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
            intrinsicMeasurable.minIntrinsicWidth(h)
        }
    }

    private fun IntrinsicMeasureScope.intrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int
    ): Int {
        val textFieldWidth =
            intrinsicMeasurer(measurables.first { it.layoutId == TextFieldId }, height)
        val labelWidth = measurables.find { it.layoutId == LabelId }?.let {
            intrinsicMeasurer(it, height)
        } ?: 0
        val trailingWidth = measurables.find { it.layoutId == TrailingId }?.let {
            intrinsicMeasurer(it, height)
        } ?: 0
        val leadingWidth = measurables.find { it.layoutId == LeadingId }?.let {
            intrinsicMeasurer(it, height)
        } ?: 0
        val placeholderWidth = measurables.find { it.layoutId == PlaceholderId }?.let {
            intrinsicMeasurer(it, height)
        } ?: 0
        return calculateWidth(
            leadingPlaceableWidth = leadingWidth,
            trailingPlaceableWidth = trailingWidth,
            textFieldPlaceableWidth = textFieldWidth,
            labelPlaceableWidth = labelWidth,
            placeholderPlaceableWidth = placeholderWidth,
            isLabelInMiddleSection = animationProgress < 1f,
            constraints = ZeroConstraints,
            density = density,
            paddingValues = paddingValues,
        )
    }

    private fun IntrinsicMeasureScope.intrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int
    ): Int {
        val textFieldHeight =
            intrinsicMeasurer(measurables.first { it.layoutId == TextFieldId }, width)
        val labelHeight = measurables.find { it.layoutId == LabelId }?.let {
            intrinsicMeasurer(it, width)
        } ?: 0
        val trailingHeight = measurables.find { it.layoutId == TrailingId }?.let {
            intrinsicMeasurer(it, width)
        } ?: 0
        val leadingHeight = measurables.find { it.layoutId == LeadingId }?.let {
            intrinsicMeasurer(it, width)
        } ?: 0
        val placeholderHeight = measurables.find { it.layoutId == PlaceholderId }?.let {
            intrinsicMeasurer(it, width)
        } ?: 0
        return calculateHeight(
            leadingPlaceableHeight = leadingHeight,
            trailingPlaceableHeight = trailingHeight,
            textFieldPlaceableHeight = textFieldHeight,
            labelPlaceableHeight = labelHeight,
            placeholderPlaceableHeight = placeholderHeight,
            constraints = ZeroConstraints,
            density = density,
            paddingValues = paddingValues
        )
    }
}

private fun calculateWidth(
    leadingPlaceableWidth: Int,
    trailingPlaceableWidth: Int,
    textFieldPlaceableWidth: Int,
    labelPlaceableWidth: Int,
    placeholderPlaceableWidth: Int,
    isLabelInMiddleSection: Boolean,
    constraints: Constraints,
    density: Float,
    paddingValues: PaddingValues,
): Int {
    val middleSection = maxOf(
        textFieldPlaceableWidth,
        if (isLabelInMiddleSection) labelPlaceableWidth else 0,
        placeholderPlaceableWidth
    )
    val wrappedWidth =
        leadingPlaceableWidth + middleSection + trailingPlaceableWidth
    val focusedLabelWidth =
        if (!isLabelInMiddleSection) {
            // Actual LayoutDirection doesn't matter; we only need the sum
            val labelHorizontalPadding = (paddingValues.calculateLeftPadding(LayoutDirection.Ltr) +
                    paddingValues.calculateRightPadding(LayoutDirection.Ltr)).value * density
            labelPlaceableWidth + labelHorizontalPadding.roundToInt()
        } else {
            0
        }
    return maxOf(wrappedWidth, focusedLabelWidth, constraints.minWidth)
}

private fun calculateHeight(
    leadingPlaceableHeight: Int,
    trailingPlaceableHeight: Int,
    textFieldPlaceableHeight: Int,
    labelPlaceableHeight: Int,
    placeholderPlaceableHeight: Int,
    constraints: Constraints,
    density: Float,
    paddingValues: PaddingValues
): Int {
    // middle section is defined as a height of the text field or placeholder ( whichever is
    // taller) plus 16.dp or half height of the label if it is taller, given that the label
    // is vertically centered to the top edge of the resulting text field's container
    val inputFieldHeight = max(
        textFieldPlaceableHeight,
        placeholderPlaceableHeight
    )
    val topPadding = paddingValues.calculateTopPadding().value * density
    val bottomPadding = paddingValues.calculateBottomPadding().value * density
    val middleSectionHeight = inputFieldHeight + bottomPadding + max(
        topPadding,
        labelPlaceableHeight / 2f
    )
    return max(
        constraints.minHeight,
        maxOf(
            leadingPlaceableHeight,
            trailingPlaceableHeight,
            middleSectionHeight.roundToInt()
        )
    )
}

private fun Placeable.PlacementScope.place(
    height: Int,
    width: Int,
    leadingPlaceable: Placeable?,
    trailingPlaceable: Placeable?,
    textFieldPlaceable: Placeable,
    labelPlaceable: Placeable?,
    placeholderPlaceable: Placeable?,
    borderPlaceable: Placeable,
    animationProgress: Float,
    singleLine: Boolean,
    density: Float,
    layoutDirection: LayoutDirection,
    paddingValues: PaddingValues
) {
    val topPadding = (paddingValues.calculateTopPadding().value * density).roundToInt()
    val startPadding =
        (paddingValues.calculateStartPadding(layoutDirection).value * density).roundToInt()

    val iconPadding = HorizontalIconPadding.value * density

    // placed center vertically and to the start edge horizontally
    leadingPlaceable?.placeRelative(
        0,
        Alignment.CenterVertically.align(leadingPlaceable.height, height)
    )

    // placed center vertically and to the end edge horizontally
    trailingPlaceable?.placeRelative(
        width - trailingPlaceable.width,
        Alignment.CenterVertically.align(trailingPlaceable.height, height)
    )

    // label position is animated
    // in single line text field label is centered vertically before animation starts
    var labelPositionY: Int? = null
    labelPlaceable?.let {
        val startPositionY = if (singleLine) {
            Alignment.CenterVertically.align(it.height, height)
        } else {
            topPadding
        }
        val positionY = lerp(startPositionY, -(it.height / 2), animationProgress)
        labelPositionY = positionY
        val positionX = (
                if (leadingPlaceable == null) {
                    0f
                } else {
                    (widthOrZero(leadingPlaceable) - iconPadding) * (1 - animationProgress)
                }
                ).roundToInt() + startPadding
        it.placeRelative(positionX, positionY)
    }

    // placed center vertically and after the leading icon horizontally if single line text field
    // placed to the top with padding for multi line text field
    val textVerticalPosition = max(
        if (singleLine) {
            Alignment.CenterVertically.align(textFieldPlaceable.height, height)
        } else {
            topPadding
        },
        heightOrZero(labelPlaceable) / 2
    )
    textFieldPlaceable.placeRelative(widthOrZero(leadingPlaceable), textVerticalPosition)

    // placed similar to the input text above
    placeholderPlaceable?.let {
        var placeholderVerticalPosition = max(
            if (singleLine) {
                Alignment.CenterVertically.align(it.height, height)
            } else {
                topPadding
            },
            heightOrZero(labelPlaceable) / 2
        )
        // Ensure placeholder's bounding box does not cover label in unfocused state
        // (workaround for b/261061240)
        labelPositionY?.let { labelPositionY ->
            if (placeholderVerticalPosition <= labelPositionY) {
                placeholderVerticalPosition = labelPositionY + 1
            }
        }
        it.placeRelative(widthOrZero(leadingPlaceable), placeholderVerticalPosition)
    }

    // place border
    borderPlaceable.place(IntOffset.Zero)
}

fun lerp(start: Int, end: Int, t: Float): Int {
    // Ensure t is within the range [0, 1]
    val clampedT = t.coerceIn(0f, 1f)

    // Perform linear interpolation
    return (start + (end - start) * clampedT).toInt()
}

internal fun Modifier.outlineCutout(labelSize: Size, paddingValues: PaddingValues) =
    this.drawWithContent {
        val labelWidth = labelSize.width
        if (labelWidth > 0f) {
            val innerPadding = OutlinedTextFieldInnerPadding.toPx()
            val leftLtr = paddingValues.calculateLeftPadding(layoutDirection).toPx() - innerPadding
            val rightLtr = leftLtr + labelWidth + 2 * innerPadding
            val left = when (layoutDirection) {
                LayoutDirection.Rtl -> size.width - rightLtr
                else -> leftLtr.coerceAtLeast(0f)
            }
            val right = when (layoutDirection) {
                LayoutDirection.Rtl -> size.width - leftLtr.coerceAtLeast(0f)
                else -> rightLtr
            }
            val labelHeight = labelSize.height
            // using label height as a cutout area to make sure that no hairline artifacts are
            // left when we clip the border
            clipRect(left, -labelHeight / 2, right, labelHeight / 2, ClipOp.Difference) {
                this@drawWithContent.drawContent()
            }
        } else {
            this@drawWithContent.drawContent()
        }
    }

internal val IntrinsicMeasurable.layoutId: Any?
    get() = (parentData as? LayoutIdParentData)?.layoutId

internal const val TextFieldId = "TextField"
internal const val PlaceholderId = "Hint"
internal const val LabelId = "Label"
internal const val LeadingId = "Leading"
internal const val TrailingId = "Trailing"
internal val ZeroConstraints = Constraints(0, 0, 0, 0)

internal const val AnimationDuration = 150
private const val PlaceholderAnimationDuration = 83
private const val PlaceholderAnimationDelayOrDuration = 67

internal val TextFieldPadding = 16.dp
internal val HorizontalIconPadding = 12.dp

internal val IconDefaultSizeModifier = Modifier.defaultMinSize(48.dp, 48.dp)
internal const val BorderId = "border"
private val OutlinedTextFieldInnerPadding = 4.dp