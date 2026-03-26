package com.shoejs.features.tag

import com.shoejs.common.pagination.PageResponse
import com.shoejs.common.query.QueryParams
import kotlin.math.ceil

class TagService {

    fun createTag(userId: Long, tagRequest: TagRequest): TagResponse? =
        TagRepository.saveTag(userId, tagRequest).toTagResponse()

    fun getTagById(userId: Long, tagId: Long): TagResponse? =
        TagRepository.getTagById(userId, tagId).toTagResponse()

    fun getAllTags(userId: Long, queryParams: QueryParams): PageResponse<TagResponse> {
        val tags = TagRepository.getAllTags(userId, queryParams).map { tag -> tag.toTagResponse() }

        val totalItems = TagRepository.countTags(userId)

        return PageResponse(
            items = tags,
            page = queryParams.page,
            size = queryParams.size,
            totalItems = totalItems,
            totalPages = ceil(totalItems / queryParams.size.toDouble()).toInt()
        )
    }

    fun updateTag(userId: Long, tagId: Long, tagRequest: TagRequest): TagResponse? =
        TagRepository.updateTagById(userId, tagId, tagRequest).toTagResponse()

    fun deleteTagById(id: Long): Boolean =
        TagRepository.deleteTagById(id = id)
}
