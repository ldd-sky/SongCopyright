package com.ktvme.songcopyright.model;

/**
 * <p>Description: 响应</p >
 * <p>Copyright: Copyright (c)2024</p >
 * <p>Company: </p >
 * <P>Created Date: 2024年07月03日</P>
 *
 * @author LiuYuHan
 * @version 1.0
 */
public interface IResult {
    /**
     * code
     * @return {@link Integer}
     */
    Integer getCode();

    /**
     * message
     *
     * @return {@link String}
     */
    String getMessage();
}