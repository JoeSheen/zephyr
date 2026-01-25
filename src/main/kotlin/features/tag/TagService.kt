package com.shoejs.features.tag

import com.shoejs.common.pagination.PageResponse
import com.shoejs.common.query.QueryParams
import kotlin.math.ceil

class TagService {

    fun createTag(tagRequest: TagRequest): TagResponse? =
        TagRepository.saveTag(name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun getTagById(id: Long): TagResponse? =
        TagRepository.getTagById(id = id)?.toTagResponse()

    fun getAllTags(queryParams: QueryParams): PageResponse<TagResponse> {
        val tags = TagRepository.getAllTags(queryParams).map { tag -> tag.toTagResponse() }
        val totalItems = TagRepository.countTags()
        return PageResponse(
            items = tags,
            page = queryParams.page,
            size = queryParams.size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / queryParams.size.toDouble()).toInt()
        )
    }

    fun updateTag(id: Long, tagRequest: TagRequest): TagResponse? =
        TagRepository.updateTagById(id = id, name = tagRequest.name, color = tagRequest.hexColor)?.toTagResponse()

    fun deleteTagById(id: Long): Boolean =
        TagRepository.deleteTagById(id = id)
}
