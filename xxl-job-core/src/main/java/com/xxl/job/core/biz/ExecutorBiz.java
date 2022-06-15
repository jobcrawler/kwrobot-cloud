package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.*;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface ExecutorBiz {

    /**
     * beat
     */
    public ReturnT<String> beat();

    /**
     * idle beat
     *
     * @param idleBeatParam
     */
    public ReturnT<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     */
    public ReturnT<String> run(TriggerParam triggerParam);

    /**
     * kill
     * @param killParam
     */
    public ReturnT<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam
     */
    public ReturnT<LogResult> log(LogParam logParam);

}
