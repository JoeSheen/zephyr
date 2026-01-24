package com.shoejs.features.tag

import com.shoejs.common.pagination.PageResponse
import kotlin.math.ceil

class TagService {

    fun createTag(tagRequest: TagRequest): TagResponse? =
        TagRepository.saveTag(name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun getTagById(id: Long): TagResponse? =
        TagRepository.getTagById(id = id)?.toTagResponse()

    fun getAllTags(page: Int, size: Int): PageResponse<TagResponse> {
        val offset = (page - 1) * size
        val tags = TagRepository.getAllTags(offset, size).map { tag -> tag.toTagResponse() }
        val totalItems = TagRepository.countTags()
        return PageResponse(
            items = tags,
            page = page,
            size = size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / size.toDouble()).toInt()
        )
    }

    fun updateTag(id: Long, tagRequest: TagRequest): TagResponse? =
        TagRepository.updateTagById(id = id, name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun deleteTagById(id: Long): Boolean =
        TagRepository.deleteTagById(id = id)
}
