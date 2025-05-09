package com.feedflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeedFlowApiApplication

fun main(args: Array<String>){
  runApplication<FeedFlowApiApplication>(*args)
}