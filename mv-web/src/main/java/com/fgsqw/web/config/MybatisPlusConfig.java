package com.fgsqw.web.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * MyBatis-Plus系统配置
 * </p>
 *
 * @author PcLing.
 * @ClassName {@link MybatisPlusConfig}
 * @Date 2022年7月20日 下午5:40:54
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

	/**
	 * 单页分页条数限制(默认无限制,参见 插件#handlerLimit 方法)
	 */
	private static final Long MAX_LIMIT = 5000L;

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 分页插件
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		paginationInnerInterceptor.setMaxLimit(MAX_LIMIT);
		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		// 防止全表更新与删除插件
		interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		// 乐观锁插件
		interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		return interceptor;
	}
}