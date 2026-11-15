# Compose 迁移审查与修复

审查对象：`feature/jetpack-compose-migration` 的迁移提交 `55f3d76`；功能对照为父提交 `638cbaa`。

## 主要发现

| 严重度 | 问题 | 影响与修复 |
| --- | --- | --- |
| 阻断 | `ComposeLayouts` 中的 `AndroidView.factory` 晚于原控制器的同步绑定 | 首页、分类、搜索、设置等拿不到实际 View。恢复真实 XML 的同步创建，移除隐藏占位 View。 |
| 阻断 | 删除 XML 后用 `ids.xml` 的空 layout 声明代替 | 资源名存在，但不能提供页面结构；仍执行原生 inflate 的路径和测试失败。恢复 145 个布局资源，删除空 layout 声明。 |
| 高 | 设置项类型和可见界面脱节 | 控制器读取的 TextView 被普通容器替代，更新无法反映到用户界面。恢复原页面控件契约。 |
| 高 | 列表迁移丢失原 Adapter 行为 | 长按、焦点、选中态、图片绑定和分页等行为不完整。恢复 30 个 Adapter、详情和直播控制器的对应绑定，以及 LoadMoreView 的实际布局。 |
| 高 | 状态复杂的弹窗只迁移了展示 | 筛选后续点击无法继续修改业务对象，字幕搜索缺少分页等。恢复筛选、选集、字幕、弹幕设置和搜索等弹窗的原生实现。 |
| 高 | 普通 Dialog 未提供 Compose 所需的生命周期和状态保存接口 | 所有弹窗统一继承 `ComponentDialog`，并给实际内容 View 绑定 LifecycleOwner 和 SavedStateRegistryOwner；在 show 时刷新窗口的 owners，支持面板重新挂载和弹窗复用。 |
| 高 | 原生 NestedScrollView 对 Compose 内容采用无限高度测量 | 含 Lazy 列表或 verticalScroll 的弹窗可能崩溃。ComposeView 直接放入受高度约束的面板，原生内容保留原滚动容器。 |
| 中 | 搜索范围在关闭后未持久化，复用弹窗时仍显示旧数据 | 在 show 时同步调用方数据，在 dismiss 时通过 SearchHelper 保存选择。 |
| 中 | 配置历史删除较早条目后选中位置偏移 | 按删除位置调整索引；补充可访问的行选中语义和独立删除按钮描述。 |

## 当前 UI 状态

本次优先恢复已有功能，结果为 XML/View 与 Compose 混合 UI。依赖同步 `findViewById` 的页面、列表和复杂弹窗恢复原生实现；Compose 导航和独立弹窗继续保留。迁移时新增的 Compose 页面源码暂时保留。

这不代表完整 Compose 迁移已经完成。后续应逐页将状态和操作从 View 控制器提取出来，在验证相同行为后替换单个页面，再删除不再引用的 XML；不应继续用隐藏 View 或空资源模拟已迁移页面。

## 验证

- Debug 应用、AndroidTest APK 构建通过。
- 单元测试 5 项通过。
- 在 PKG110 / Android 17 真机分批验证 31 个不同用例：原有 NativeBehaviorTest 27 项、新增 ComposeInteropBehaviorTest 4 项，均已通过。
- 完整 31 项运行中有 22 项通过，9 项被系统的原生库兼容提示遮挡而失去窗口焦点。关闭提示后单独重跑这 9 项，结果为 `OK (9 tests)`；没有把首次失败运行计作全量通过。
- UI 自动化期间关闭系统动画（Gradle 中也配置了 animationsDisabled）；结束后恢复手机原来的三个动画比例 1.0，并人工复查正常启动、首页海报、设置和布局弹窗。
- 为 Android 17 上失败截图的 PixelCopy 停滞关闭 Espresso 自动失败截图，保留断言和错误报告。测试显式截图仍然保留。
- 测试通过 HawkPreferencesCheckpoint 保存原始加密偏好，在正常结束或下一轮崩溃恢复时还原，避免临时配置和选择污染用户设置。
- 测试使用本地 HTTP 配置与媒体 fixture，覆盖界面和播放链路；外部内容源、真实投屏接收器等服务仍取决于各自环境。

新增 `ComposeInteropBehaviorTest` 覆盖同步控件绑定、Compose 弹窗测量、搜索范围持久化与复用、配置历史异步更新及删除后的选择。

## 尚存限制

原有预编译播放器库在这台 Android 17 设备上会触发 16 KB ELF 对齐兼容提示，涉及 libp2p、libplayer、libffmpegJNI、libijksdl 等。此次关闭系统提示后，直播、网页解析媒体、链接播放和本地文件均已实际解码通过；原生库本身的对齐升级仍需单独处理。本次修复未将关闭提示描述为已修复原生库兼容性。
