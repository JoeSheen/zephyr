package com.shoejs.features.tag

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.tagRoutes(tagService: TagService) {
    route("/tags") {
        authenticate("jwt-auth") {
            post {
                val tagRequest = call.receive<TagRequest>()

                tagService.createTag(tagRequest)?.let { tagResponse ->
                    call.respond(HttpStatusCode.Created, tagResponse)
                } ?: call.respond(HttpStatusCode.BadRequest, "Invalid Tag Request")
            }
            get("/{tagId}") {
                val tagId = call.parameters["tagId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' missing in request"
                )

                tagService.getTagById(tagId)?.let { tagResponse ->
                    call.respond(HttpStatusCode.OK, tagResponse)
                } ?: call.respond(HttpStatusCode.NotFound, "Tag not found")
            }
            get {
                tagService.getAllTags().let { tags ->
                    call.respond(HttpStatusCode.OK, tags)
                }
            }
            put("/{tagId}") {
                val tagId = call.parameters["tagId"]?.toLong() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' missing in request"
                )
                val updateTagRequest = call.receive<TagRequest>()

                tagService.updateTag(tagId, updateTagRequest)?.let { tagResponse ->
                    call.respond(HttpStatusCode.OK, tagResponse)
                } ?: call.respond(HttpStatusCode.NotFound, "Tag not found")
            }
            delete("/{tagId}") {
                val tagId = call.parameters["tagId"]?.toLong() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' missing in request"
                )

                when(tagService.deleteTagById(tagId)) {
                    true -> call.respond(HttpStatusCode.OK, "Tag successfully deleted")
                    false -> call.respond(HttpStatusCode.NotFound, "Tag not found")
                }
            }
        }
    }
}
