package com.shoejs.features.journal

class JournalService {

    fun createJournal(journalRequest: JournalRequest): JournalResponse? =
        JournalRepository.createJournal(
            title = journalRequest.title, content = journalRequest.content
        )?.toJournalResponse()

    fun getJournalById(id: Long): JournalResponse? =
        JournalRepository.getJournalById(id = id)?.toJournalResponse()
}
