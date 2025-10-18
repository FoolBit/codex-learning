# Time Overlay

一个使用 Kotlin 编写的 Android 应用，可以以悬浮窗的形式实时显示当前时间（精确到毫秒），并允许用户拖动窗口进行自定义摆放。

## 功能特性

- **毫秒级时间显示**：通过前台服务在悬浮窗中实时刷新 `HH:mm:ss.SSS` 格式的时间。
- **悬浮窗权限引导**：应用会检测 `SYSTEM_ALERT_WINDOW` 权限，提供一键跳转到系统设置页的引导。
- **拖拽移动**：用户可以长按并拖动悬浮窗到屏幕任意位置，布局参数实时更新。
- **前台服务保障**：使用前台服务配合通知渠道，确保在 Android 8.0 及以上系统上持续显示。

## 工程结构

```
TimeOverlay/
├── app/
│   ├── build.gradle.kts        # 模块级 Gradle 配置
│   ├── proguard-rules.pro      # 混淆配置
│   └── src/main/
│       ├── AndroidManifest.xml # 权限、Activity、Service 声明
│       ├── java/com/example/timeoverlay/
│       │   ├── MainActivity.kt # 权限处理与 UI 控制
│       │   └── OverlayService.kt # 悬浮窗前台服务
│       └── res/
│           ├── layout/         # 页面与悬浮窗布局
│           ├── values/         # 主题、字符串、颜色
│           └── drawable/       # 悬浮窗背景样式
├── build.gradle.kts            # 项目级 Gradle 配置
└── settings.gradle.kts         # 模块声明
```

## 开发环境要求

- Android Studio Giraffe (或更新版本)
- Android Gradle Plugin 8.1.4
- Kotlin 1.9.10
- JDK 17
- Android SDK 34（可根据需要调整）

> **提示**：仓库中未提供 Gradle Wrapper，请在 Android Studio 中导入项目后根据提示自动生成，或运行 `gradle wrapper`（确保本地已安装对应版本的 Gradle）。

## 快速开始

1. 将仓库克隆到本地：
   ```bash
   git clone <repository-url>
   cd TimeOverlay
   ```
2. 在 Android Studio 中 `File > Open...` 选择项目根目录导入。
3. 首次同步时，Android Studio 会根据 `build.gradle.kts` 自动下载所需依赖。
4. 连接设备或启动模拟器，点击 **Run** 按钮即可安装并运行应用。

## 使用说明

1. 首次启动会显示权限状态提示，如未授予悬浮窗权限，点击“打开设置”跳转系统设置开启权限。
2. 权限授予后，点击“开始显示悬浮窗”即可启动前台服务并在屏幕上显示悬浮窗。
3. 长按悬浮窗并拖动即可调整位置。点击“关闭悬浮窗”会停止服务并移除悬浮窗。
4. 悬浮窗关闭后，如需再次显示，可重新点击“开始显示悬浮窗”。

## 代码关键点

- `OverlayService` 中通过 `WindowManager` 添加布局，并使用 `Handler` 每 10ms 更新一次 `TextView`，确保毫秒级刷新。
- 使用 `NotificationChannel` 与 `startForeground`，满足 Android 8.0+ 的后台限制要求。
- `MainActivity` 使用 `ActivityResultContracts.StartActivityForResult` 引导用户前往权限设置，并在权限状态变化时更新按钮可用性。

## 常见问题

- **无法启动悬浮窗**：请确认在系统设置中已授予悬浮窗权限，并重新回到应用界面。
- **通知常驻**：为保证服务不被系统回收，必须展示一个低优先级的前台通知，可在系统设置中隐藏通知渠道。
- **自定义刷新频率**：可在 `OverlayService` 中调整 `UPDATE_INTERVAL_MS` 常量改变更新时间间隔。

## 许可证

本项目采用 [MIT License](https://opensource.org/licenses/MIT)。
