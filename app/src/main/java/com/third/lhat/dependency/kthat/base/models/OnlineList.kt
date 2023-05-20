package com.third.lhat.dependency.kthat.base.models


/**
 * 用法：
 * OnlineList.instance.list - 获得在线用户列表
 * 更改这个列表并不能让在线的用户增加或者减少。
 * 需要注意的是，您必须先初始化Connection类，才能获取本类实例。
 */
class OnlineList private constructor(var list: List<String>) {
    companion object {
        private lateinit var instance: OnlineList

        fun setList(list: List<String>) {
            if (!this::instance.isInitialized) {
                instance = OnlineList(list)
            }
            instance.list = list
        }

        fun getInstance(): OnlineList {
            if (!this::instance.isInitialized) {
                throw NullPointerException("Instance has not been initialized! ")
            }
            return instance
        }
    }
}