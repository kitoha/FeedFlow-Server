package com.feedflow.domain.exception

sealed class FileStorageException(message: String, cause: Throwable? = null) :
  RuntimeException(message, cause) {

  class FileUploadException(fileName: String, cause: Throwable) :
    FileStorageException("Failed to upload file: $fileName", cause)

  class FileDownloadException(fileName: String, cause: Throwable) :
    FileStorageException("Failed to download file: $fileName", cause)

  class PreSignedUrlException(fileName: String, cause: Throwable) :
    FileStorageException("PreSignedUrlException : $fileName", cause)

  class BucketNotFoundException(bucketName: String) :
    FileStorageException("Bucket not found: $bucketName")

  class InvalidFileException(reason: String) :
    FileStorageException("Invalid file: $reason")
}