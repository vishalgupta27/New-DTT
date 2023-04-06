package com.example.dtt


import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("message")
    var message: String,
    @SerializedName("profile")
    var profile: Profile,
    @SerializedName("status")
    var status: Boolean,
    @SerializedName("token")
    var token: String
) {
    data class Profile(
        @SerializedName("email")
        var email: String,
        @SerializedName("first_name")
        var firstName: String,
        @SerializedName("id")
        var id: Int,
        @SerializedName("language")
        var language: String,
        @SerializedName("last_name")
        var lastName: String,
        @SerializedName("profession")
        var profession: String,
        @SerializedName("profile_image")
        var profileImage: String,
        @SerializedName("status")
        var status: Int,
        @SerializedName("user_id")
        var userId: String,
        @SerializedName("user_name")
        var userName: String
    )
}