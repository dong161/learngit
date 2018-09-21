package com.kanq.train;

import com.jfinal.core.JFinal;

/**
 * 应用启动入口; 推荐放在 src/test/java目录下
 * @author LQ
 *
 */
public class TrainProjectAppRun {

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		// 第一个参数可以是 绝对路径
		JFinal.start("src/main/webapp", 8090, "/lq", 5);
	}
}