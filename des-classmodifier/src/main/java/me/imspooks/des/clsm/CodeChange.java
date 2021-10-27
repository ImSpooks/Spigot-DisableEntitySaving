package me.imspooks.des.clsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Nick on 27 Oct 2021.
 * Copyright © ImSpooks
 */
@Getter
@RequiredArgsConstructor
public class CodeChange {
    /**
     * Method name to modify.
     *
     * @return The current method name
     */
    private final String methodName;

    /**
     * Parameters of the method.
     *
     * @return The current parameters
     */
    private final String[] params;

    /**
     * Content that will be inserted.
     *
     * @return The content to insert
     */
    private final String content;

    /**
     * Insert the code before, after or at a specific line.
     * Options:
     * - insert: before
     * - insert: after
     * - insert: x
     *      x = line to insert
     *
     * @return The insertion type
     */
    private final String insert;

    /**
     * Whether this change is optional or not.
     *
     * @return {@code true} if the change is optional, {@code false} when it is required
     */
    private final boolean optional;

}