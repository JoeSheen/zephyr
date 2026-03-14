package com.shoejs.features.journal

import com.shoejs.common.pagination.PageResponse
import com.shoejs.common.query.QueryParams
import com.shoejs.features.user.UserRepository
import kotlin.math.ceil

class JournalService {

    fun createJournal(userId: Long, journalRequest: JournalRequest): JournalResponse {
        return JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content, userId = userId
        ).let { savedJournal ->
            val username = this.getUsernameForJournalAuthor(savedJournal.authorId)
            savedJournal.toJournalResponse(username)
        }
    }

    fun getJournalById(userId: Long, journalId: Long): JournalResponse {
        return JournalRepository.getJournalById(userId, journalId).let { journal ->
            val username = this.getUsernameForJournalAuthor(journal.authorId)
            journal.toJournalResponse(username)
        }
    }

    fun getAllJournals(userId: Long, queryParams: QueryParams): PageResponse<JournalSummaryResponse> {
        val usernameCache = mutableMapOf<Long, String>() // Test to see if this actually improves performance
        val journals = JournalRepository.getAllJournals(userId, queryParams).map { journal ->
            val username = usernameCache.getOrPut(journal.authorId) {
                this.getUsernameForJournalAuthor(journal.authorId)
            }
            journal.toJournalSummaryResponse(username)
        }

        val totalItems = JournalRepository.countJournals(userId, queryParams.query)

        return PageResponse(
            items = journals,
            page = queryParams.page,
            size = queryParams.size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / queryParams.size.toDouble()).toInt()
        )
    }

    fun updateJournal(userId: Long, journalId: Long, journalRequest: JournalRequest): JournalResponse {
        return JournalRepository.updateJournalById(userId, journalId, journalRequest.title, journalRequest.content)
            .let { journal ->
                val username = this.getUsernameForJournalAuthor(journal.authorId)
                journal.toJournalResponse(username)
            }
    }

    fun deleteJournalById(userId: Long, journalId: Long): Boolean =
        JournalRepository.deleteJournalById(userId, journalId)

    private fun getUsernameForJournalAuthor(userId: Long): String = UserRepository.getUsernameForUserId(userId)
}
