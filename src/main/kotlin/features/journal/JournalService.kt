package com.shoejs.features.journal

import com.shoejs.common.pagination.PageResponse
import com.shoejs.common.query.QueryParams
import com.shoejs.features.user.UserRepository
import kotlin.math.ceil

class JournalService {

    // TODO: JournalResponse? vs JournalResponse
    fun createJournal(userId: Long, journalRequest: JournalRequest): JournalResponse? {
        return JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content, userId = userId
        ).let { savedJournal ->
            val username = UserRepository.getUserById(savedJournal.authorId)!!.username // <-- TODO: FIX THIS LINE
            savedJournal.toJournalResponse(username)
        }
    }

    // TODO: JournalResponse? vs JournalResponse
    fun getJournalById(userId: Long, journalId: Long): JournalResponse? {
        return JournalRepository.getJournalById(userId, journalId).let { journal ->
            val username = UserRepository.getUserById(journal.authorId)!!.username // <-- TODO: FIX THIS LINE
            journal.toJournalResponse(username)
        }
    }

    fun getAllJournals(userId: Long, queryParams: QueryParams): PageResponse<JournalSummaryResponse> {
        val journals = JournalRepository.getAllJournals(userId, queryParams).map { journal ->
            val username = UserRepository.getUserById(journal.authorId)!!.username // <-- TODO: FIX THIS LINE
            journal.toJournalSummaryResponse(username)
        }
        /*
        TODO: In future add something like:
            val authorIds = journals.map { it.authorId }.distinct()
            val usersById = UserRepository.getUsersByIds(authorIds).associateBy { it.id }
            So multiple calls to the UserRepository aren't needed to get the same username
         */

        val totalItems = JournalRepository.countJournals(userId, queryParams.query)

        return PageResponse(
            items = journals,
            page = queryParams.page,
            size = queryParams.size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / queryParams.size.toDouble()).toInt()
        )
    }

    fun updateJournal(id: Long, journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.updateJournalById(
            id = id, title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse("")

    fun deleteJournalById(userId: Long, journalId: Long): Boolean =
        JournalRepository.deleteJournalById(userId, journalId)
}
