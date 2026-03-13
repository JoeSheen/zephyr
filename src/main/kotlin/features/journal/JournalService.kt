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

    fun getJournalById(id: Long): JournalResponse? =
        JournalRepository.getJournalById(id = id)?.toJournalResponse("")

    fun getAllJournals(queryParams: QueryParams): PageResponse<JournalSummaryResponse> {
        val journals =
            JournalRepository.getAllJournals(queryParams).map { journal -> journal.toJournalSummaryResponse("") }
        val totalItems = JournalRepository.countJournals(queryParams.query)
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

    fun deleteJournalById(id: Long): Boolean =
        JournalRepository.deleteJournalById(id = id)
}
