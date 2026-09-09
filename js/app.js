const MODULES = [
  { name: "AutoTotem", cat: "Combat", desc: "Свап тотема в оффхэнд с порогом HP", on: true, bind: "NONE",
    settings: [
      { k: "HP Threshold", type: "num", min: 0, max: 20, step: 0.5, v: 8 },
      { k: "Smart", type: "bool", v: true },
      { k: "Restore", type: "bool", v: true }
    ]},
  { name: "AutoClicker", cat: "Combat", desc: "Автокликер 1.9+ с пропуском друзей",
    settings: [
      { k: "Weapons Only", type: "bool", v: true },
      { k: "1.9 Cooldown", type: "bool", v: true },
      { k: "CPS", type: "num", min: 1, max: 20, v: 12 },
      { k: "Skip Friends", type: "bool", v: true }
    ]},
  { name: "ShieldBreaker", cat: "Combat", desc: "Авто-свап на топор при блоке щитом",
    settings: [{ k: "Switch Back", type: "num", min: 1, max: 20, v: 4 }]},
  { name: "KillAura", cat: "Combat", desc: "Silent-аура, raytrace, кулдаун 1.9+",
    settings: [
      { k: "Range", type: "num", min: 2.5, max: 6, step: 0.1, v: 4.2 },
      { k: "Priority", type: "mode", options: ["Distance", "Health", "Angle"], v: "Distance" },
      { k: "Silent", type: "bool", v: true },
      { k: "Players", type: "bool", v: true },
      { k: "Through Walls", type: "bool", v: false }
    ]},
  { name: "HitBoxes", cat: "Combat", desc: "Расширение хитбоксов врагов",
    settings: [{ k: "Expand", type: "num", min: 0, max: 1.5, step: 0.05, v: 0.3 }]},
  { name: "HitParticles", cat: "Combat", desc: "Частицы при ударах и критах",
    settings: [
      { k: "Mode", type: "mode", options: ["Crit", "Enchant", "Dust", "Soul"], v: "Crit" },
      { k: "Always", type: "bool", v: true },
      { k: "Dust Color", type: "color", v: "#a78bfa" }
    ]},
  { name: "TotemPopCounter", cat: "Combat", desc: "Счётчик сбитых тотемов",
    settings: [{ k: "Notify", type: "bool", v: true }]},

  { name: "AutoSprint", cat: "Movement", desc: "Постоянный бег без двойного W", on: true },
  { name: "GuiMove", cat: "Movement", desc: "Движение с открытым инвентарём" },
  { name: "SafeWalk", cat: "Movement", desc: "Не падать с края блоков" },
  { name: "JumpCircles", cat: "Movement", desc: "Круги на земле при прыжке", on: true,
    settings: [{ k: "Duration", type: "num", min: 0.4, max: 3, step: 0.1, v: 1.4 }]},
  { name: "Trails", cat: "Movement", desc: "Ribbon-шлейф с time-based alpha", on: true,
    settings: [{ k: "Length", type: "num", min: 8, max: 64, v: 24 }]},

  { name: "Chams", cat: "Render", desc: "Шейдерный рендер сквозь стены", on: true,
    settings: [
      { k: "Mode", type: "mode", options: ["Flat", "Wireframe", "Lighting", "Texture"], v: "Flat" },
      { k: "Color Mode", type: "mode", options: ["Custom", "Rainbow", "Wave"], v: "Custom" },
      { k: "Visible", type: "color", v: "#a78bfa" }
    ]},
  { name: "GlowESP", cat: "Render", desc: "Неоновый контур через stencil", on: true },
  { name: "BoxESP", cat: "Render", desc: "2D/3D рамки вокруг игроков", on: true,
    settings: [
      { k: "Mode", type: "mode", options: ["3D", "2D"], v: "3D" },
      { k: "Fill", type: "bool", v: true }
    ]},
  { name: "Tracers", cat: "Render", desc: "Линии к ближайшим врагам" },
  { name: "NameTags", cat: "Render", desc: "Плашки HP, броня, тотем-попы", on: true },
  { name: "TargetESP", cat: "Render", desc: "3D кольца + offscreen arrows", on: true },
  { name: "StorageESP", cat: "Render", desc: "Сундуки, шалкеры, эндер-сундуки",
    settings: [{ k: "Mode", type: "mode", options: ["Outline", "Fill", "Both"], v: "Outline" }]},
  { name: "TargetHUD", cat: "Render", desc: "Панель цели со скином и lerp HP", on: true },
  { name: "ChinaHat", cat: "Render", desc: "Светящийся конус над головой" },
  { name: "ViewModel", cat: "Render", desc: "Смещение рук + 5 анимаций",
    settings: [
      { k: "X", type: "num", min: -2, max: 2, step: 0.05, v: 0 },
      { k: "Y", type: "num", min: -2, max: 2, step: 0.05, v: 0 },
      { k: "Z", type: "num", min: -2, max: 2, step: 0.05, v: 0 },
      { k: "Swing", type: "mode", options: ["Pulsar", "1.7", "Swipe", "Spin", "Drop"], v: "Pulsar" }
    ]},
  { name: "CustomFog", cat: "Render", desc: "Цвет и дистанция тумана",
    settings: [{ k: "Color", type: "color", v: "#2e1065" }, { k: "Distance", type: "num", min: 0.05, max: 1.5, step: 0.05, v: 0.55 }]},
  { name: "CustomTime", cat: "Render", desc: "Фиксация времени суток",
    settings: [{ k: "Mode", type: "mode", options: ["Night", "Day", "Noon", "Sunset", "Custom"], v: "Night" }]},
  { name: "FreeCam", cat: "Render", desc: "Полёт камеры сквозь блоки", bind: "F" },
  { name: "FullBright", cat: "Render", desc: "Gamma-lock против сброса", on: true },
  { name: "Watermark", cat: "Render", desc: "Логотип, FPS и пинг", on: true },
  { name: "ArmorHUD", cat: "Render", desc: "Прочность надетой брони", on: true },
  { name: "PotionHUD", cat: "Render", desc: "Зелья с таймерами MM:SS", on: true },
  { name: "KeybindsList", cat: "Render", desc: "Список активных биндов", on: true },

  { name: "FastPlace", cat: "Player", desc: "Мгновенная установка блоков",
    settings: [{ k: "Delay", type: "num", min: 0, max: 4, v: 0 }]},
  { name: "AutoRespawn", cat: "Player", desc: "Респавн без экрана смерти" },
  { name: "NoRender", cat: "Player", desc: "HurtCam, Fire, Blindness, Pumpkin", on: true,
    settings: ["HurtCam","Fire","Blindness","Darkness","Pumpkin"].map(k => ({ k, type: "bool", v: true })) },
  { name: "ChestStealer", cat: "Player", desc: "Автолут с jitter 20–50 мс",
    settings: [
      { k: "Min Delay", type: "num", min: 0, max: 200, v: 20 },
      { k: "Max Delay", type: "num", min: 0, max: 250, v: 50 },
      { k: "Smart Filter", type: "bool", v: true },
      { k: "AutoClose", type: "bool", v: true }
    ]},
  { name: "StaffAlert", cat: "Misc", desc: "Детект стаффа и спектаторов", on: true },
  { name: "MiddleClickFriend", cat: "Misc", desc: "Колёсико мыши — друг", on: true }
];

const CATS = ["Combat", "Movement", "Render", "Player", "Misc"];
const THEMES = [
  { id: "NEON_VIOLET", name: "Neon Violet", accent: "#a78bfa", dark: "#6d28d9" },
  { id: "CYBER_CYAN", name: "Cyber Cyan", accent: "#22d3ee", dark: "#0891b2" },
  { id: "SUNSET_ORANGE", name: "Sunset Orange", accent: "#fb923c", dark: "#f43f5e" },
  { id: "RAINBOW", name: "Rainbow", accent: "#ec4899", dark: "#8b5cf6" },
  { id: "CUSTOM", name: "Custom", accent: "#a78bfa", dark: "#6d28d9" }
];

const state = {
  theme: "NEON_VIOLET",
  gui: true,
  selected: null,
  binding: null,
  friends: ["Neoter", "holy_kit"],
  fps: 240,
  ping: 34
};

function load() {
  try {
    const raw = JSON.parse(localStorage.getItem("noether-config") || "null");
    if (!raw) return;
    state.theme = raw.theme || state.theme;
    state.friends = raw.friends || state.friends;
    if (raw.modules) {
      MODULES.forEach(m => {
        const s = raw.modules[m.name];
        if (!s) return;
        m.on = !!s.enabled;
        m.bind = s.keybind || m.bind || "NONE";
        if (s.settings && m.settings) {
          m.settings.forEach(st => { if (s.settings[st.k] !== undefined) st.v = s.settings[st.k]; });
        }
      });
    }
  } catch {}
}
function save() {
  const modules = {};
  MODULES.forEach(m => {
    modules[m.name] = { enabled: !!m.on, keybind: m.bind || "NONE", settings: {} };
    (m.settings || []).forEach(s => modules[m.name].settings[s.k] = s.v);
  });
  localStorage.setItem("noether-config", JSON.stringify({ theme: state.theme, friends: state.friends, modules }));
}

function isOn(name) { return MODULES.find(m => m.name === name)?.on; }

function applyTheme() {
  const t = THEMES.find(x => x.id === state.theme) || THEMES[0];
  let accent = t.accent, dark = t.dark;
  if (state.theme === "RAINBOW") {
    const h = (Date.now() / 20) % 360;
    accent = `hsl(${h} 85% 65%)`;
    dark = `hsl(${(h + 40) % 360} 70% 40%)`;
  }
  document.documentElement.style.setProperty("--accent", accent);
  document.documentElement.style.setProperty("--accent-2", dark);
  document.querySelectorAll(".theme-dot").forEach(d => d.classList.toggle("active", d.dataset.id === state.theme));
}

function toast(title, msg, type) {
  const el = document.createElement("div");
  el.className = "toast";
  if (type === "ok") el.style.borderLeftColor = "var(--ok)";
  if (type === "err") el.style.borderLeftColor = "var(--err)";
  if (type === "warn") el.style.borderLeftColor = "var(--warn)";
  el.innerHTML = `<b>${title}</b><span>${msg}</span>`;
  document.getElementById("toasts").prepend(el);
  setTimeout(() => el.remove(), 2800);
}

function renderHud() {
  const wm = document.getElementById("watermark");
  wm.classList.toggle("hidden", !isOn("Watermark"));
  wm.textContent = `NOETHER  |  ${state.fps} fps  |  ${state.ping} ms`;

  const kb = document.getElementById("keybinds");
  kb.classList.toggle("hidden", !isOn("KeybindsList"));
  const bound = MODULES.filter(m => m.on && m.bind && m.bind !== "NONE");
  kb.innerHTML = "<div style='color:var(--accent);margin-bottom:4px'>keybinds</div>" +
    bound.map(m => `${m.name} <span style="color:#9ca3af">[${m.bind}]</span>`).join("<br>");

  const pot = document.getElementById("potions");
  pot.classList.toggle("hidden", !isOn("PotionHUD"));
  pot.innerHTML = "Speed II  <span style='color:#9ca3af'>0:42</span><br>Strength  <span style='color:#9ca3af'>1:08</span>";

  const ar = document.getElementById("armorhud");
  ar.classList.toggle("hidden", !isOn("ArmorHUD"));
  ar.innerHTML = "Netherite Helm  <span style='color:var(--ok)'>97%</span><br>Netherite Chest  <span style='color:var(--warn)'>38%</span><br>Netherite Legs  <span style='color:var(--ok)'>81%</span><br>Netherite Boots  <span style='color:var(--err)'>12%</span>";

  const th = document.getElementById("targethud");
  th.classList.toggle("hidden", !isOn("TargetHUD"));
  const hp = 11 + Math.sin(Date.now() / 700) * 4;
  const pct = Math.max(5, Math.min(100, (hp / 20) * 100));
  th.innerHTML = `<div class="row"><div class="face"></div><div>
      <b>xFunTime_PvP</b>
      <div style="color:#9ca3af;font-size:11px">12m  ·  armor 20  ·  pops 3</div>
    </div></div><div class="hp"><b style="width:${pct}%"></b></div>`;

  document.getElementById("esp-layer").style.opacity = (isOn("BoxESP") || isOn("GlowESP") || isOn("Chams")) ? "1" : "0";
  document.getElementById("bossbar").classList.toggle("hidden", !isOn("StaffAlert"));
}

function renderPanels() {
  const root = document.getElementById("panels");
  root.innerHTML = "";
  CATS.forEach(cat => {
    const panel = document.createElement("div");
    panel.className = "panel";
    const mods = MODULES.filter(m => m.cat === cat);
    panel.innerHTML = `<h4>${cat.toUpperCase()} <span>${mods.filter(m => m.on).length}</span></h4>`;
    mods.forEach(m => {
      const row = document.createElement("div");
      row.className = "mod" + (m.on ? " on" : "") + (state.selected === m ? " active" : "");
      row.innerHTML = `<span>${m.name}</span><i class="dot"></i>`;
      row.addEventListener("click", e => {
        if (e.button === 0) {
          m.on = !m.on;
          toast(m.name, m.on ? "Enabled" : "Disabled", m.on ? "ok" : "warn");
          save(); renderAll();
        }
      });
      row.addEventListener("contextmenu", e => {
        e.preventDefault();
        state.selected = m;
        renderAll();
      });
      panel.appendChild(row);
    });
    makeDrag(panel, panel.querySelector("h4"));
    root.appendChild(panel);
  });
}

function renderInspector() {
  const m = state.selected || MODULES.find(x => x.on) || MODULES[0];
  state.selected = m;
  document.getElementById("insp-title").textContent = m.name;
  document.getElementById("insp-desc").textContent = m.desc;
  document.getElementById("insp-bind").textContent = m.bind || "NONE";
  const box = document.getElementById("insp-settings");
  box.innerHTML = "";
  (m.settings || []).forEach(s => {
    const wrap = document.createElement("div");
    wrap.className = "setting";
    if (s.type === "bool") {
      wrap.innerHTML = `<label>${s.k} <div class="toggle ${s.v ? "on" : ""}"><i></i></div></label>`;
      wrap.querySelector(".toggle").onclick = () => { s.v = !s.v; save(); renderInspector(); };
    } else if (s.type === "num") {
      wrap.innerHTML = `<label>${s.k}<span>${s.v}</span></label>
        <input type="range" min="${s.min}" max="${s.max}" step="${s.step || 1}" value="${s.v}" />`;
      wrap.querySelector("input").oninput = e => {
        s.v = Number(e.target.value);
        wrap.querySelector("span").textContent = s.v;
        save();
      };
    } else if (s.type === "mode") {
      wrap.innerHTML = `<label>${s.k}</label><div class="modes"></div>`;
      const modes = wrap.querySelector(".modes");
      s.options.forEach(opt => {
        const b = document.createElement("button");
        b.textContent = opt;
        b.className = opt === s.v ? "on" : "";
        b.onclick = () => { s.v = opt; save(); renderInspector(); };
        modes.appendChild(b);
      });
    } else if (s.type === "color") {
      wrap.innerHTML = `<label>${s.k}<input type="color" value="${s.v}" /></label>`;
      wrap.querySelector("input").oninput = e => { s.v = e.target.value; save(); };
    }
    box.appendChild(wrap);
  });
}

function renderFriends() {
  const ul = document.getElementById("friend-list");
  ul.innerHTML = state.friends.map(n => `<li>${n}<button data-n="${n}">×</button></li>`).join("");
  ul.querySelectorAll("button").forEach(b => b.onclick = () => {
    state.friends = state.friends.filter(x => x !== b.dataset.n);
    toast("Friends", "- " + b.dataset.n, "warn");
    save(); renderFriends();
  });
}

function renderThemes() {
  const box = document.getElementById("themes");
  box.innerHTML = "";
  THEMES.forEach(t => {
    const d = document.createElement("button");
    d.className = "theme-dot";
    d.dataset.id = t.id;
    d.title = t.name;
    d.style.background = t.id === "RAINBOW"
      ? "conic-gradient(red, orange, yellow, green, cyan, blue, magenta, red)"
      : t.accent;
    d.onclick = () => { state.theme = t.id; save(); applyTheme(); toast("Theme", t.name, "ok"); };
    box.appendChild(d);
  });
}

function renderEsp() {
  const layer = document.getElementById("esp-layer");
  layer.innerHTML = "";
  const players = [
    { name: "xFunTime_PvP", x: 58, y: 38, w: 7, h: 22, hp: 0.62, friend: false },
    { name: "Neoter", x: 28, y: 46, w: 5, h: 16, hp: 0.9, friend: true },
    { name: "holy_kit", x: 74, y: 50, w: 4.5, h: 14, hp: 0.4, friend: true }
  ];
  players.forEach(p => {
    const el = document.createElement("div");
    el.className = "esp-box";
    el.style.left = p.x + "%";
    el.style.top = p.y + "%";
    el.style.width = p.w + "vw";
    el.style.height = p.h + "vh";
    if (p.friend) el.style.borderColor = "var(--ok)";
    el.innerHTML = `<span style="${p.friend ? "color:var(--ok)" : ""}">${p.name}  ${Math.round(p.hp * 20)}</span><i><b style="height:${p.hp * 100}%"></b></i>`;
    layer.appendChild(el);
  });
}

function renderHotbar() {
  const items = ["⚔", "🪓", "◎", "✦", "▣", "🍎", "💧", "☄", "◆"];
  const labels = ["", "", "16", "", "64", "8", "3", "", ""];
  document.getElementById("hotbar").innerHTML = items.map((it, i) =>
    `<div class="slot ${i === 0 ? "sel" : ""}">${it}<small style="position:absolute;font-size:9px">${labels[i]}</small></div>`
  ).join("");
  document.getElementById("hearts").textContent = "❤❤❤❤❤❤❤❤❤❤";
}

function particles() {
  const root = document.getElementById("particles");
  for (let i = 0; i < 28; i++) {
    const m = document.createElement("div");
    m.className = "mote";
    m.style.left = Math.random() * 100 + "%";
    m.style.animationDuration = 8 + Math.random() * 14 + "s";
    m.style.animationDelay = -Math.random() * 12 + "s";
    m.style.opacity = 0.2 + Math.random() * 0.5;
    root.appendChild(m);
  }
}

function makeDrag(el, handle) {
  let ox = 0, oy = 0, drag = false;
  handle.addEventListener("mousedown", e => {
    drag = true; ox = e.clientX; oy = e.clientY; handle.style.cursor = "grabbing";
  });
  window.addEventListener("mousemove", e => {
    if (!drag) return;
    el.style.position = "relative";
    el.style.left = (parseFloat(el.style.left || 0) + e.clientX - ox) + "px";
    el.style.top = (parseFloat(el.style.top || 0) + e.clientY - oy) + "px";
    ox = e.clientX; oy = e.clientY;
  });
  window.addEventListener("mouseup", () => { drag = false; });
}

function makeHudDrag() {
  document.querySelectorAll(".hud-el").forEach(el => {
    let drag = false, ox = 0, oy = 0;
    el.addEventListener("mousedown", e => { drag = true; ox = e.clientX - el.offsetLeft; oy = e.clientY - el.offsetTop; });
    window.addEventListener("mousemove", e => {
      if (!drag) return;
      el.style.left = e.clientX - ox + "px";
      el.style.top = e.clientY - oy + "px";
      el.style.right = "auto"; el.style.bottom = "auto";
    });
    window.addEventListener("mouseup", () => drag = false);
  });
}

function toggleGui(force) {
  state.gui = force !== undefined ? force : !state.gui;
  document.getElementById("clickgui").classList.toggle("hidden", !state.gui);
  document.getElementById("btn-gui").textContent = state.gui ? "Закрыть ClickGUI" : "Right Shift · ClickGUI";
}

function renderAll() {
  applyTheme();
  renderPanels();
  renderInspector();
  renderHud();
  renderFriends();
}

function onFriendCommand(text) {
  const p = text.trim().split(/\s+/);
  if (p[0]?.toLowerCase() !== "/friend") return false;
  const sub = (p[1] || "").toLowerCase();
  if (sub === "add" && p[2]) {
    if (!state.friends.includes(p[2])) state.friends.push(p[2]);
    toast("Friends", "+ " + p[2], "ok");
  } else if ((sub === "remove" || sub === "del") && p[2]) {
    state.friends = state.friends.filter(x => x.toLowerCase() !== p[2].toLowerCase());
    toast("Friends", "- " + p[2], "warn");
  } else if (sub === "list") {
    toast("Friends", state.friends.join(", ") || "пусто", "ok");
  } else if (sub === "clear") {
    state.friends = [];
    toast("Friends", "cleared", "warn");
  }
  save(); renderFriends();
  return true;
}

load();
renderThemes();
renderHotbar();
renderEsp();
particles();
renderAll();
makeHudDrag();

document.getElementById("btn-gui").onclick = () => toggleGui();
document.getElementById("btn-help").onclick = () => document.getElementById("help").classList.remove("hidden");
document.getElementById("help-close").onclick = () => document.getElementById("help").classList.add("hidden");
document.getElementById("friend-form").onsubmit = e => {
  e.preventDefault();
  const v = document.getElementById("friend-name").value.trim();
  if (!v) return;
  if (!onFriendCommand(v.startsWith("/") ? v : "/friend add " + v)) {
    if (!state.friends.includes(v)) state.friends.push(v);
    toast("Friends", "+ " + v, "ok");
    save(); renderFriends();
  }
  document.getElementById("friend-name").value = "";
};

document.getElementById("insp-bind").onclick = () => {
  state.binding = state.selected;
  document.getElementById("bind-overlay").classList.remove("hidden");
};

window.addEventListener("keydown", e => {
  if (state.binding) {
    if (e.key === "Escape") { state.binding = null; document.getElementById("bind-overlay").classList.add("hidden"); return; }
    if (e.key === "Delete" || e.key === "Backspace") state.binding.bind = "NONE";
    else state.binding.bind = e.key.toUpperCase();
    state.binding = null;
    document.getElementById("bind-overlay").classList.add("hidden");
    save(); renderAll();
    return;
  }
  if (e.code === "ShiftRight" || (e.key === "Shift" && e.location === 2)) {
    e.preventDefault();
    toggleGui();
  }
  if (e.key === "Escape" && state.gui) toggleGui(false);
});

setInterval(() => {
  state.fps = 220 + Math.floor(Math.random() * 30);
  state.ping = 28 + Math.floor(Math.random() * 18);
  renderHud();
  if (state.theme === "RAINBOW") applyTheme();
}, 500);

toggleGui(true);
toast("Noether", "Right Shift — закрыть / открыть ClickGUI", "ok");
