package com.shoejs.features.journal

import com.shoejs.common.pagination.PageResponse
import com.shoejs.common.query.QueryParams
import kotlin.math.ceil

class JournalService {

    fun createJournal(journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun getJournalById(id: Long): JournalResponse? =
        JournalRepository.getJournalById(id = id)?.toJournalResponse()

    fun getAllJournals(queryParams: QueryParams): PageResponse<JournalSummaryResponse> {
        val journals =
            JournalRepository.getAllJournals(queryParams).map { journal -> journal.toJournalSummaryResponse() }
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
        )?.toJournalResponse()

    fun deleteJournalById(id: Long): Boolean =
        JournalRepository.deleteJournalById(id = id)
}
