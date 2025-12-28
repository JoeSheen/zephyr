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

                val journal = journalService.createJournal(journalRequest) ?: return@post call.respond(
                    HttpStatusCode.BadRequest, "Invalid journal request"
                )

                call.respond(HttpStatusCode.Created, journal)
            }
            get("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' is invalid or blank"
                )

                val journal = journalService.getJournalById(journalId) ?: return@get call.respond(
                    HttpStatusCode.NotFound, "Journal not found"
                )

                call.respond(HttpStatusCode.OK, journal)
            }
            put("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@put call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' is invalid or blank"
                )
                val updateJournalRequest = call.receive<JournalRequest>()

                val journal = journalService.updateJournal(journalId, updateJournalRequest) ?: return@put call.respond(
                    HttpStatusCode.NotFound, "Journal not found"
                )

                call.respond(HttpStatusCode.OK, journal)
            }
            delete("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@delete call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' is invalid or blank"
                )

                when(journalService.deleteJournalById(journalId)) {
                    true -> call.respond(HttpStatusCode.OK, "Journal successfully deleted")
                    false -> call.respond(HttpStatusCode.NotFound, "Journal not found")
                }
            }
        }
    }
}
