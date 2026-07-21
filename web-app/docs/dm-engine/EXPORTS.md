# EXPORTS.md — dm-engine 脱敏引擎模块

**版本：** v1.0.0

## Service 接口

| 接口 | 方法 | 描述 | 调用方 |
|------|------|------|--------|
| `AlgorithmEntrance` | `check()` | 敏感数据识别 | sensdata |
| `AlgorithmEntrance` | `desensitize()` | 数据脱敏处理 | strategy, sensdata |
| `AlgorithmFactory` | `getAlgorithm()` | 获取脱敏算法 | strategy |

## 支持的敏感类型（35+种）

| 类型 | 检查器 | 类型 | 检查器 |
|------|--------|------|--------|
| 身份证 | IdNumberCheck | 银行卡 | BankCardCheck |
| 手机号 | MobilePhoneCheck | 邮箱 | EmailCheck |
| 姓名 | NameCheck | 地址 | AddressCheck |
| IP地址 | IpCheck | 护照 | ChinesePassportCheck/HkMcPassCheck |
| 统一社会信用代码 | SocialCreditCodeCheck | 手机号 | LandLineCheck |
| 驾驶证 | TransportPermitCheck | 医师证 | PhysicianCertificateCheck |
| 邮编 | PostalCodeCheck | 性别 | SexCheck |
| 自定义 | UserDefinedCheck | 字符串 | StringCheck |
| 军密 | MilitarySecretCheck | 公积金 | ProvidentFundCheck |
| 车牌号 | CarNumberCheck | 工商注册号 | TaxRegistrationCheck |
| 组织编码 | OrganizationCodeCheck | 港澳台通行证 | TwCompatriotPassCheck |
| 军官证 | OfficerCardCheck | 居住证 | PermanentResidentCheck |
| 医师执照 | PhysicianLicenseCheck | 组织机构名 | OrganizationNameCheck |

## 脱敏方式

| 方式 | 说明 | 示例 |
|------|------|------|
| 映射 | 映射到字典值 | 姓名→张** |
| 随机 | 生成随机值 | 手机号→138****1234 |
| 降维 | 降低精度 | 地址→北京* |
| 可逆 | 加密可还原 | SHA256哈希 |
| 分区 | 分段脱敏 | 身份证→110***********1234 |
