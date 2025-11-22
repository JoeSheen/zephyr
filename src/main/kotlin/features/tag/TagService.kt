package com.shoejs.features.tag

class TagService {

    fun createTag(tagRequest: TagRequest): TagResponse? =
        TagRepository.saveTag(name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun getTagById(id: Long): TagResponse? =
        TagRepository.getTagById(id = id)?.toTagResponse()

    fun getAllTags(): List<TagResponse> =
        TagRepository.getAllTags().map { it.toTagResponse() }

    fun updateTag(id: Long, tagRequest: TagRequest): TagResponse? =
        TagRepository.updateTagById(id = id, name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun deleteTagById(id: Long): Boolean =
        TagRepository.deleteTagById(id = id)
}
