package com.feedflow.api.controller.post

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PostController {

  @PostMapping("/v1/posts")
  fun createPost(){

  }
}