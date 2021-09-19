package dev.hirogakatageri.sandbox.ui.chat.items

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import dev.hirogakatageri.sandbox.R
import dev.hirogakatageri.sandbox.data.chat.model.ChatMessageModel
import dev.hirogakatageri.sandbox.databinding.ItemChatBinding
import dev.hirogakatageri.sandbox.helpers.epoxy.ViewBindingEpoxyModelWithHolder

@EpoxyModelClass
abstract class FireChatEpoxyModel : ViewBindingEpoxyModelWithHolder<ItemChatBinding>() {

    @EpoxyAttribute
    lateinit var model: ChatMessageModel

    override fun ItemChatBinding.bind() {
        txtEmail.text = model.email
        txtMessage.text = model.message
    }

    override fun getDefaultLayout(): Int = R.layout.item_chat
}