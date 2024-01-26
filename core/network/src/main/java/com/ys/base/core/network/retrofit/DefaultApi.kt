package com.ys.base.core.network.retrofit

import com.ys.base.core.network.model.Document
import com.ys.base.core.network.model.Post
import org.w3c.dom.Comment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DefaultApi {
    @GET("posts")
    fun listPosts(): Response<List<Post>>

    @GET("posts/{id}")
    fun getPost(@Path("id") id: String): Response<Post>

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") id: String): Response<List<Comment>>

    @POST("posts/{id}/comments")
    fun addComment(@Path("id") id: String, @Body comment: Comment): Response<Document>
}