package com.feedflow.domain.model

data class Page<T>(
  val content: List<T>,
  val pageable: Pageable,
  val totalElements: Long,
  val totalPages: Int,
  val isLast: Boolean,
  val isFirst: Boolean,
  val isEmpty: Boolean
) {
  companion object {
    fun <T> empty(pageable: Pageable): Page<T> {
      return Page(
        content = emptyList(),
        pageable = pageable,
        totalElements = 0,
        totalPages = 0,
        isLast = true,
        isFirst = true,
        isEmpty = true
      )
    }
  }
}
