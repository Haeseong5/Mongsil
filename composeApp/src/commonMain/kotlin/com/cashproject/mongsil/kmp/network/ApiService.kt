package com.cashproject.mongsil.kmp.network

import com.cashproject.mongsil.kmp.network.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * API 서비스 클래스
 * Ktor HttpClient를 사용하여 네트워크 통신을 수행합니다.
 */
class ApiService(private val client: HttpClient) {
    
    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }
    
    /**
     * 게시글 목록 조회
     * @return Post 리스트
     */
    suspend fun getPosts(): Result<List<Post>> {
        return try {
            val posts = client.get("$BASE_URL/posts").body<List<Post>>()
            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 특정 게시글 조회
     * @param id 게시글 ID
     * @return Post 객체
     */
    suspend fun getPost(id: Int): Result<Post> {
        return try {
            val post = client.get("$BASE_URL/posts/$id").body<Post>()
            Result.success(post)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 게시글 생성
     * @param post 생성할 Post 객체
     * @return 생성된 Post 객체
     */
    suspend fun createPost(post: Post): Result<Post> {
        return try {
            val createdPost = client.post("$BASE_URL/posts") {
                contentType(ContentType.Application.Json)
                setBody(post)
            }.body<Post>()
            Result.success(createdPost)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 리소스 해제
     */
    fun close() {
        client.close()
    }
}
