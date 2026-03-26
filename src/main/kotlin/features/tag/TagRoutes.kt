package com.shoejs.features.tag

import com.shoejs.common.query.getQueryParameters
import com.shoejs.infrastructure.security.getUserIdentity
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
                val userId = call.getUserIdentity()
                val tagRequest = call.receive<TagRequest>()

                val tag = tagService.createTag(userId, tagRequest) ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Invalid tag request"
                )

                call.respond(HttpStatusCode.Created, tag)
            }
            get("/{tagId}") {
                val userId = call.getUserIdentity()
                val tagId = call.parameters["tagId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' is invalid or blank"
                )

                val tag = tagService.getTagById(userId, tagId) ?: return@get call.respond(
                    HttpStatusCode.NotFound, "Tag not found"
                )

                call.respond(HttpStatusCode.OK, tag)
            }
            get {
                val userId = call.getUserIdentity()
                val queryParams = call.getQueryParameters(defaultPage = 1, defaultSize = 20)

                val pageResponse = tagService.getAllTags(userId, queryParams)
                call.respond(HttpStatusCode.OK, pageResponse)
            }
            put("/{tagId}") {
                val userId = call.getUserIdentity()
                val tagId = call.parameters["tagId"]?.toLong() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' is invalid or blank"
                )
                val updateTagRequest = call.receive<TagRequest>()

                val tag = tagService.updateTag(userId, tagId, updateTagRequest) ?: return@put call.respond(
                    HttpStatusCode.NotFound, "Tag not found"
                )

                call.respond(HttpStatusCode.OK, tag)
            }
            delete("/{tagId}") {
                val tagId = call.parameters["tagId"]?.toLong() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'tagId' is invalid or blank"
                )

                when(tagService.deleteTagById(tagId)) {
                    true -> call.respond(HttpStatusCode.OK, "Tag successfully deleted")
                    false -> call.respond(HttpStatusCode.NotFound, "Tag not found")
                }
            }
        }
    }
}
