package com.feedflow.api.controller.post

import com.feedflow.api.request.PostRequest
import com.feedflow.application.service.post.PostService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PostController(private val postService: PostService) {
  @PostMapping("/v1/posts")
  fun createPost(@RequestBody postRequest: PostRequest){
    val post = postRequest.toPost()
    postService.createPost(post)
  }
}