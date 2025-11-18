package com.shoejs.features.tag

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.tagRoutes(tagService: TagService) {
    route("/tags") {
        authenticate("jwt-auth") {
            post {
                val tagRequest = call.receive<TagRequest>()

                val tag = tagService.createTag(tagRequest)
                if (tag == null) {
                    return@post call.respond(status = HttpStatusCode.BadRequest, message = "Invalid tag details")
                }

                call.respond(HttpStatusCode.Created, tag)
            }
            get("/{tagId}") {
                val tagId = call.parameters["tagId"]?.toLong()
                    ?: return@get call.respond(status = HttpStatusCode.BadRequest, message = "Invalid tag details")

                tagService.getTagById(tagId)?.let { tag ->
                    call.respond(status = HttpStatusCode.OK, message = tag)
                } ?: call.respond(status = HttpStatusCode.NotFound, message = "Tag not found")
            }
            get {
                tagService.getAllTags().let { tags ->
                    call.respond(HttpStatusCode.OK, tags)
                }
            }
        }
    }
}
