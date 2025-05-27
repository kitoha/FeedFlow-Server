package com.feedflow.domain.model

data class Pageable(
  val page: Int,
  val size: Int,
){
  init {
    require(page >= 0) { "Page index must not be less than zero!"}
    require(size >=1){"Page size must not be less than one!"}
  }

  fun offset(): Long = page.toLong() * size.toLong()
}
