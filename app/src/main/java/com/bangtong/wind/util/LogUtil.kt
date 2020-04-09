package com.bangtong.wind.util

import android.util.Log

class LogUtil {
    companion object {
        const val VERBOSE = 1
        const val DEBUG = 2
        const val INFO = 3
        const val WARN = 4
        const val ERROR = 5
        const val NOTHING = 6
        var level: Int = LogUtil.VERBOSE

        fun v(tag: String, msg: String) {
            if (LogUtil.level <= LogUtil.VERBOSE) {
                Log.v(tag, msg)
            }
        }

        fun d(tag: String, msg: String) {
            if (LogUtil.level <= LogUtil.DEBUG) {
                Log.d(tag, msg)
            }
        }

        fun i(tag: String, msg: String) {
            if (LogUtil.level <= LogUtil.INFO) {
                Log.i(tag, msg)
            }
        }

        fun w(tag: String, msg: String) {
            if (LogUtil.level <= LogUtil.WARN) {
                Log.w(tag, msg)
            }
        }

        fun e(tag: String, msg: String) {
            if (LogUtil.level <= LogUtil.ERROR) {
                Log.e(tag, msg)
            }
        }
    }

}
