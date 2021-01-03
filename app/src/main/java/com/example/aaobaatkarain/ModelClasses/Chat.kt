package com.example.aaobaatkarain.ModelClasses

class Chat {
    private var sender:String=""
    private var message:String=""
    private var receiver :String=""
    private var isSeen:Boolean=false
    private var url:String=""
    private var messageId:String=""

    constructor()

    constructor(
            sender:String,
            message:String,
            receiver :String,
            isSeen:Boolean,
            url:String,
            messageId:String
                ){
        this.sender=sender
        this.message=message
        this.receiver=receiver
        this.isSeen=isSeen
        this.url=url
        this.messageId=messageId
    }

    fun getSender(): String {
        return this.sender
    }

    fun getMessage():String{
        return this.message
    }

    fun getReceiver():String{
        return this.receiver
    }

    fun getIsSeen():Boolean{
        return this.isSeen
    }

    fun getURL():String{
        return this.url
    }

    fun getMessageId():String{
        return this.messageId
    }

    fun setSender(sender:String){
        this.sender=sender
    }

    fun setMessage(message:String){
        this.message=message
    }

    fun setReceiver(receiver:String){
        this.receiver=receiver
    }

    fun setIsSeen(isSeen:Boolean){
        this.isSeen=isSeen
    }

    fun setURL(url:String){
        this.url=url
    }

    fun setMessageId(messageId:String){
        this.messageId=messageId
    }

}