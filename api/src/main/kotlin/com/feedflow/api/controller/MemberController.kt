package com.feedflow.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class MemberController {

  @PostMapping("/v1/member/login")
  fun login(){

  }

}