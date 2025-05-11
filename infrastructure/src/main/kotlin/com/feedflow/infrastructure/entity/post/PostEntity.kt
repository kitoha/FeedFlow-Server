package com.feedflow.infrastructure.entity.post

import com.feedflow.domain.enums.post.VisibilityType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class PostEntity(
  @Id
  @Column(name = "id")
  val id: String,

  @Column(name = "user_id", nullable = false)
  val userId: String,

  @Column(name = "content", nullable = false, length = 5000)
  var content: String,

  @ElementCollection
  @CollectionTable(
    name = "post_media_urls",
    joinColumns = [JoinColumn(name = "post_id")]
  )
  @Column(name = "media_url", nullable = false)
  var mediaUrls: MutableList<String> = mutableListOf(),

  @Enumerated(EnumType.STRING)
  @Column(name = "visibility", nullable = false)
  var visibility: VisibilityType,

  @Column(name = "created_at", nullable = false)
  val createdAt: LocalDateTime,

  @Column(name = "updated_at", nullable = false)
  var updatedAt: LocalDateTime,

  @Column(name = "deleted_at", nullable = false)
  val deletedAt: LocalDateTime?
)