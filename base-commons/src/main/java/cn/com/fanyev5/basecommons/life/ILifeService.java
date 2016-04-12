package cn.com.fanyev5.basecommons.life;

/**
 * 生命周期服务接口
 *
 * @author fanqi427@gmail.com
 * @since 2013-7-4
 */
public interface ILifeService {

    /**
     * 启动
     */
    public abstract void start();

    /**
     * 停止
     */
    public abstract void stop();

}
