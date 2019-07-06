package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {

    /**
     * @return строка, содержащая информацию об id сообщения,
     * имени получателя/отправителя, виде сообщения ("получил/отправил")
     * и типе сообщения ("сообщение"/"изображение")
     */
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1

        fun makeMessage(
            from: User, chat: Chat, date: Date = Date(), type: String = "text", payload: Any?,
            isIncoming: Boolean = false
        ) = when (type) {
                "image" -> ImageMessage(
                    "${++lastId}",
                    from,
                    chat,
                    date = date,
                    image = payload as String,
                    isIncoming = isIncoming
                )
                else -> TextMessage(
                    "${++lastId}",
                    from,
                    chat,
                    date = date,
                    text = payload as String,
                    isIncoming = isIncoming
                )
            }
    }
}