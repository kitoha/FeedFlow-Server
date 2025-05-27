package com.feedflow.api.controller.post

import com.feedflow.api.request.PostRequest
import com.feedflow.application.service.post.PostService
import com.feedflow.domain.model.Page
import com.feedflow.domain.model.Pageable
import com.feedflow.domain.model.post.Post
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PostController(private val postService: PostService) {
  @PostMapping("/v1/posts")
  fun createPost(@RequestBody postRequest: PostRequest){
    val post = postRequest.toPost()
    postService.createPost(post)
  }

  @GetMapping("/v1/posts")
  fun findPosts(@RequestParam(defaultValue = "0") page: Int,
    @RequestParam(defaultValue = "10") size: Int): ResponseEntity<Page<Post>>{
    val pageable = Pageable(page = page, size = size)
    return ResponseEntity.ok(postService.findPosts(pageable))
  }
}