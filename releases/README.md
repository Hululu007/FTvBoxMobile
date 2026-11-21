# Android 原生 Release

- 安装包：`fbox-native-2.1.4-mobile.apk`
- 应用 ID：`com.fbox.fbox`
- 版本：`2.1.4-mobile`（versionCode 7）
- 架构：arm64-v8a；Android 6.0 / API 23 起可安装。
- 单 MainActivity + Compose 导航，无业务 Fragment；播放器及复杂列表保留 AndroidView 桥接。
- R8 优化构建，`debuggable=false`；不包含 Flutter/Dart 引擎或代码。
- 由提交 `0b4b3a1` 构建，包含首页切换源时的崩溃修复。
- APK SHA-256 为 `7a7341f81535b9f9bdd0b5a92b6aa2ab93b0d6bb9c2ebe826c98103bc1d133aa`。
- 签名证书 SHA-256：`222ec043e288bb141b7f4e9fb7329725cf1a94022aa0d2deb2b9f5e0fd983d4c`

本次构建已在 PKG110 真机覆盖安装，核对了版本号、调试标记和已安装 APK 的 SHA-256。

签名与备份恢复方式见 [签名说明](../signing/README.md)。首次从旧签名版本切换时应先备份数据；后续版本沿用当前签名即可覆盖安装。

此前的单 Activity 发布通过了 Release 构建与关键 lint 检查、11 项 JVM 测试、76 套布局契约检查和 14 项真机仪器测试。真机验证覆盖首页、搜索、设置页、生命周期、状态恢复、直播、历史续播及授权界面。切换签名前完成了应用私有数据的备份和恢复，配置、加密密钥及数据库的哈希比对一致。
