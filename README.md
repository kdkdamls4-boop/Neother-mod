# Noether Client — Полная История Разработки и README

> Клиентский мод для Minecraft Fabric 1.20.4, разработанный для серверов типа HolyWorld / ReallyWorld / FunTime.
> Вдохновлён клиентами StarkClient, Expensive, Celestial, Wild, Akrien, Minced.

---

## Оглавление

1. [Что такое Noether Client](#1-что-такое-noether-client)
2. [Технический стек](#2-технический-стек)
3. [История создания — этапы разработки](#3-история-создания--этапы-разработки)
4. [Полный список модулей](#4-полный-список-модулей)
5. [Архитектура проекта](#5-архитектура-проекта)
6. [Управление и горячие клавиши](#6-управление-и-горячие-клавиши)
7. [Система конфигурации](#7-система-конфигурации)
8. [Сборка и запуск](#8-сборка-и-запуск)
9. [Дорожная карта (Roadmap)](#9-дорожная-карта-roadmap)
10. [Интерактивное превью ClickGUI](#10-интерактивное-превью-clickgui)

---

## 1. Что такое Noether Client

**Noether Client** — мощный клиентский визуальный и утилитарный мод для Minecraft (Fabric 1.20.4), ориентированный на игроков русскоязычных анархо-серверов. Мод предоставляет:

- **Визуальные преимущества** (ESP, Chams, Trails, HUD-оверлеи) в стиле топовых клиентов (Expensive 3.1, Celestial Recode)
- **Боевые утилиты** (AutoTotem, AutoClicker, ShieldBreaker, KillAura)
- **Удобство (QoL)** (AutoSprint, FastPlace, GuiMove, AutoRespawn)
- **Защиту и безопасность** (StaffAlert, Friend System, NoRender)
- **Полностью настраиваемый интерфейс** (ClickGUI, Draggable HUD, глобальные темы)

В этом репозитории:

| Путь | Что это |
|---|---|
| `src/main/java/com/noether/client/` | Исходники Fabric-мода |
| `src/test/java/.../VisualsCoreTest.java` | 8 JUnit 5 тестов ядра |
| `index.html` + `css/` + `js/` | Интерактивное превью ClickGUI / HUD |

---

## 2. Технический стек

| Компонент | Версия / Технология |
|---|---|
| Minecraft | 1.20.4 |
| Mod Loader | Fabric Loader |
| Маппинги | Yarn 1.20.4+build.3 |
| Fabric Loom | 1.6 |
| Java | 17 |
| Рендеринг | OpenGL (Tesselator, VertexConsumerProvider, MatrixStack) |
| Сборка | Gradle |
| Конфиг | JSON (Gson) → `.minecraft/noether/config.json` |
| Тесты | JUnit 5 |

---

## 3. История создания — этапы разработки

### Этап 0: Подготовка среды

- Инициализирован проект на базе Fabric MDK для Minecraft 1.20.4 с Yarn маппингами.
- Настроен `build.gradle`, `gradle.properties`, `settings.gradle`.
- Создана структура `src/main/java/com/noether/client/`.
- Конфигурация `fabric.mod.json` и `noether.mixins.json`.

### Этап 1: Базовое ядро и первые визуалы

**Инфраструктура**

- `NoetherClient.java` — точка входа (`ClientModInitializer`)
- `ModuleManager.java` — реестр модулей, кейбинды с защитой от дребезга и блокировкой при открытых экранах
- `EventBus` — шина событий
- `Module.java` — базовый класс с `BooleanSetting`, `NumberSetting`, `ModeSetting`, `ColorSetting`

**HUD V1:** Watermark, ArmorHUD, KeybindsList, TargetHUD (скин через `AbstractClientPlayerEntity.getSkinTextures()`, HP lerp), TotemPopCounter (`EntityStatuses.USE_TOTEM_OF_UNDYING` / `ADD_DEATH_PARTICLES`), HitParticles

**QoL:** AutoSprint, FastPlace, AutoRespawn, GuiMove, NoRender, FullBright (gamma-lock)

**Визуалы V1:** BoxESP, Tracers, NameTags, JumpCircles, Trails, ChinaHat, ViewModel (Pulsar / 1.7 / Swipe / Spin / Drop), CustomFog, CustomTime

**Combat V1:** AutoClicker, ShieldBreaker (`UpdateSelectedSlotC2SPacket`), AutoTotem, HitBoxes

### Этап 2: Масштабное визуальное обновление

- Friend System: `/friend add|remove|list|clear`, middle-click, отмена атаки
- Chams (Flat / Wireframe / Lighting / Texture, Hidden+Visible pass)
- GlowESP, TargetESP + Offscreen Arrows
- StorageESP, FreeCam, Notifications, PotionHUD
- Draggable HUD в чате
- ThemeManager + ConfigManager JSON
- JUnit: 8 тестов ядра

### Этап 3: Анархо-боевое ядро

- KillAura — silent-ротации, raytrace, кулдаун 1.9+, приоритеты, игнор друзей
- AutoTotem V2 — HP threshold, restore offhand
- ChestStealer — фильтр ценных предметов, 20–50 мс jitter, AutoClose
- StaffAlert — паттерны таба + spectator nearby

---

## 4. Полный список модулей

### Combat

| Модуль | Описание |
|---|---|
| `AutoTotem` | Свап тотема в оффхэнд с порогом HP |
| `AutoClicker` | Автокликер 1.9+ с пропуском друзей |
| `ShieldBreaker` | Авто-свап на топор при щите |
| `KillAura` | Silent-аура, raytrace, кулдаун |
| `HitBoxes` | Расширение хитбоксов врагов |
| `HitParticles` | Частицы при ударах |
| `TotemPopCounter` | Счётчик сбитых тотемов |

### Movement

| Модуль | Описание |
|---|---|
| `AutoSprint` | Постоянный спринт |
| `GuiMove` | Ходьба в GUI |
| `SafeWalk` | Не падать с края |
| `JumpCircles` | Круги при прыжке |
| `Trails` | Ribbon-шлейф |

### Render

| Модуль | Описание |
|---|---|
| `Chams` | Рендер сквозь стены |
| `GlowESP` | Неоновый контур |
| `BoxESP` | 2D/3D рамки |
| `Tracers` | Линии к игрокам |
| `NameTags` | Плашки HP / броня / попы |
| `TargetESP` | Кольца + offscreen arrows |
| `StorageESP` | Сундуки, шалкеры, эндер |
| `TargetHUD` | Панель цели со скином |
| `ChinaHat` | Конус над головой |
| `ViewModel` | Смещение рук + 5 анимаций |
| `CustomFog` | Цвет и дистанция тумана |
| `CustomTime` | Фиксация времени |
| `FreeCam` | Свободная камера |
| `FullBright` | Gamma-lock |
| `Watermark` | Логотип / FPS / ping |
| `ArmorHUD` | Прочность брони |
| `PotionHUD` | Зелья MM:SS |
| `KeybindsList` | Активные бинды |

### Player / Misc

| Модуль | Описание |
|---|---|
| `FastPlace` | Без задержки постановки |
| `AutoRespawn` | Мгновенный респавн |
| `NoRender` | HurtCam, Fire, Blindness… |
| `ChestStealer` | Автолут с jitter |
| `StaffAlert` | Детект стаффа |
| `MiddleClickFriend` | Колёсико = друг |

---

## 5. Архитектура проекта

```
src/main/java/com/noether/client/
├── NoetherClient.java
├── core/          ModuleManager, EventBus, RotationManager, TargetManager
├── modules/       combat, movement, render, player, misc
├── gui/           ClickGuiScreen, hud/*
├── render/        RenderUtils, Render3DUtils, Chams*
├── mixin/         19 client mixins
├── friend/        FriendManager, FriendCommands
├── config/        ConfigManager, ThemeManager, ConfigData
├── settings/      Boolean, Number, Mode, Color
└── util/          ColorUtils
```

---

## 6. Управление и горячие клавиши

| Клавиша | Действие |
|---|---|
| `Right Shift` | Открыть / закрыть ClickGUI |
| `T` или чат | Режим перетаскивания HUD |
| Колёсико по игроку | Добавить / удалить друга |
| `F` | FreeCam (по умолчанию) |

```
/friend add <ник>
/friend remove <ник>
/friend list
/friend clear
```

---

## 7. Система конфигурации

Файл: `.minecraft/noether/config.json`

- Состояния модулей, настройки, кейбинды
- Координаты HUD
- Список друзей
- Тема (`Neon Violet`, `Cyber Cyan`, `Sunset Orange`, `Rainbow`, `Custom RGB`)

---

## 8. Сборка и запуск

Требования: JDK 17+, Fabric Loader и Fabric API для 1.20.4.

```bash
./gradlew test
./gradlew build
./gradlew runClient
```

Готовый JAR: `build/libs/noether-client-1.0.0.jar` → `.minecraft/mods/`

---

## 9. Дорожная карта (Roadmap)

```
✅ Этап 1: фундамент и базовые визуалы
✅ Этап 2: шейдерные визуалы, friends, config, HUD
✅ Этап 3: KillAura, AutoTotem V2, ChestStealer, StaffAlert
⏳ Этап 4: CrystalAura, Velocity, облачные конфиги, SessionInfo HUD
```

---

## 10. Интерактивное превью ClickGUI

Откройте `index.html` (или live preview) — симулятор клиента:

- Пять панелей категорий, как в игре
- Переключение модулей, слайдеры, режимы, темы
- HUD: Watermark, TargetHUD, Armor, Potions, Keybinds, тосты
- Friend system и сохранение в `localStorage`

---

## Дисклеймер

Проект создан в образовательных целях для изучения архитектуры Fabric-модов.
Использование на серверах без разрешения нарушает их правила.

*Noether Client — Fabric API · OpenGL · Yarn · Java 17 · Gradle*
