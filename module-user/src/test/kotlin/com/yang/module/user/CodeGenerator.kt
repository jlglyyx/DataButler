package com.yang.module.user

import com.baomidou.mybatisplus.generator.FastAutoGenerator
import com.baomidou.mybatisplus.generator.config.OutputFile
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import java.util.*



fun main() {
    // 数据库配置
    val url = "jdbc:mysql://localhost:3306/lpg?serverTimezone=GMT%2B8"
    val username = "root"
    val password = "root"

    // 核心：定义module-user模块的根路径（适配多模块项目结构）
    val moduleUserPath = System.getProperty("user.dir") + "/module-user"

    FastAutoGenerator.create(url, username, password)
        .globalConfig { builder ->
            builder.author("yyx") // 设置作者
                .enableKotlin() // 开启 Kotlin 模式 (关键！)
                // 1. Kotlin代码输出到module-user的src/main/kotlin下
                .outputDir("$moduleUserPath/src/main/kotlin")
                .disableOpenDir() // 生成后不自动打开文件夹（可选，避免多文件夹弹窗）
        }
        .packageConfig { builder ->
            builder.parent("com.yang.module") // 父包名
                .moduleName("user") // 模块名 → 最终包名：com.yang.module.user
                // 2. Mapper XML输出到module-user的src/main/resources/mapper下
                .pathInfo(Collections.singletonMap(
                    OutputFile.xml,
                    "$moduleUserPath/src/main/resources/mapper"
                ))
        }
        .strategyConfig { builder ->
            builder.addInclude("user_info") // 需要生成的表名
                .entityBuilder()
                .enableFileOverride() // 覆盖已生成文件
                // Kotlin 不需要 Lombok，自动生成 Data Class（保持注释说明）
                .controllerBuilder()
                .enableRestStyle() // 开启 @RestController
                .enableHyphenStyle() // URL驼峰转连字符（可选，如userInfo→user-info）
        }
        .templateEngine(VelocityTemplateEngine())
        .execute()

    println("✅ 代码已成功生成到 module-user 模块下！")
}
