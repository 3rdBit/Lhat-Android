package com.third.lhat.dependency.kthat.base.messages

import com.ktHat.Messages.Message

data class EmptyMessage(
    override val json: String = "",
    //override val end: String = ""
) : Message()