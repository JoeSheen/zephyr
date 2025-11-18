package com.shoejs.features.tag

class TagService {

    fun createTag(tagRequest: TagRequest): TagResponse? {
        val (name, color) = tagRequest
        return TagRepository.saveTag(
            name = name,
            color = color
        )?.toTagResponse()
    }

    fun getTagById(id: Long): TagResponse? {
        return null
    }

    fun getAllTags(): Collection<TagResponse> {
        return listOf()
    }
}