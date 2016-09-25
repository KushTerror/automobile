/*
 * Copyright (c) 2016 FC-POS Compulynx LTD.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are NOT permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COMPULYNX LTD BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.automobile.dal.log.impl;

import org.automobile.dal.log.AutomobileLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kushal
 * @Created On 3/27/2015.
 */
public class AutomobileLoggerImpl implements AutomobileLogger {
    Logger logger;

    public AutomobileLoggerImpl(Class aClass) {
        logger = LoggerFactory.getLogger(aClass);
    }

    /**
     * Log the information message to the information configuration.
     *
     * @param message to log
     */
    @Override
    public void info(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info(message);
            }
        }).start();
    }

    /**
     * Log the information message to the information configuration.
     *
     * @param message   to log
     * @param throwable to show trace
     */
    @Override
    public void info(final String message, final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info(message, throwable);
            }
        }).start();
    }

    /**
     * Log the error message to the error configuration.
     *
     * @param message to log
     */
    @Override
    public void error(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.error(message);
            }
        }).start();
    }

    /**
     * Log the error message to the error configuration.
     *
     * @param message   to log
     * @param throwable to show trace
     */
    @Override
    public void error(final String message, final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.error(message, throwable);
            }
        }).start();
    }
}
