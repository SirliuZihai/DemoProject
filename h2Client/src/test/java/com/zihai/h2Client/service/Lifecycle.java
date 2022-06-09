package com.zihai.h2Client.service;

public interface Lifecycle {
    /**
     * 启动
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 暂停
     */
    void pause();

    /**
     * 恢复
     */
    void resume();

    StateEnums getStatus();


    enum StateEnums {
        STOP(0),
        RUNING(1),
        PAUSE(2);

        private int state;

        StateEnums(Integer state) {
            this.state = state;
        }

        public Integer getState() {
            return state;
        }

        public String getChiness() {
            switch (state) {
                case 0:
                    return "停止";
                case 1:
                    return "运行中";
                case 2:
                    return "暂停";
            }
            return "";
        }
    }

}
