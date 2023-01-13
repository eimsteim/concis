package com.joycoho.concis.kernel.context;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname PageFactory
 * @Description 默认的分页参数创建
 * @Version 1.0.0
 * @Date 2022/10/30 11:48
 * @Created by Leo
 */
public class PageFactory<T> {

    public Page<T> defaultPage() {
        HttpServletRequest request = HttpContext.getRequest();
        String current = request.getParameter("current");
        String pageSize = request.getParameter("pageSize");

        String sorter = request.getParameter("sorter");  //排序字段名称
        String order  = request.getParameter("order");   //asc或desc(升序或降序)

        Boolean searchCount = Boolean.FALSE;
        if (StringUtils.isEmpty(sorter)) {
            PageDTO<T> page = new PageDTO<>(Integer.valueOf(current), Integer.valueOf(pageSize));
            return page;
        } else {
            PageDTO<T> page = new PageDTO<>(Integer.valueOf(current), Integer.valueOf(pageSize), searchCount);
            List<OrderItem> list = new ArrayList<>();
            OrderItem orderItem;
            if ("asc".equals(order)) {
                orderItem = OrderItem.asc(sorter);
            } else {
                orderItem = OrderItem.desc(sorter);
            }
            list.add(orderItem);
            page.setOrders(list);
            return page;
        }
    }

    public Page<T> vuePage() {
        HttpServletRequest request = HttpContext.getRequest();
        int limit = Integer.valueOf(request.getParameter("limit"));     //pageSize
        int current = Integer.valueOf(request.getParameter("page"));   //pageNum
        String sort = request.getParameter("sort");         //排序字段名称
        String order = request.getParameter("order");       //asc或desc(升序或降序)
        Boolean searchCount = Boolean.FALSE;
        if (StringUtils.isEmpty(sort)) {
            PageDTO<T> page = new PageDTO<>(current, limit);
            return page;
        } else {
            PageDTO<T> page = new PageDTO<>(current, limit, searchCount);
            List<OrderItem> list = new ArrayList<>();
            OrderItem orderItem;
            if ("asc".equals(order)) {
                orderItem = OrderItem.asc(sort);
            } else {
                orderItem = OrderItem.desc(sort);
            }
            list.add(orderItem);
            page.setOrders(list);
            return page;
        }
    }
}