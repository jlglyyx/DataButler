package com.yang.lib.common.data

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.extension.plugins.pagination.Page

open class PageRequest {

    @TableField(exist = false)
    var pageNum: Long = 1

    @TableField(exist = false)
    var pageSize: Long = 10

    fun <T> buildPage(): Page<T> = Page(pageNum, pageSize)
}
