package ru.skillbranch.devintensive.extensions

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup

/**
 * Find the closest ancestor of the given type.
 */
inline fun <reified T> View.findParentOfType(): T? {
    var p = parent
    while (p != null && p !is T) p = p.parent
    return p as T?
}

fun ViewGroup.howFarDownIs(descendant: View): Int? {
    val bottom = Rect().also {
        descendant.getDrawingRect(it)
        offsetDescendantRectToMyCoords(descendant, it)
    }.bottom
    return (bottom - height - scrollY).takeIf { it > 0 }
}

fun ViewGroup.scrollDownTo(descendant: View) {
    howFarDownIs(descendant)?.let {
        scrollBy(0, it)
    }
}