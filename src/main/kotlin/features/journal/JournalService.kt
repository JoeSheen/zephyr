package com.shoejs.features.journal

import com.shoejs.common.pagination.PageResponse
import kotlin.math.ceil

class JournalService {

    fun createJournal(journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun getJournalById(id: Long): JournalResponse? =
        JournalRepository.getJournalById(id = id)?.toJournalResponse()

    fun getAllJournals(page: Int, size: Int): PageResponse<JournalResponse> {
        val offset = (page - 1) * size
        val journals = JournalRepository.getAllJournals(offset, size).map { journal -> journal.toJournalResponse() }
        val totalItems = JournalRepository.countJournals()
        return PageResponse(
            items = journals,
            page = page,
            size = size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / size.toDouble()).toInt()
        )
    }

    fun updateJournal(id: Long, journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.updateJournalById(
            id = id, title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun deleteJournalById(id: Long): Boolean =
        JournalRepository.deleteJournalById(id = id)
}
