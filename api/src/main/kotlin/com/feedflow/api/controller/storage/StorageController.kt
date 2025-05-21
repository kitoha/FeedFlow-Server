package com.feedflow.api.controller.storage

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/api")
class StorageController {

  @PostMapping("/v1/storage")
  fun upload(@RequestParam file: MultipartFile){

  }
}