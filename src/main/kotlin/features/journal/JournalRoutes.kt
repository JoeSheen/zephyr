package com.shoejs.features.journal

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.journalRoutes(journalService: JournalService) {
    route("/journals") {
        //authenticate("jwt-auth") {
            post {
                val journalRequest = call.receive<JournalRequest>()

                journalService.createJournal(journalRequest)?.let { journalResponse ->
                    call.respond(HttpStatusCode.Created, journalResponse)
                } ?: call.respond(HttpStatusCode.BadRequest, "Invalid journal request")
            }
            get("/{journalId}") {
                val journalId = call.parameters["journalId"]?.toLong() ?: return@get call.respond(
                    HttpStatusCode.BadRequest, "Path parameter 'journalId' is missing in request"
                )

                journalService.getJournalById(journalId)?.let { journalResponse ->
                    call.respond(HttpStatusCode.OK, journalResponse)
                } ?: call.respond(HttpStatusCode.BadRequest, "Journal not found")
            }
        //}
    }
}
