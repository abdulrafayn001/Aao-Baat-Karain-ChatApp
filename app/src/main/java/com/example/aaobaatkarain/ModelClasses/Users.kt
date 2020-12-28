package com.example.aaobaatkarain.ModelClasses

class Users {
    private var uid = ""
    private var username = ""
    private var profile = ""
    private var cover = ""
    private var status = ""
    private var search = ""
    private var facebook = ""
    private var instagram = ""
    private var website = ""



    constructor(
            uid: String,
            username: String,
            profile: String,
            cover: String,
            status: String,
            search: String,
            facebook: String,
            instagram: String,
            website: String
    ) {
        this.uid = uid
        this.username = username
        this.profile = profile
        this.cover = cover
        this.status = status
        this.search = search
        this.facebook = facebook
        this.instagram = instagram
        this.website = website
    }

    constructor()

    fun getUid(): String
    {
        return uid
    }

    fun getUsername(): String
    {
        return username
    }

    fun getProfile(): String
    {
        return profile
    }

    fun getCover(): String
    {
        return cover
    }

    fun getStatus(): String
    {
        return status
    }

    fun getSearch(): String
    {
        return search
    }


    fun getFacebook(): String
    {
        return facebook
    }


    fun getInstagram(): String
    {
        return instagram
    }


    fun getWebsite(): String
    {
        return website
    }

    fun setUid(Uid: String)
    {
        uid=Uid
    }

    fun setUsername(Username: String)
    {
        username=Username
    }

    fun setProfile(Profile: String)
    {
        profile=Profile
    }

    fun setCover(Cover: String)
    {
        cover=Cover
    }

    fun setStatus(Status:String)
    {
        status=Status
    }

    fun setSearch(Search:String)
    {
        search=Search
    }


    fun setFacebook(Facebook:String)
    {
        facebook=Facebook
    }


    fun setInstagram(Instagram:String)
    {
        instagram=Instagram
    }


    fun setWebsite(Website: String)
    {
        website= Website
    }
}