package com.shoejs.features.journal

class JournalService {

    fun createJournal(journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun getJournalById(id: Long): JournalResponse? =
        JournalRepository.getJournalById(id = id)?.toJournalResponse()

    fun updateJournal(id: Long, journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.updateJournalById(
            id = id, title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun deleteJournalById(id: Long): Boolean =
        JournalRepository.deleteJournalById(id = id)
}
