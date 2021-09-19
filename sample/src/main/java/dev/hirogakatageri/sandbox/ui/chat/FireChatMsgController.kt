package dev.hirogakatageri.sandbox.ui.chat

import com.airbnb.epoxy.EpoxyController
import dev.hirogakatageri.sandbox.data.chat.model.ChatMessageModel
import dev.hirogakatageri.sandbox.ui.chat.items.fireChat

class FireChatMsgController : EpoxyController() {

    var messages: List<ChatMessageModel> = emptyList()

    override fun buildModels() {
        messages.forEach { model ->
            fireChat {
                id(model.id)
                model(model)
            }
        }
    }

}