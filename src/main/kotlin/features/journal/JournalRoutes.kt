package com.shoejs.features.journal

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

fun Route.journalRoutes(journalService: JournalService) {
    route("/journals") {
        authenticate("jwt-auth") {
            post {
                val journalRequest = call.receive<JournalRequest>()

                journalService.createJournal(journalRequest)?.let { journalResponse ->
                    call.respond(HttpStatusCode.Created, journalResponse)
                } ?: call.respond(HttpStatusCode.BadRequest, "Invalid Journal Request")
            }
            get("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' missing in request"
                )

                journalService.getJournalById(journalId)?.let { journalResponse ->
                    call.respond(HttpStatusCode.OK, journalResponse)
                } ?: call.respond(HttpStatusCode.NotFound, "Journal Not Found")
            }
            put("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' missing in request"
                )
                val updateJournalRequest = call.receive<JournalRequest>()

                journalService.updateJournal(journalId, updateJournalRequest)?.let { journalResponse ->
                    call.respond(HttpStatusCode.OK, journalResponse)
                } ?: call.respond(HttpStatusCode.NotFound, "Journal Not Found")
            }
            delete("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' missing in request"
                )

                when(journalService.deleteJournalById(journalId)) {
                    true -> call.respond(HttpStatusCode.OK, "Journal Successfully deleted")
                    false -> call.respond(HttpStatusCode.NotFound, "Journal Not Found")
                }
            }
        }
    }
}
