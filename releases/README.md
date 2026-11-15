# Android 原生 Release

- 安装包：`fbox-native-2.1.4-mobile.apk`
- 应用 ID：`com.fbox.fbox`
- 版本：`2.1.4-mobile`（versionCode 7）
- 架构：arm64-v8a；Android 6.0 / API 23 起可安装。
- 单 MainActivity + Compose 导航，无业务 Fragment；播放器及复杂列表保留 AndroidView 桥接。
- R8 优化构建，`debuggable=false`；不包含 Flutter/Dart 引擎或代码。
- APK SHA-256：`0334dbb83a0a686eb1d83940c9d4d52bb7b563c836a062f1b5899cb62d255d7e`
- 签名证书 SHA-256：`222ec043e288bb141b7f4e9fb7329725cf1a94022aa0d2deb2b9f5e0fd983d4c`

已在 PKG110 真机安装本文件，核对版本号与关闭调试标记，并验证首页、搜索及设置页切换。切换签名前完成私有数据备份和恢复，配置、加密密钥及数据库的哈希比对一致。

签名与备份恢复方式见 [签名说明](../signing/README.md)。首次从旧签名版本切换时应先备份数据；后续版本沿用当前签名即可覆盖安装。

验证：Release 构建与关键 lint 检查、11 项 JVM 测试、76 套布局契约检查通过；单 Activity 改造的 14 项真机仪器测试已通过，覆盖生命周期、状态恢复、搜索、直播、历史续播及授权界面。
