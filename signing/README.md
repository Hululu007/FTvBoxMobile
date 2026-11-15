# Release 签名

`2.1.4-mobile`（versionCode 7）起使用 2026-11-15 新生成的签名。应用 ID 为 `com.fbox.fbox`，密钥别名为 `fbox-native`，密钥格式为 PKCS12，算法为 RSA 3072，有效期为 10000 天。

- `release-keystore.p12.enc`：当前私钥的加密备份，使用 AES-256-CBC、PBKDF2、随机盐和 200000 次迭代。
- `release-certificate.pem`：当前公开证书，不含私钥。
- `release-certificate.json`：当前证书指纹、有效期和算法信息。

当前密钥、备份口令分别保存在本机 `~/.android/keystores/fbox-native-20261115/fbox-native-release.p12` 和 `release.pass`，仅当前用户可读。根目录 `release-signing.properties` 同样只在本机保存。请将私钥和口令另行备份到安全位置；仅有仓库内容不能恢复私钥。

## 在新电脑恢复当前签名

需要 OpenSSL 和 JDK 17。将备份口令文件安全地复制到本机，恢复到仅自己可访问的目录：

```sh
umask 077
mkdir -p ~/.android/keystores/fbox-native-20261115
openssl enc -d -aes-256-cbc -pbkdf2 -iter 200000 \
  -in signing/release-keystore.p12.enc \
  -out ~/.android/keystores/fbox-native-20261115/fbox-native-release.p12 \
  -pass file:/安全目录/release.pass
keytool -list -v -storetype PKCS12 \
  -keystore ~/.android/keystores/fbox-native-20261115/fbox-native-release.p12 \
  -storepass:file /安全目录/release.pass
```

核对别名和下方 SHA-256 后，将根目录 `release-signing.properties.example` 复制为 `release-signing.properties`，填写私钥绝对路径和本机口令。当前密钥的 storePassword 与 keyPassword 相同。按根目录 README 构建 release。加密备份已通过解密及逐字节比对验证。

证书 SHA-256：

```text
222ec043e288bb141b7f4e9fb7329725cf1a94022aa0d2deb2b9f5e0fd983d4c
```

这个签名与此前调试版及旧 release 签名不同。切换前先备份应用数据，再卸载旧签名版本、安装新包并恢复；后续使用同一签名即可覆盖升级。

## 历史签名备份

保留 `release-signing.enc.json` 和 `tools/restore_release_signing.py` 供历史版本恢复使用。它们对应旧别名 `fbox-mobile`、旧 SHA-256 `8ba70010a7fcd0a2d97178fcf5a688573ef788e9b6ee54a87938a337f83b21ce`，不是当前 release 的签名。该备份使用 Fernet 和 scrypt，需要原备份口令；不要用历史恢复脚本覆盖当前的签名配置。
