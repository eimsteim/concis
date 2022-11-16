package com.joycoho.concis.kernel.context;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RtnInfo<T> {

    private Integer code;

    private String message;

    private T data;

    /**
     * 兼容ProTable: success 请返回 true，不然 table 会停止解析数据，即使有数据
     */
    private Boolean success;
    /**
     * 兼容ProTable: 不传会使用 data 的长度，如果是分页一定要传
     */
    private Long total;

    public static final RtnInfo SUCCESS = RtnInfo.builder().code(BaseResponseStatus.SUCCESS.code())
            .message(BaseResponseStatus.SUCCESS.message()).success(Boolean.TRUE).build();

    public static RtnInfo error(IResponseEnum a) {
        return RtnInfo.builder().code(a.code()).success(Boolean.FALSE).message(a.message()).build();
    }

    public static RtnInfo error(Integer code, String msg) {
        return RtnInfo.builder().code(code).success(Boolean.FALSE).message(msg).build();
    }

    public static RtnInfo success(Object data) {
        return RtnInfo.builder().code(BaseResponseStatus.SUCCESS.code()).success(Boolean.TRUE).data(data).build();
    }

    public static RtnInfo success(Object data, String message) {
        return RtnInfo.builder().code(BaseResponseStatus.SUCCESS.code()).success(Boolean.TRUE)
                .data(data).message(message).build();
    }

    /**
     * 如果是分页查询，则需要插入total
     * @param page
     * @return
     */
    public static RtnInfo success(IPage page) {
        return RtnInfo.builder().code(BaseResponseStatus.SUCCESS.code()).success(Boolean.TRUE)
                .data(page.getRecords()).total(page.getTotal()).build();
    }
}
