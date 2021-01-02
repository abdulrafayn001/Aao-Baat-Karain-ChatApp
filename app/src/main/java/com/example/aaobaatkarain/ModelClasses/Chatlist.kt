package com.example.aaobaatkarain.ModelClasses

class Chatlist {
    private var id:String=""

    constructor()

    constructor(id:String){
        this.id=id
    }

    fun getId():String{
        return this.id
    }

    fun setId(id:String){
        this.id=id
    }
}