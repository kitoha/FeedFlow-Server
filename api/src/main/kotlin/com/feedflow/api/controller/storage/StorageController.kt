package com.feedflow.api.controller.storage

import com.feedflow.api.request.UploadRequest
import com.feedflow.api.validator.SimpleMultipartFileValidator
import com.feedflow.application.service.storage.FileService
import com.feedflow.domain.model.storage.MinioFileResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class StorageController(
  private val fileService: FileService,
  private val simpleMultipartFileValidator: SimpleMultipartFileValidator
) {

  @PostMapping("/v1/storage/upload",
    consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
  fun upload(@ModelAttribute uploadRequest: UploadRequest): ResponseEntity<Boolean> {
    val fileCommand = simpleMultipartFileValidator.toCommand(uploadRequest.file)
    return ResponseEntity.ok(fileService.upload(fileCommand, uploadRequest.bucketName))
  }

  @GetMapping("/v1/storage/download/{fileName}")
  fun downloadFile(
    @PathVariable fileName: String,
    @RequestParam bucketName: String
  ): ResponseEntity<ByteArray> {
    return ResponseEntity.ok(fileService.download(fileName, bucketName))
  }

  @GetMapping("/v1/storage/presigned-url/{fileName}")
  fun generatePreSignUrl(
    @PathVariable fileName: String,
    @RequestParam bucketName: String
  ): ResponseEntity<MinioFileResponse> {
    return ResponseEntity.ok(fileService.generatePreSignUrl(fileName, bucketName))
  }
}