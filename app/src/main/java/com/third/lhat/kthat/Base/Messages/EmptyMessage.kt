package com.third.lhat.kthat.Base.Messages

import com.ktHat.Messages.Message

class EmptyMessage(override val json: String = "", override val end: String = "") : Message()